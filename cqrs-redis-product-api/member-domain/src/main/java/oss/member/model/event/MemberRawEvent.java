package oss.member.model.event;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import oss.core.event.RawEvent;
import oss.core.infra.InfraConstants;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Entity
@Table(name = "raw_event")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "seq" })
@NoArgsConstructor
public class MemberRawEvent implements RawEvent<String>{
	/** seq */
	@Id
	@Column(name = "seq", nullable = false)
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	/**  이벤트 식별자 */
	@Column(name = "identifier", nullable = false)
	private String identifier;

	/** 이벤트 유형 */
	@Column(name = "type", nullable = false)
	private String type;

	/** 이벤트 버전 */
	@Column(name="version", nullable = false)
	private Long version;

	/** 이벤트 payload */
	@Column(name="payload", nullable = false)
	private String payload;

	/** 이벤트 생성일시 */
	@Column(name = "created", nullable = false)
	//@Type(type= InfraConstants.LOCAL_DATE_TIME_TYPE)
	private String created;

	public MemberRawEvent(String identifier, String type, Long version, String payload, String created) {
		this.identifier = identifier;
		this.type = type;
		this.version = version;
		this.payload = payload;
		this.created = created;
	}
}
