package ar.edu.itba.it.paw.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ar.edu.itba.it.paw.web.command.IssueForm;

@Component
public class IssueFormValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return IssueForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		IssueForm form = (IssueForm) target;
		
		String name = form.getTitle();
		
		if(name.isEmpty())
			errors.rejectValue("title", "required");
	}
}
