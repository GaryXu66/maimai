package maimai.app.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSON;

import maimai.app.base.ServiceException;

/**
 * 
 * 2017年2月11日上午11:48:26 @author Gary
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	//@ExceptionHandler(value={RuntimeException.class,MyRuntimeException.class})
	//@ExceptionHandler//处理所有异常
 	@ExceptionHandler(ServiceException.class)
    public void exceptionHandler(ServiceException e, HttpServletRequest req, HttpServletResponse response) {
 		response.setCharacterEncoding("utf-8");
 		if(HttpUtils.isAjax(req)){
 			try {
				response.getWriter().write(JSON.toJSONString(new ActionResult<Object>(e)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
 		}else{
 			try {
				response.sendRedirect("/error500");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
 		}
    }

}
