package ar.edu.itba.it.paw.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.RegisterForm;

@Component
public class RegisterFormValidator implements Validator{

	private UserService userService;

	@Autowired
	public RegisterFormValidator(UserService userService){
		this.userService = userService;
	}
	
	public boolean supports(Class<?> clazz){
		return RegisterForm.class.equals(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		RegisterForm form = (RegisterForm) target;

		String name = form.getUsername();
		String password = form.getPassword();
		String confirmPassword = form.getConfirmPassword();
		String fullname = form.getFullname();

		if (name.length() == 0)
			errors.rejectValue("username", "required");
		else if (userService.getUser(name) != null)
			errors.rejectValue("username", "exists");

		if (password.length() == 0)
			errors.rejectValue("password", "required");

		if (!password.equals(confirmPassword))
			errors.rejectValue("confirmPassword", "invalid");

		if (fullname.length() == 0)
			errors.rejectValue("fullname", "required");

	}
}
