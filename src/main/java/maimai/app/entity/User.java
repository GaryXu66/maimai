package maimai.app.entity;

import maimai.app.base.BaseEntity;

public class User extends BaseEntity{
	private String loginName;
	private String loginPass;
	private String userName;
	private int age;
	private String phone;
	private String email;
	private String IdNum;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
	public String getIdNum() {
		return IdNum;
	}
	public void setIdNum(String idNum) {
		IdNum = idNum;
	}
	
	
	
}
