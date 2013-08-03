package ar.edu.itba.it.paw.web.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;

@SuppressWarnings("serial")
public class ProjectConverter implements IConverter {

	private ProjectRepo projectRepo;

	public ProjectConverter(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	public Object convertToObject(String value, Locale locale) {
		return projectRepo.get(value);
	}

	public String convertToString(Object value, Locale locale) {
		return ((Project) value).getCode();
	}
}
