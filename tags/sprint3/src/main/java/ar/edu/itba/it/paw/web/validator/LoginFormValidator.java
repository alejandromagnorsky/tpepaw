package ar.edu.itba.it.paw.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.repo.UserRepo;
import ar.edu.itba.it.paw.web.command.LoginForm;

@Component
public class LoginFormValidator implements Validator {

	private UserRepo userRepo;

	@Autowired
	public LoginFormValidator(UserRepo userRepo){
		this.userRepo = userRepo;
	}
	
	public boolean supports(Class<?> clazz){
		return LoginForm.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors){
		LoginForm form = (LoginForm)target;
		
		String name = form.getUsername();
		String password = form.getPassword();
		
		User user = userRepo.get(name);
		if (user == null || !user.verifyPassword(password) || !user.getValid())
			errors.rejectValue("password", "invalid");
	}
}
