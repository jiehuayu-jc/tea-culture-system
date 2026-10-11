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
							"/admin",
							"/admin/",
							"/admin/**",
							"/yonghu/register",
							"/yonghu/login",
							"/shangjia/register",
							"/shangjia/login",
							"/users/register",
							"/users/login",
							"/file/upload",
							"/**/yonghu/register",
							"/**/yonghu/login",
							"/**/shangjia/register",
							"/**/shangjia/login",
							"/**/users/register",
							"/**/users/login",
							"/**/file/upload");
	}

		@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 管理端静态页：源码在 src/main/resources/admin-ui，构建时自动进入 classpath，
		// 访问 /admin/index.html 即可，无需任何手工拷贝（该映射比下方 /** 更精确，优先生效）
		registry.addResourceHandler("/admin/**")
				.addResourceLocations("classpath:/admin-ui/");
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/resources/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/admin/")
				.addResourceLocations("classpath:/front/")
				.addResourceLocations("classpath:/front-pc/")
				.addResourceLocations("classpath:/public/")
				// 兜底：工作目录 static/（服务器上 /home/ubuntu/tea/static），
				// 运行期补充的图片等静态文件放这里即可生效，无需重新打包
				.addResourceLocations("file:static/")
				// no-cache = 可缓存但每次必须 revalidate：index.html 发版后立即生效，
				// 带 hash 的 js/css 内容不变时返回 304，代价极小
				.setCacheControl(org.springframework.http.CacheControl.noCache());
	}

	@Override
	public void addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry registry) {
		registry.addRedirectViewController("/admin", "/admin/index.html");
		registry.addRedirectViewController("/admin/", "/admin/index.html");
	}
}
