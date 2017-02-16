package maimai.app.controller.front.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import maimai.app.controller.front.base.FrontBaseController;
import maimai.app.entity.User;

@Controller
@RequestMapping("/account")
public class AcountController extends FrontBaseController {
	
	@RequestMapping("idx")
	public String index(Model model){
		User user = (User) session.getAttribute("user");
		if(null != user){
			model.addAttribute("email", user.getEmail());
		}else{
			return "redirect:/user/loginPage";
		}
		return "/front/account/index";
	}

}
