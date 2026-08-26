package org.jeecg.modules.shiro.authc.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.shiro.authc.JwtToken;
import org.jeecg.modules.shiro.vo.DefContants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 鉴权登录拦截器
 * @Author: Scott
 * @Date: 2018/10/7
 **/
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 执行登录认证。鉴权失败时返回 false，由 onAccessDenied 输出 401，避免抛异常导致系统 ERROR 堆栈。
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		try {
			executeLogin(request, response);
			return true;
		} catch (AuthenticationException e) {
			request.setAttribute("jwtAuthMessage", e.getMessage());
			return false;
		} catch (Exception e) {
			request.setAttribute("jwtAuthMessage", "Token失效，请重新登录");
			log.warn("JwtFilter login failed: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 1.首先从消息头获取，
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(DefContants.X_ACCESS_TOKEN);
		if (token == null) {
			token = httpServletRequest.getParameter(DefContants.X_ACCESS_TOKEN);
		}
		if (token == null || token.trim().isEmpty()) {
			throw new AuthenticationException("token为空!");
		}
		if (token.endsWith("#/")) {
			StringBuilder tokenTemp = new StringBuilder(token);
			token = new String(tokenTemp.replace(token.length() - 2, token.length(), ""));
		}
		JwtToken jwtToken = new JwtToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");
		httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		Object message = request.getAttribute("jwtAuthMessage");
		String tip = message == null ? "Token失效，请重新登录" : String.valueOf(message);
		Result<Object> result = Result.error(401, tip);
		PrintWriter writer = null;
		try {
			writer = httpServletResponse.getWriter();
			writer.write(JSON.toJSONString(result));
			writer.flush();
		} catch (IOException e) {
			log.warn("JwtFilter write unauthorized response failed: {}", e.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
		return false;
	}

	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}

}
