package oss.member.service;

import static java.lang.String.format;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import oss.member.exception.DuplicateMemberEmailException;
import oss.member.exception.DuplicateMemberIdException;
import oss.member.exception.NotExistsMemberException;
import oss.member.infra.MemberEventHandler;
import oss.member.infra.read.MemberReadRepository;
import oss.member.model.Member;
import oss.member.model.command.MemberCommand;
import oss.member.model.google.Recaptcha;

/**
 * Created by jaceshim on 2017. 3. 22..
 */
@Service
@Transactional
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberEventHandler memberEventHandler;

	@Autowired
	MemberReadRepository memberReadRepository;
	
	@Autowired
	RestTemplate restTemplate;

	public Recaptcha token(String token) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("secret", "6LdZJuAZAAAAAKK6imuK_An_Z6Wfa2jli2wRJoRr");
        map.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        Recaptcha response = restTemplate.postForObject( url, request, Recaptcha.class );
        return response;
    }
	
	/**
	 * 회원 등록
	 * @param memberCreateCommand
	 * @return
	 */
	public Member createMember(MemberCommand.CreateMember memberCreateCommand) {
		Member findMember = memberEventHandler.find(memberCreateCommand.getId());
		if (findMember != null) {
			throw new DuplicateMemberIdException(memberCreateCommand.getId() + " is already registered.");
		}

		if (isDuplicateEmail(memberCreateCommand.getEmail())) {
			throw new DuplicateMemberEmailException(memberCreateCommand.getEmail() + " is already registered.");
		}

		Member member = Member.create(memberCreateCommand);
		// 이벤트 저장
		memberEventHandler.save(member);

		return member;
	}

	/**
	 * 회원 조회
	 *
	 * @param id
	 * @return
	 */
	public Member getMember(String id) {
		final Member member = memberEventHandler.find(id);
		if (member == null) {
			throw new NotExistsMemberException(id + "is not exists!");
		}
		return member;
	}

	/**
	 * 회원 비밀번호 변경
	 * @param id
	 * @param memberChangePasswordCommand
	 * @return
	 */
	public Member changePassword(String id, MemberCommand.ChangePassword memberChangePasswordCommand) {
		final Member member = getMember(id);
		member.changePassword(memberChangePasswordCommand);

		memberEventHandler.save(member);

		return member;
	}

	/**
	 * 회원 탈퇴여부 변경
	 * @param id
	 * @param memberChangeWithdrawalCommand
	 * @return
	 */
	public Member changeWithdrawal(String id, MemberCommand.ChangeWithdrawal memberChangeWithdrawalCommand) {
		final Member member = getMember(id);
		member.changeWithdrawal(memberChangeWithdrawalCommand);

		memberEventHandler.save(member);

		return member;
	}

	/**
	 * 회원정보 변경
	 *
	 * @param id
	 * @param memberChangeProfileCommand
	 * @return
	 */
	public void changeProfile(String id, MemberCommand.ChangeProfile memberChangeProfileCommand) {
		final Member member = getMember(id);
		member.changeProfile(memberChangeProfileCommand);

		memberEventHandler.save(member);
	}

	/**
	 * 이메일 중복 확인
	 * @param email
	 * @return
	 */
	private boolean isDuplicateEmail(String email) {
		final List<Member> members = memberEventHandler.findAll();
		final long duplicatedEmailCount = members.stream()
			.filter(member -> member.getEmail().equals(email)).count();

		if (duplicatedEmailCount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 회원 로그인
	 *
	 * @param memberLoginCommand
	 * @return
	 */
	public void login(MemberCommand.Login memberLoginCommand) {
		final Member member = getMember(memberLoginCommand.getId());
		member.login();

		memberEventHandler.save(member);
	}

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		oss.member.model.read.Member member = memberReadRepository.findAllById(id);
		if (member == null) {
			throw new UsernameNotFoundException(format("{} is not found", id));
		}

		return new LoginMemberDetails(member);
	}
}
