package oss.order.infra.remote;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import oss.core.infra.InfraConstants;
import oss.member.model.read.Member;
import oss.order.config.RedisCmd;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Component
@Slf4j
public class MemberInfoRepository {

	private static final String SPRING_SESSION_PREFIX = "spring:session:sessions:";

	private static final String LOGIN_MEMBER_SESSION_ID_KEY = "sessionAttr:LOGIN_MEMBER_SESSION_ID_KEY";

	private static final String MEMBER_SERVICE_HOST = "member.jaceshim.com";

	private static final int MEMBER_SERVICE_PORT = 10001;

	@Autowired
	private RestTemplate restTemplate;

	
	@Autowired
	private RedisCmd redisCmd; 
	
	//@Autowired
	//private JedisPool jedisPool;

	/**
	 * 회원정보 조회 using member api
	 * @param memberId
	 * @return
	 */
	public Member findById(String memberId) {
		URI uri = UriComponentsBuilder.newInstance().scheme("http").host(MEMBER_SERVICE_HOST).port(MEMBER_SERVICE_PORT)
			.path("/api/members/" + memberId).build()
			.encode()
			.toUri();

		Member result = restTemplate.getForObject(uri, Member.class);

		return new Member(result.getId(), result.getName(), result.getEmail(), result.getAddress());
	}

	public Member findBySessionId(String sessionId) {
		Member member = null;

		//Jedis jedis = null;
		try {
//			String key = SPRING_SESSION_PREFIX + sessionId;
//			String field = LOGIN_MEMBER_SESSION_ID_KEY;

			//template.getConnectionFactory().getConnection();
			String memberId = (String) redisCmd.hget(InfraConstants.USER_H_KEY, sessionId);
			//jedis = jedisPool.getResource();
			
			//final byte[] valueByte = jedis.hget(key.getBytes(), field.getBytes());
			//String memberId = (String) deserialize(valueByte);
			
			member = findById(memberId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			/*
			 * if (jedis != null) { jedis.close(); }
			 */
		}
		return member;
	}

	private static final Converter<byte[], Object> DESERIALIZER = new DeserializingConverter();

	private static Object deserialize(byte[] bytes) {
		try {
			return DESERIALIZER.convert(bytes);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}
}
