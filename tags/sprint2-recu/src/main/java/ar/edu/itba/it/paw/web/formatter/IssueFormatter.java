package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.service.IssueService;
import ar.edu.itba.it.paw.service.ProjectService;

@Component
public class IssueFormatter implements Formatter<Issue> {
	
	private IssueService issueService;
	private ProjectService projectService;
	
	@Autowired
	public IssueFormatter(IssueService issueService, ProjectService projectService) {
		this.issueService = issueService;
		this.projectService = projectService;
	}

	public String print(Issue issue, Locale l) {
		return issue.getCode();
	}

	public Issue parse(String code, Locale l) throws ParseException {		
		String projectCode = code.substring(0, code.lastIndexOf('-'));
		int issueId = Integer.valueOf(code.substring(code.lastIndexOf('-')+1));
		Issue issue = issueService.getIssue(issueId);
		Project project = projectService.getProject(projectCode);
		if(issue.getProject().equals(project))
			return issue;
		return null;		
	}
}
