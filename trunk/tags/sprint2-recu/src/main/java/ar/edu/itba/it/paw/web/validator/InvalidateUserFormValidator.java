package ar.edu.itba.it.paw.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.InvalidateUserForm;

@Component
public class InvalidateUserFormValidator implements Validator {

	private UserService userService;

	@Autowired
	public InvalidateUserFormValidator(UserService userService) {
		this.userService = userService;
	}

	public boolean supports(Class<?> clazz) {
		return InvalidateUserForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		InvalidateUserForm form = (InvalidateUserForm) target;
		
		User user = form.getTarget();
		if (userService.isActive(user))
			errors.rejectValue("target", "active");
		
		if(user.equals(form.getSource()))
			errors.rejectValue("target", "source");
	}
}
