package maimai.app.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
	
	/**
	 * 判断请求是否为ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request){
	    return  (request.getHeader("X-Requested-With") != null  
	    && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString())) ;
	}
}
