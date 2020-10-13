package oss.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import brave.http.HttpTracing;
import lombok.RequiredArgsConstructor;
import oss.member.interceptor.SamplingInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcTracingConfig implements WebMvcConfigurer {

    private final HttpTracing httpTracing;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SamplingInterceptor(httpTracing));
    }
}
