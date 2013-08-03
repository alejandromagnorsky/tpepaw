package ar.edu.itba.it.paw.web.validator;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.WorkReportForm;

@Component
public class WorkReportFormValidator implements Validator {
	
	UserService userService;

	@Autowired
	public WorkReportFormValidator(UserService userService) {
		this.userService = userService;		
	}

	public boolean supports(Class<?> clazz) {
		return WorkReportForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		WorkReportForm form = (WorkReportForm) target;
		Project project = form.getProject();

		try {
			DateTime from = form.getFrom();
			DateTime to = form.getTo();

			if (from == null || to == null || !to.isAfter(from.toInstant()))
				errors.rejectValue("project", "reportDates");
			
		} catch (Exception e) {
			errors.rejectValue("project", "reportDates");
		}

		if (project == null)
			errors.rejectValue("project", "notExists");
	}
}
