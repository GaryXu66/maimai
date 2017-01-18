package maimai.app.dao;

import maimai.app.base.BaseDAO;
import maimai.app.entity.User;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public interface UserDAO extends BaseDAO{
	
	public void add(User user);

	public List<User> findUserByPass(Map<String, String> params);

}
