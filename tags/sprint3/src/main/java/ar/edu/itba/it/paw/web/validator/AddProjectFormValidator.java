package ar.edu.itba.it.paw.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.repo.ProjectRepo;
import ar.edu.itba.it.paw.web.command.ProjectForm;

@Component
public class AddProjectFormValidator implements Validator {

	private ProjectRepo projectRepo;

	@Autowired
	public AddProjectFormValidator(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	public boolean supports(Class<?> clazz) {
		return ProjectForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		ProjectForm form = (ProjectForm) target;

		String name = form.getName();
		String code = form.getCode();

		if (name.isEmpty())
			errors.rejectValue("name", "required");

		if (code.isEmpty())
			errors.rejectValue("code", "required");
		else {
			Project project = projectRepo.get(code);
			if (project != null)
				errors.rejectValue("code", "existing");
		}
	}
}