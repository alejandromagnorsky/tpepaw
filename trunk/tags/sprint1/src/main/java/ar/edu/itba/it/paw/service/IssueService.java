package ar.edu.itba.it.paw.service;

import java.util.List;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.Issue.Priority;
import ar.edu.itba.it.paw.Issue.Resolution;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;

public interface IssueService {

	public Issue getIssue(int id);

	public void addIssue(User user, Issue issue);
	
	public void updateIssue(User user, Issue issue);
	
	public void assignUserToIssue(User user, Issue issue);

	public void setPriorityAs(User user, Issue issue, Priority priority);

	public void setResolutionAs(User user, Issue issue, Resolution resolution);

	public void markIssueAsClosed(User user, Issue issue);

	public void markIssueAsOpen(User user, Issue issue);

	public void markIssueAsOngoing(User user, Issue issue);

	public List<Issue> getAssignedIssues(User user, Project project);

	public List<Issue> getAssignedIssues(User user);

	public List<Issue> getIssues(Project p);

	public int[] getQuantPerState(Project p);

	public float[] getHoursPerState(Project p);

}
