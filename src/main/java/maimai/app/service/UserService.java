package maimai.app.service;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
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
	@Value(value="password.encryptkey")
	private String encryptKey;
	
	public void addUser(User user){
		byte[] encryptPass = null;
		try {
			encryptPass = MD5Util.encrypt(user.getLoginPass(), encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("保存用户时，加密pass出现异常",e);
			return ;
		}
		user.setLoginPass(String.valueOf(encryptPass));
		userDAO.add(user);
	}

	public boolean loginValid(String name, String pass) {
		byte[] encryptPass = null;
		try {
			encryptPass = MD5Util.encrypt(pass, encryptKey, Charset.forName(SysConstant.ENCODE_UTF8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error("验证用户登录，加密pass出现异常",e);
			return false;
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("pass", String.valueOf(encryptPass));
		List<User> result = userDAO.findUserByPass(params);
		if(null != result && result.size() > 0){
			return true;
		}else{
			return false;
		}
	}
}
