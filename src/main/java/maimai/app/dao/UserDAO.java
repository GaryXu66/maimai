package maimai.app.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import maimai.app.base.BaseDAO;
import maimai.app.entity.User;

@Component
public interface UserDAO extends BaseDAO{
	
	public void add(User user);

	public User findUserByPass(Map<String, String> params);

}
