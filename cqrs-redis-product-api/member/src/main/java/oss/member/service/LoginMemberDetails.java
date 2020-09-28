package oss.member.service;

import org.springframework.security.core.authority.AuthorityUtils;

import oss.member.model.read.Member;

/**
 * Created by jaceshim on 2017. 4. 15..
 */
public class LoginMemberDetails extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177270655332013134L;
	private final Member member;

	public LoginMemberDetails(Member member){
		super(
			member.getId(),
			member.getPassword(),
			AuthorityUtils.createAuthorityList("ROLE_USER")
		);
		this.member = member;
	}
}