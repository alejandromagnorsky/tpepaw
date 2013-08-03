package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

public class FilterForm {
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getIssueCode() {
		return issueCode;
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public User getIssueReportedUser() {
		return issueReportedUser;
	}

	public void setIssueReportedUser(User issueReportedUser) {
		this.issueReportedUser = issueReportedUser;
	}

	public User getIssueAssignedUser() {
		return issueAssignedUser;
	}

	public void setIssueAssignedUser(User issueAssignedUser) {
		this.issueAssignedUser = issueAssignedUser;
	}

	public State getIssueState() {
		return issueState;
	}

	public void setIssueState(State issueState) {
		this.issueState = issueState;
	}

	public Resolution getIssueResolution() {
		return issueResolution;
	}

	public void setIssueResolution(Resolution issueResolution) {
		this.issueResolution = issueResolution;
	}

	public DateTime getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(DateTime dateFrom) {
		this.dateFrom = dateFrom;
	}

	public DateTime getDateTo() {
		return dateTo;
	}

	public void setDateTo(DateTime dateTo) {
		this.dateTo = dateTo;
	}

	public Filter build(){
		return new Filter(	this.user,				this.project,			this.issueCode,
							this.issueTitle,		this.issueDescription,	this.issueReportedUser,
							this.issueAssignedUser,	this.issueState,		this.issueResolution,
							this.dateFrom,			this.dateTo);
	}
}
