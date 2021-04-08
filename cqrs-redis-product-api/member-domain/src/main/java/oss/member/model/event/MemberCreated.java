package oss.member.model.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import oss.core.utils.Time;
import oss.core.utils.Util;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
@Getter
@NoArgsConstructor
public class MemberCreated extends AbstractMemberEvent {
	private String name;
	private String email;
	private String password;
	private String address;
	private String created;

	public MemberCreated(String id, String name, String email, String password, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.created = Util.toFormat();
	}
}
