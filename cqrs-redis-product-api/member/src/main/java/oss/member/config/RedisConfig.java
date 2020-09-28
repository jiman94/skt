package oss.member.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//@EnableCaching
//@EnableRedisHttpSession
public class RedisConfig {
	
	@Value("${spring.redis.host}")
	private String sdHost;

	@Value("${spring.redis.port}")
	private int sdPort;
	
//	@Bean
//	public ConfigureRedisAction configureRedisAction() {
//		return ConfigureRedisAction.NO_OP;
//	}

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
	 
//	@Primary
//	@Bean(value = "redisTemplate")
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(redisConnectionFactory);
//		return redisTemplate;
//	}
	
	@Bean(value = "redisTemplateCmd")
	public RedisTemplate<String, Object> redisTemplateCmd(RedisConnectionFactory redisConnectionFactory) {
		ObjectMapper om = new ObjectMapper();
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Json 포멧 강제화 해제 (Unrecognized field 처리)

		Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		redisSerializer.setObjectMapper(om);
				
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        template.getConnectionFactory().getConnection();
		template.afterPropertiesSet();
		return template;
	}


}
