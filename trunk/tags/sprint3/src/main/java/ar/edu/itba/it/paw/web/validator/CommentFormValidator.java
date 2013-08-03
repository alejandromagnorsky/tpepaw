package ar.edu.itba.it.paw.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.web.command.CommentForm;

@Component
public class CommentFormValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return CommentForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		CommentForm form = (CommentForm) target;
		
		String description = form.getDescription();
		
		if(description.isEmpty())
			errors.rejectValue("description", "required");
	}
}