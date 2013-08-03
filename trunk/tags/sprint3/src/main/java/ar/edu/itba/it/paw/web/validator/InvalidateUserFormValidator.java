package ar.edu.itba.it.paw.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.command.InvalidateUserForm;

@Component
public class InvalidateUserFormValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return InvalidateUserForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		InvalidateUserForm form = (InvalidateUserForm) target;
		
		User user = form.getTarget();
		if (user.isActive())
			errors.rejectValue("target", "active");
		
		if(user.equals(form.getSource()))
			errors.rejectValue("target", "source");
	}
}
