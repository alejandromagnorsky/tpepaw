package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.repo.IssueRepo;
import ar.edu.itba.it.paw.repo.ProjectRepo;

@Component
public class IssueFormatter implements Formatter<Issue> {

	private IssueRepo issueRepo;
	private ProjectRepo projectRepo;
	
	@Autowired
	public IssueFormatter(IssueRepo issueRepo, ProjectRepo projectRepo) {
		this.issueRepo = issueRepo;
		this.projectRepo = projectRepo;
	}

	public String print(Issue issue, Locale l) {
		return issue.getCode();
	}

	public Issue parse(String code, Locale l) throws ParseException {
		String projectCode = code.substring(0, code.lastIndexOf('-'));
		int issueId = Integer.valueOf(code.substring(code.lastIndexOf('-')+1));
		Issue issue = issueRepo.get(issueId);
		Project project = projectRepo.get(projectCode);
		if(issue.getProject().equals(project))
			return issue;
		return null;		
	}
}
