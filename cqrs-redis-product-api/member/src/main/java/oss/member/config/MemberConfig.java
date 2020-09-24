package oss.member.config;

import java.util.concurrent.Executor;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jaceshim on 2017. 3. 21..
 */
@Configuration
@EnableAsync
@Order(1)
@Slf4j
@EntityScan("oss")
public class MemberConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>  {
	
	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).modules(new JavaTimeModule()).build();
	}

	@Bean(name = "asyncExecutor")
    public Executor asyncExecutor() 
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
	
//	@Bean
//	public DefaultCookieSerializer cookieSerializer() {
//		return new DefaultCookieSerializer();
//	}
//	
//
//	private class SpringCacheConfigurer implements ServletContextInitializer {
//
//		@Override
//		public void onStartup(ServletContext servletContext) throws ServletException {
////			SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
//			DefaultCookieSerializer serializer = cookieSerializer();
//			//serializer.setCookieMaxAge(sessionCookieConfig.getMaxAge());
//			//serializer.setCookieName(sessionCookieConfig.getName());
//			serializer.setCookiePath("/"); 
//			serializer.setDomainName("jaceshim.com");
//			serializer.setUseHttpOnlyCookie(true);
//			//serializer.setUseSecureCookie(sessionCookieConfig.isSecure());
//		}
//	}

	//@Bean
	//public JedisConnectionFactory jedisConnectionFactory() {
	//	return new JedisConnectionFactory();
	//}


	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
//		factory.addInitializers(new SpringCacheConfigurer());
		
	}
}
