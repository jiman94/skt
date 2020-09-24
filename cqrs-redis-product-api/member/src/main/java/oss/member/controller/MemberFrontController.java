package oss.member.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import oss.core.infra.InfraConstants;
import oss.member.config.RedisCmd;
import oss.member.infra.read.MemberReadRepository;
import oss.member.infra.read.OrderReadRepository;
import oss.member.model.command.MemberCommand;
import oss.member.model.read.Member;
import oss.member.model.read.MemberAuthToken;
import oss.member.service.MemberService;
import oss.order.model.read.Order;

/**
 * Created by jaceshim on 2017. 3. 30..
 */
@Controller
@Slf4j
public class MemberFrontController {

	private static final String LOGIN_MEMBER_SESSION_ID_KEY = "LOGIN_MEMBER_SESSION_ID_KEY";

	@Autowired
	RedisCmd redisCmd;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MemberReadRepository memberReadRepository;

	@Autowired
	private OrderReadRepository orderReadRepository;

	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String signup(Model model) {
		return "regist";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MemberAuthToken> login(@RequestParam String id, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, password);
		Authentication authentication = this.authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);

//		HttpSession session = request.getSession(true);
//		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//		session.setAttribute(LOGIN_MEMBER_SESSION_ID_KEY, authentication.getName());
		
		//TODO 아이디에 관련한 정보 없으면 넣기??
		redisCmd.hput(InfraConstants.USER_H_KEY, id, id);
		Cookie c = new Cookie("SESSION", id);
		c.setDomain("jaceshim.com");
		c.setHttpOnly(true);
		response.addCookie(c);

		MemberCommand.Login memberLoginCommand = new MemberCommand.Login(id, password);
		memberService.login(memberLoginCommand);

		UserDetails details = this.memberService.loadUserByUsername(id);
		final MemberAuthToken memberAuthToken = new MemberAuthToken(details.getUsername(), "token");

		return new ResponseEntity<>(memberAuthToken, HttpStatus.OK);
	}

	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String mypage(@AuthenticationPrincipal UserDetails loginUser, Model model) {

		log.debug("로그인 사용자명 {}", loginUser.getUsername());
		return "mypage";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Member> getMember(@AuthenticationPrincipal UserDetails loginUser) {
		final Member member = memberReadRepository.findAllById(loginUser.getUsername());
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal UserDetails loginUser) {

		final String memberId = loginUser.getUsername();

		final List<Order> orders = orderReadRepository.findByMemberId(memberId);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

}
