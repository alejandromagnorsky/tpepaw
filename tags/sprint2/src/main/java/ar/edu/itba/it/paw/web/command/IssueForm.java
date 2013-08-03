package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Issue.Priority;

public class IssueForm {
	private Issue originalIssue;
	private String title;
	private String description;
	private User assignedUser;
	private Time estimatedTime;
	private Priority priority;
	private Project project;
	
	
	public Issue getOriginalIssue(){
		return originalIssue;
	}
	
	public void setOriginalIssue(Issue originalIssue){
		this.originalIssue = originalIssue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Time getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Time estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	
	public Issue build(Issue.State state, DateTime date, User reportedUser){
		return new Issue(	this.project,	this.title,			this.description,
							date,	this.estimatedTime,	this.assignedUser,
							reportedUser, state, this.priority);
	}
	
	public void update(){
		this.originalIssue.setTitle(this.title);
		this.originalIssue.setDescription(this.description);
		this.originalIssue.setEstimatedTime(this.estimatedTime);
		this.originalIssue.setAssignedUser(this.assignedUser);
		this.originalIssue.setPriority(this.priority);
	}
}
