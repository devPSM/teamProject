package ksmart39.psmybatis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart39.psmybatis.interceptor.CommonInterceptor;
import ksmart39.psmybatis.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final CommonInterceptor commonInterceptor;
	private final LoginInterceptor loginInterceptor;
	
	public WebConfig(CommonInterceptor commonInterceptor, LoginInterceptor loginInterceptor) {
		this.commonInterceptor = commonInterceptor;
		this.loginInterceptor = loginInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/AdminLTE3/**")
				.excludePathPatterns("/js/**");
		
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/addMember")
				.excludePathPatterns("/memberIdCheck")
				.excludePathPatterns("/login")
				.excludePathPatterns("/AdminLTE3/**")
				.excludePathPatterns("/js/**");
		
		
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
