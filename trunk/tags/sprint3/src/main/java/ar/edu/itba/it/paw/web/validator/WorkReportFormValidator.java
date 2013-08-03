package ar.edu.itba.it.paw.web.validator;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.command.WorkReportForm;

@Component
public class WorkReportFormValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return WorkReportForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		WorkReportForm form = (WorkReportForm) target;
		Project project = form.getProject();

		DateTime fromDate = form.getFrom();
		DateTime toDate = form.getTo();

		if (fromDate == null || toDate == null || !toDate.isAfter(fromDate.toInstant()))
			errors.rejectValue("project", "reportDates");

		if (project == null)
			errors.rejectValue("project", "notExists");
	}
}
