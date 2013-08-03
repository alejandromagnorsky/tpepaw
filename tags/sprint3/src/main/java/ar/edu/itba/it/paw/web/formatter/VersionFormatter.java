package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.repo.ProjectRepo;

@Component
public class VersionFormatter implements Formatter<Version> {

	private ProjectRepo projectRepo;

	@Autowired
	public VersionFormatter(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	public String print(Version version, Locale l) {
		return String.valueOf(version.getId());
	}

	public Version parse(String id, Locale l) throws ParseException {
		return projectRepo.getVersion(Integer.valueOf(id));
	}

}
