package maimai.service;

import javax.annotation.Resource;

import org.junit.Test;

import maimai.app.entity.User;
import maimai.app.service.UserService;
import maimai.base.BaseTest;

public class TestUserService extends BaseTest {
	@Resource
	private UserService userService;
	
	@Test
	public void testAddUser(){
		User user = new User();
		user.setLoginName("test");
		user.setLoginPass("123");
		user.setPhone("2323545");
		userService.addUser(user);
	}
}
