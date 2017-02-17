package maimai.app.service;

import maimai.app.entity.User;

public interface IUserService extends IBaseService{

	void addUser(User user);
	
	User loginAndGetUser(String name, String pass);
}
