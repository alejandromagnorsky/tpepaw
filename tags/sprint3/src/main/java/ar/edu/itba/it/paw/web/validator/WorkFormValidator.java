package ar.edu.itba.it.paw.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.web.command.WorkForm;

@Component
public class WorkFormValidator implements Validator{

	public boolean supports(Class<?> clazz) {
		return WorkForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		WorkForm form = (WorkForm) target;

		String description = form.getDescription();
		Time dedicatedTime = form.getDedicatedTime();

		if (description.isEmpty())
			errors.rejectValue("description", "requiredWork");

		
		if (!errors.hasFieldErrors("dedicatedTime") && dedicatedTime == null)
			errors.rejectValue("dedicatedTime", "required");
	}

}
