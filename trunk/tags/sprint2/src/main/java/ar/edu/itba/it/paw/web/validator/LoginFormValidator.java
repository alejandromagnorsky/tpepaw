package ar.edu.itba.it.paw.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.LoginForm;

@Component
public class LoginFormValidator implements Validator {

	private UserService userService;

	@Autowired
	public LoginFormValidator(UserService userService){
		this.userService = userService;
	}
	
	public boolean supports(Class<?> clazz){
		return LoginForm.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors){
		LoginForm form = (LoginForm)target;
		
		String name = form.getUsername();
		String password = form.getPassword();
		if(!userService.verifyUser(name, password))
			errors.rejectValue("password", "invalid");
	}
}
