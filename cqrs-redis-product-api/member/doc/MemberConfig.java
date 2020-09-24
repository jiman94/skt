package jace.shim.springcamp2017.member.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.web.http.DefaultCookieSerializer;

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
@EntityScan("jace.shim.springcamp2017")
public class MemberConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>  {
	
	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).modules(new JavaTimeModule()).build();
	}

	@Bean
	public DefaultCookieSerializer cookieSerializer() {
		return new DefaultCookieSerializer();
	}


	private class SpringCacheConfigurer implements ServletContextInitializer {

		@Override
		public void onStartup(ServletContext servletContext) throws ServletException {
			SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
			DefaultCookieSerializer serializer = cookieSerializer();
			//serializer.setCookieMaxAge(sessionCookieConfig.getMaxAge());
			//serializer.setCookieName(sessionCookieConfig.getName());
			serializer.setCookiePath(sessionCookieConfig.getPath());
			serializer.setDomainName(sessionCookieConfig.getDomain());
			serializer.setUseHttpOnlyCookie(sessionCookieConfig.isHttpOnly());
			//serializer.setUseSecureCookie(sessionCookieConfig.isSecure());
		}
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}


	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.addInitializers(new SpringCacheConfigurer());
		
	}
}
