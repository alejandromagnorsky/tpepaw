package ar.edu.itba.it.paw.model;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;

public class Filter extends AbstractModel {
	private User user;
	private Project project;
	private String issueCode;
	private String issueTitle;
	private String issueDescription;
	private User issueReportedUser;
	private User issueAssignedUser;
	private State issueState;
	private Resolution issueResolution;
	@DateTimeFormat(pattern="dd/MM/yyyy") private DateTime dateFrom;
	@DateTimeFormat(pattern="dd/MM/yyyy") private DateTime dateTo;
	
	public Filter(User user, Project project, String issueCode, String issueTitle,
			String issueDescription, User issueReportedUser,
			User issueAssignedUser, State issueState,
			Resolution issueResolution, DateTime dateFrom,
			DateTime dateTo) {
		
		if (project == null	|| !ValidationUtils.validateMaxLength(issueTitle, 25)
				|| !ValidationUtils.validateMaxLength(issueCode, 25) 
				|| !ValidationUtils.validateMaxLength(issueDescription, 255) )
			throw new IllegalArgumentException();
		
		this.user = user;
		this.project = project;
		this.issueCode = issueCode;
		this.issueTitle = issueTitle;
		this.issueDescription = issueDescription;
		this.issueReportedUser = issueReportedUser;
		this.issueAssignedUser = issueAssignedUser;
		this.issueState = issueState;
		this.issueResolution = issueResolution;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public User getUser(){
		return user;
	}
	
	public Project getProject() {
		return project;
	}

	public String getIssueCode() {
		return issueCode;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public User getIssueReportedUser() {
		return issueReportedUser;
	}

	public User getIssueAssignedUser() {
		return issueAssignedUser;
	}

	public State getIssueState() {
		return issueState;
	}

	public Resolution getIssueResolution() {
		return issueResolution;
	}

	public DateTime getDateFrom() {
		return dateFrom;
	}

	public DateTime getDateTo() {
		return dateTo;
	}
}
