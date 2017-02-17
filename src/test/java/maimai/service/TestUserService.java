package maimai.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import maimai.app.entity.User;
import maimai.app.service.IUserService;
import maimai.base.BaseTest;

public class TestUserService extends BaseTest {
	@Resource
	private IUserService userService;
	
	@Test
	public void testAddUser(){
		User user = new User();
		user.setName("test");
		user.setPass("123");
		user.setPhone("2323545");
		user = (User) userService.getEntityById(33, User.class);
		System.out.println(JSON.toJSON(user));
	}
}
