package maimai.app.service.impl;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maimai.app.constant.SysConstant;
import maimai.app.dao.UserDAO;
import maimai.app.entity.User;
import maimai.app.service.IUserService;
import maimai.app.util.MD5Util;

@Transactional
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements IUserService{
	@Resource
	UserDAO userDAO;
	@Value("${password.encryptkey}")
	public String encryptKey;
	
	@Override
	public void addUser(User user){
		String encryptPass = MD5Util.encrypt(user.getPass(), encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		user.setPass(encryptPass);
		userDAO.add(user);
	}

	@Override
	public User loginAndGetUser(String name, String pass) {
		String encryptPass = MD5Util.encrypt(pass, encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("pass", encryptPass);
		return userDAO.findUserByPass(params);
	}
}
