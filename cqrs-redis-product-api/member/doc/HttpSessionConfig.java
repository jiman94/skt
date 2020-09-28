package jace.shim.springcamp2017.member.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import lombok.Value;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
//extends RedisHttpSessionConfiguration {

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}
//
//	@Bean
//	public RedisTemplate<Object, Object> sessionRedisTemplate(
//		RedisConnectionFactory connectionFactory) {
//		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//		template.setKeySerializer(new StringRedisSerializer());
//		//template.setHashKeySerializer(new StringRedisSerializer());
//		template.setHashValueSerializer(new StringRedisSerializer());
//		template.setConnectionFactory(connectionFactory);
//		return template;
//	}
/*
	@Value("${spring.redis.host}")
	private String sdHost;

	@Value("${spring.redis.port}")
	private int sdPort;
	
	@Bean
	public ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}

	@Autowired
	private RedisClusterConfigurationProperties clusterProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		if(clusterProperties.getNodes() == null) {
			RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
			redisConf.setHostName(sdHost);
			redisConf.setPort(sdPort);
			return new LettuceConnectionFactory(redisConf);
		}
		
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
		clusterProperties.getNodes().forEach(s -> {
			String[] url = s.split(":");
			redisClusterConfiguration.clusterNode(url[0], Integer.parseInt(url[1]));
		});
		return new LettuceConnectionFactory(redisClusterConfiguration);
	}
	 
	@Bean(value = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}	
	*/
	
}
