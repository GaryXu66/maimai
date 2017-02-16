package maimai.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.plexus.util.StringUtils;

import maimai.app.entity.User;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 判断是否是http请求
		if (!(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)) {
			throw new ServletException(
					"OncePerRequestFilter just supports HTTP requests");
		}
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(true);

		User user = (User) session.getAttribute("user");
		if(null != user){
			chain.doFilter(request, response);
			return;
		}else{
			// 用户不存在,踢回登录页面
			String returnUrl = httpRequest.getContextPath() + "/user/loginPage";
			httpRequest.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("text/html; charset=UTF-8"); // 转码
			httpResponse.getWriter()
					.println(
							"<script language=\"javascript\">alert(\"您还没有登录，请先登录!\");if(window.opener==null){window.top.location.href=\""
									+ returnUrl
									+ "\";}else{window.opener.top.location.href=\""
									+ returnUrl
									+ "\";window.close();}</script>");
			return;
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
