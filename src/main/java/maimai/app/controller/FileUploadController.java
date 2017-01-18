package maimai.app.controller;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import maimai.app.base.BaseController;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("uploadfile")
public class FileUploadController extends BaseController{

	@RequestMapping("pic")
	public void upload(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("============upload file start");
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		response.setContentType("text/plain;charset=UTF-8");  /****为兼容IE浏览器，相应头修改为字符串类型*****/
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(response.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//判断 request 是否有文件上传,即多部分请求
		if(!multipartResolver.isMultipart(request)){
	    	return ;
		}
		// 单个上传文件请求
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// 取得上传文件
		MultipartFile file = multiRequest.getFile(multiRequest.getFileNames().next());
		if (file == null) {
	    	return ;
		}
		JSONObject obj =new JSONObject();
		String realPath = request.getSession().getServletContext().getRealPath("/") + "imgs\\";
		String fileName = System.currentTimeMillis()+ file.getOriginalFilename();
		try {
			if (!file.isEmpty()) {
				FileOutputStream fos = new FileOutputStream(realPath+ fileName);
				int b = 0;
				// 获得输入流
				InputStream in = file.getInputStream();
				while ((b = in.read()) != -1) {
					fos.write(b); // 写出
				}
				fos.flush();
				fos.close();
				in.close();
			}
		} catch (Exception e) {

		}
		String contPath = request.getContextPath()+"/imgs/";
		obj.put("url", contPath+fileName);
		try {
			out.writeBytes(java.net.URLEncoder.encode(obj.toString(), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;

	}
}
