package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.interceptor.AuthorizationInterceptor;

/**
 * 使用 WebMvcConfigurer，避免继承 WebMvcConfigurationSupport导致 Spring Boot MVC 自动配置被整份关掉，
 * 从而出现拦截器与 Handler映射异常、登录接口一直被 401 等问题。
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private AuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/static/**")
				.excludePathPatterns(
						"/error",
						"/**/error",
						"/yonghu/register",
						"/yonghu/login",
						"/yonghu/resetPass",
						"/shangjia/register",
						"/shangjia/login",
						"/users/register",
						"/users/login",
						"/file/upload",
						"/**/yonghu/register",
						"/**/yonghu/login",
						"/**/yonghu/resetPass",
						"/**/shangjia/register",
						"/**/shangjia/login",
						"/**/users/register",
						"/**/users/login",
						"/**/file/upload");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/resources/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/admin/")
				.addResourceLocations("classpath:/front/")
				.addResourceLocations("classpath:/front-pc/")
				.addResourceLocations("classpath:/public/");
	}
}
