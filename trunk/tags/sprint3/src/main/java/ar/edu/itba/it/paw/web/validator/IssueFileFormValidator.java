package ar.edu.itba.it.paw.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.web.command.IssueFileForm;

@Component
public class IssueFileFormValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return IssueFileForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		IssueFileForm form = (IssueFileForm) target;

		// TODO en ubuntu no se puede, pero en windows puedo meter desde el browse un archivo que no existe.
		
		if (form.getFile().getSize() == 0)
			errors.rejectValue("file", "empty");
		
	}

}
