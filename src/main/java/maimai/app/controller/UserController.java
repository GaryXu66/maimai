package maimai.app.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.StringUtils;

import maimai.app.base.BaseController;
import maimai.app.entity.User;
import maimai.app.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Resource
	UserService userService;
	
	@RequestMapping(value="login")
	public String login(Model model, HttpSession session , @RequestParam String name, @RequestParam String pass){
		
		boolean loginSeccess = userService.loginValid(name, pass);
		if(loginSeccess){
			session.setAttribute("name", name);
			
			return "front/login/login_success";
		}else{
			return "front/login/login_error";
		}
	}
	
	@RequestMapping("loginPage")
	public String loginPage(){
		return "front/login/login";
	}
	
	@RequestMapping("regPage")
	public String regPage(){
		return "front/login/reg";
	}
	
	@RequestMapping("reg")
	public String reg(Model model,
			@RequestParam(value="email", required=true) String email, 
			@RequestParam(value="userName", required=true) String name, 
			@RequestParam(value="passA", required=true) String passA, 
			@RequestParam(value="passB", required=true) String passB,
			@RequestParam(value="pic") String picPath){
		if(StringUtils.isNullOrEmpty(passA) || StringUtils.isNullOrEmpty(passB) || !passA.equals(passB)){
			model.addAttribute("error", "两次的密码不一致");
			return "front/login/reg";
		}
		
		User user = new User();
		user.setEmail(email);
		user.setLoginName(name);
		user.setLoginPass(passA);
		userService.addUser(user);
		return "front/login/login";
	}

}
