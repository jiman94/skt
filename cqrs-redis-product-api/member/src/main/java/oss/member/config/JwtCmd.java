package oss.member.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.JwtMap;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtCmd {
	
	@Autowired
	JsonCmd jsonCmd;
	
    public static String jwtTyp = "JWT";
    
//	@Value("${jwt.client.iss}")
	public static String jwtIss = "smcho";

//	@Value("${jwt.client.expTimeMilli}")
//	public static long jwtExpTimeMilli = 30*1000; // test용도
	public static long jwtExpTimeMilli = 30*1000*60; //30분 

	public Claims getPayload(String token) {
		try {
			String[] body = token.split("\\.");
			if (body.length != 3) {
				log.debug("token is incorrect. token len={}, d={}", body.length, token);
				return null;
			}
			byte[] payload = Base64.getDecoder().decode(body[1]);
			// log.debug("token decode. token len={}", payload.length);

			JwtMap map = (JwtMap) jsonCmd.jsonStringToObj(new String(payload), JwtMap.class);
			// log.debug("token map={}", map);
			
			Claims claims = null;
			if (map != null) claims = new DefaultClaims(map);
			// log.debug("token claims={}", claims);
			
			return claims;
		} catch(Exception e) {
			log.warn("Payload cannot gathering. err={}", e.getMessage());
			return null;
		}
	}
	
	public String getSubject(String token) {
		if (getPayload(token) == null) return null;
		return getPayload(token).getSubject();
	}
	
	public Boolean isTokenRefresh(String token, int minute) {
		Claims map = getPayload(token);
		if (map == null) return true;

		Date expiration = map.getExpiration();
		if (expiration == null) return true;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expiration);
		calendar.add(Calendar.MINUTE, minute*-1);
		
		log.debug("exp = {}", expiration.getTime());
		return calendar.getTime().before(new Date());
	}
	
	public Claims getAllClaimsFromToken(String token) {
		return getPayload(token);
	}
	
	public Date getExpirationDateFromToken(String token) {
		Claims map = getPayload(token);
		if (map == null) return null;
		
		return map.getExpiration();
	}
	
	public Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		if (expiration == null) return true;
		
		return expiration.before(new Date());
	}
	
	///////////////////////////////////////
	
	private Claims getAllClaimsFromToken(String jwtCliSecretKey, String token) {
		Claims claims = null; 
		
		try {
			claims = Jwts.parser().setSigningKey(jwtCliSecretKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			log.warn("expired token, payload={}", getPayload(token));
		} catch (UnsupportedJwtException e) {
			log.error("not supported token, payload={}", getPayload(token));
		} catch (MalformedJwtException e) {
			log.error("incorrected token, payload={}", getPayload(token));
		} catch (SignatureException e) {
			log.error("invalid signature, payload={}", getPayload(token));
		} catch (IllegalArgumentException e) {
			log.error("unknown token, payload={}", getPayload(token));
		}
		
		return claims;
	}
	
	private Date getExpirationDateFromToken(String jwtCliSecretKey, String token) {
		try {
			return getAllClaimsFromToken(jwtCliSecretKey, token).getExpiration();
		} catch (Exception e) {
			return null;
		}
	}
	
	private Boolean isTokenExpired(String jwtCliSecretKey, String token) {
		Date expiration = getExpirationDateFromToken(jwtCliSecretKey, token);
		if (expiration == null) return true;
		return expiration.before(new Date());
	}
	
	public Boolean validateToken(String jwtCliSecretKey, String token) {
		return !isTokenExpired(jwtCliSecretKey, token);
	}

	///////////////////////////////////////

	public String getToken(
			String jwtSecretKey,
			String agentId,
			long exptime
			) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
			Map<String, Object> claims = new HashMap<>();
			claims.put("agentId", agentId); // 토큰 사용 대상 목적(리프레쉬, 목록조회) 이 되는 corpId
			claims.put("sIp", "*.*.*.*"); // 허용가능한 url
			return genToken(jwtSecretKey, exptime, jwtIss, agentId, claims);
		} catch (UnknownHostException e) {
			log.error("do not read ip ",e);
			return "";
		}
	}
	
	public String getToken(
			String jwtSecretKey,
			String agentId,
			String ip 
		) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("agentId", agentId); // 토큰 사용 대상 목적(리프레쉬, 목록조회) 이 되는 corpId
		claims.put("sIp", ip); // 허용가능한 url
		
		return genToken(jwtSecretKey, jwtExpTimeMilli, jwtIss, agentId, claims);
	}

	/**
	 * Token 생성
	 * @param isRefresh refresh 토큰 여부
	 * @return 토큰 문자열
	 */
	private String genToken(
			String jwtSecretKey, 
			long jwtExpTimeMilli, 
			String jwtIss, 
			String id, 
			Map<String, Object> claims
	) {
		try {
			Date createdDate = new Date();

			claims.put(Claims.ISSUER, jwtIss); // 토큰 발행자 정보
			claims.put("typ", jwtTyp); 
			
			Date expirationDate = null;
			expirationDate = new Date(createdDate.getTime() + jwtExpTimeMilli);
			
			return Jwts.builder()
					.setClaims(claims) // payload 로 넣으면 받는 부분이 애매하네??
//					.setPayload( claims )
					.setSubject(id)
					.setIssuedAt(createdDate)
					.setExpiration(expirationDate)
					.signWith(SignatureAlgorithm.HS256, jwtSecretKey)
					.compact();
			
		} catch(Exception e) {
			log.error("JWT generate fail. e={}", e.getMessage(), e);
			return "";
		}
	}

	
}

