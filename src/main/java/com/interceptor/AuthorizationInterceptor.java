package com.interceptor;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;

import com.annotation.IgnoreAuth;
import com.entity.TokenEntity;
import com.service.TokenService;
import com.utils.R;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_TOKEN_KEY = "Token";

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private TokenService tokenService;

    /**
     * 按「去掉 context-path、查询串、;jsessionid 等」后的路径判断是否放行登录/注册。
     * 否则 URI 形如 /ctx/yonghu/login;jsessionid=xx 时 endsWith("/yonghu/login") 为 false，会误拦登录。
     */
    private boolean isOpenAuthPath(HttpServletRequest request) {
        // 与 DispatcherServlet 映射一致的路径（推荐，避免手写解析与容器差异）
        String lookupPath = normalizeSlashPath(urlPathHelper.getLookupPathForRequest(request));
        if (isOpenRelativePath(lookupPath)) {
            return true;
        }
        // 带 context-path 时，getServletPath() 一般为 /yonghu/login
        String servletPath = StringUtils.defaultString(request.getServletPath());
        String pathInfo = request.getPathInfo();
        String combined = servletPath;
        if (StringUtils.isNotBlank(pathInfo)) {
            combined = servletPath + pathInfo;
        }
        combined = normalizeSlashPath(combined);
        if (isOpenRelativePath(combined)) {
            return true;
        }
        String uri = request.getRequestURI();
        if (StringUtils.isBlank(uri)) {
            return false;
        }
        int q = uri.indexOf('?');
        String path = q >= 0 ? uri.substring(0, q) : uri;
        int semi = path.indexOf(';');
        if (semi >= 0) {
            path = path.substring(0, semi);
        }
        String ctx = StringUtils.defaultString(request.getContextPath());
        // 多次去掉 context-path（防止网关/配置导致路径重复前缀）
        while (StringUtils.isNotBlank(ctx) && path.startsWith(ctx)) {
            path = path.substring(ctx.length());
        }
        if (StringUtils.isBlank(path)) {
            path = "/";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        while (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (isOpenRelativePath(path)) {
            return true;
        }
        // contextPath 为空但 URI 仍带 /springbootj8kskvkr/... 时，上面 equals 会失败，用「去查询/会话后的后缀」兜底
        String raw = q >= 0 ? uri.substring(0, q) : uri;
        int rs = raw.indexOf(';');
        if (rs >= 0) {
            raw = raw.substring(0, rs);
        }
        return raw.endsWith("/yonghu/login")
                || raw.endsWith("/yonghu/register")
                || raw.endsWith("/yonghu/resetPass")
                || raw.endsWith("/shangjia/login")
                || raw.endsWith("/shangjia/register")
                || raw.endsWith("/users/login")
                || raw.endsWith("/users/register")
                || raw.endsWith("/file/upload");
    }

    private static String normalizeSlashPath(String p) {
        if (StringUtils.isBlank(p)) {
            return "/";
        }
        p = p.trim();
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        while (p.length() > 1 && p.endsWith("/")) {
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }

    /** 兜底：任意容器/代理下只要路径里出现这些片段即放行 */
    private static boolean isQuickOpenByUriContains(HttpServletRequest request) {
        try {
            String uri = request.getRequestURI();
            if (StringUtils.isBlank(uri)) {
                return false;
            }
            int q = uri.indexOf('?');
            if (q >= 0) {
                uri = uri.substring(0, q);
            }
            int semi = uri.indexOf(';');
            if (semi >= 0) {
                uri = uri.substring(0, semi);
            }
            try {
                uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
            } catch (Exception ignored) {
                // ignore
            }
            return uri.contains("/yonghu/login")
                    || uri.contains("/yonghu/register")
                    || uri.contains("/yonghu/resetPass")
                    || uri.contains("/shangjia/login")
                    || uri.contains("/shangjia/register")
                    || uri.contains("/users/login")
                    || uri.contains("/users/register")
                    || uri.contains("/file/upload");
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isOpenRelativePath(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        return "/yonghu/login".equals(path)
                || "/yonghu/register".equals(path)
                || "/yonghu/resetPass".equals(path)
                || "/shangjia/login".equals(path)
                || "/shangjia/register".equals(path)
                || "/users/login".equals(path)
                || "/users/register".equals(path)
                || "/file/upload".equals(path);
    }
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,request-source,Token, Origin,imgType, Content-Type, cache-control,postman-token,Cookie, Accept,authorization");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	// 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
	if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
        	response.setStatus(HttpStatus.OK.value());
            return false;
        }

        // 错误页转发（如业务异常进入 /error）：若仍走拦截器，否则会误返回「请先登录」掩盖真实原因
        if (request.getAttribute("javax.servlet.error.status_code") != null) {
            return true;
        }

        // 最优先：仅看 URI 字符串（解码后）是否包含公开接口路径，不依赖 Handler、UrlPathHelper
        if (isQuickOpenByUriContains(request)) {
            return true;
        }

        // 与 URI 解析无关：只要映射到 Controller 的 login/register，一律放行（本项目仅 Yonghu/Shangjia/Users 三处）
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            String mname = hm.getMethod().getName();
            if ("login".equals(mname) || "register".equals(mname)) {
                return true;
            }
        }

        if (isOpenAuthPath(request)) {
            return true;
        }
        
        IgnoreAuth annotation = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            annotation = AnnotationUtils.findAnnotation(hm.getMethod(), IgnoreAuth.class);
            if (annotation == null) {
                annotation = AnnotationUtils.findAnnotation(hm.getBeanType(), IgnoreAuth.class);
            }
        } else {
            return true;
        }

        //从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        
        /**
         * 不需要验证权限的方法直接放过
         */
        if(annotation!=null) {
        	return true;
        }
        
        TokenEntity tokenEntity = null;
        if(StringUtils.isNotBlank(token)) {
        	tokenEntity = tokenService.getTokenEntity(token);
        }
        
        if(tokenEntity != null) {
        	request.getSession().setAttribute("userId", tokenEntity.getUserid());
        	request.getSession().setAttribute("role", tokenEntity.getRole());
        	request.getSession().setAttribute("tableName", tokenEntity.getTablename());
        	request.getSession().setAttribute("username", tokenEntity.getUsername());
        	return true;
        }
        
			PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// 与 Controller 返回一致使用 HTTP 200，否则部分前端库（如 vue-resource）会把非 2xx 当网络错误
		response.setStatus(HttpServletResponse.SC_OK);
		try {
		    writer = response.getWriter();
		    writer.print(JSONObject.toJSONString(R.error(401, "请先登录")));
		} finally {
		    if(writer != null){
		        writer.close();
		    }
		}
//				throw new EIException("请先登录", 401);
		return false;
    }
}
