package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.repo.ProjectRepo;

@Component
public class FilterFormatter implements Formatter<Filter> {

	private ProjectRepo projectRepo;

	@Autowired
	public FilterFormatter(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	public String print(Filter filter, Locale l) {
		return filter.getId() + "";
	}

	public Filter parse(String id, Locale l) throws ParseException {
		return projectRepo.getFilter(Integer.valueOf(id));
	}

}