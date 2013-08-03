package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.repo.ProjectRepo;

@Component
public class ProjectFormatter implements Formatter<Project> {
	private ProjectRepo projectRepo;

	@Autowired
	public ProjectFormatter(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	public String print(Project project, Locale l) {
		return project.getCode();
	}

	public Project parse(String code, Locale l) throws ParseException {
		return projectRepo.get(code);
	}
}
