package maimai.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maimai.app.base.BaseService;
import maimai.app.dao.UserDAO;
import maimai.app.entity.User;

@Transactional
@Service
public class UserService extends BaseService{
	@Resource
	UserDAO userDAO;
	
	public void addUser(User user){
		userDAO.add(user);
	}

	public boolean loginValid(String name, String pass) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("pass", pass);
		List<User> result = userDAO.findUserByPass(params);
		if(null != result && result.size() > 0){
			return true;
		}else{
			return false;
		}
	}
}
