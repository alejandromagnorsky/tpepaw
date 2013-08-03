package ar.edu.itba.it.paw.web.command;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

public class IssueForm {
	private Issue originalIssue;
	private String title;
	private String description;
	private User assignedUser;
	private Time estimatedTime;
	private Priority priority;
	private Type type;
	private Project project;
	
	private List<Version> affectedVersions;
	private List<Version> fixedVersions;

	public Issue getOriginalIssue() {
		return originalIssue;
	}

	public void setOriginalIssue(Issue originalIssue) {
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setAffectedVersions(List<Version> affectedVersions) {
		this.affectedVersions = affectedVersions;
	}

	public List<Version> getAffectedVersions() {
		return affectedVersions;
	}

	public void setFixedVersions(List<Version> fixedVersions) {
		this.fixedVersions = fixedVersions;
	}

	public List<Version> getFixedVersions() {
		return fixedVersions;
	}

	public Issue build(State state, DateTime creationDate, User reportedUser) {
		return new Issue(this.project, this.title, this.description,
				creationDate, this.estimatedTime, this.assignedUser,
				reportedUser, state, this.priority, this.type);
	}

	public void update(User source) {
		this.originalIssue.setTitle(source, this.title);
		this.originalIssue.setDescription(source, this.description);
		this.originalIssue.setEstimatedTime(source, this.estimatedTime);
		this.originalIssue.setAssignedUser(source, this.assignedUser);
		this.originalIssue.setPriority(source, this.priority);
		this.originalIssue.setType(source, this.type);

		List<Version> affected = getAffectedVersions();
		if (affected == null)
			affected = new ArrayList<Version>();

		List<Version> originalAffected = new ArrayList<Version>(originalIssue
				.getAffectedVersions());

		// Get versions to delete
		originalAffected.removeAll(affected);
		for (Version v : originalAffected)
			v.removeAffectingIssue(source, originalIssue);

		// Add all versions
		for (Version v : affected)
			v.addAffectingIssue(source, originalIssue);

		List<Version> fixed = getFixedVersions();
		if (fixed == null)
			fixed = new ArrayList<Version>();

		List<Version> originalFixed = new ArrayList<Version>(originalIssue
				.getFixedVersions());

		// Get versions to delete
		originalFixed.removeAll(fixed);

		for (Version v : originalFixed)
			v.removeFixedIssue(source, originalIssue);

		// Add all versions
		for (Version v : fixed)
			v.addFixedIssue(source, originalIssue);
	}
}
