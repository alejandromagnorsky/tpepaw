package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.model.User;

public class RegisterForm {

	private String username;
	private String password;
	private String confirmPassword;
	private String fullname;
	private String email;
	
	public RegisterForm(){
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User buildUser(){
		return  new User(false, true, username, password, fullname, email);
	}
}
