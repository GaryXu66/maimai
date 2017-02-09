package maimai.app.service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maimai.app.base.BaseService;
import maimai.app.constant.SysConstant;
import maimai.app.dao.UserDAO;
import maimai.app.entity.User;
import maimai.app.util.MD5Util;

@Transactional
@Service
public class UserService extends BaseService{
	@Resource
	UserDAO userDAO;
	@Value("${password.encryptkey}")
	public String encryptKey;
	
	public void addUser(User user){
		String encryptPass = MD5Util.encrypt(user.getLoginPass(), encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		user.setLoginPass(encryptPass);
		userDAO.add(user);
	}

	public boolean loginValid(String name, String pass) {
		String encryptPass = MD5Util.encrypt(pass, encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("pass", encryptPass);
		List<User> result = userDAO.findUserByPass(params);
		if(null != result && result.size() > 0){
			return true;
		}else{
			return false;
		}
	}
}
