package ar.edu.itba.it.paw.web.validator;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ar.edu.itba.it.paw.model.ValidationUtils;
import ar.edu.itba.it.paw.web.command.VersionForm;

@Component
public class VersionFormValidator {

	public boolean supports(Class<?> clazz) {
		return VersionForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		VersionForm form = (VersionForm) target;

		String name = form.getName();		
		DateTime releaseDate = form.getReleaseDate();
		
		if (!ValidationUtils.validateLength(name, 1, 25))
			errors.rejectValue("name", "required");
		if (releaseDate == null) 
			errors.rejectValue("releaseDate", "invalid");
	}
}
