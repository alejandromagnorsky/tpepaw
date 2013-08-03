package ar.edu.itba.it.paw.service;

import java.util.List;
import java.util.SortedSet;

import ar.edu.itba.it.paw.model.Access;
import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.service.IssueServiceImpl.AccessPerIssue;

public interface IssueService {
	
	public Issue getIssue(int id);

	public void updateIssue(User user, Issue issue);

	public void assignUserToIssue(User user, Issue issue);

	public void setPriorityAs(User user, Issue issue, Priority priority);

	public void setResolutionAs(User user, Issue issue, Resolution resolution);
	
	public void markIssueAs(User user, Issue issue, State state);

	public List<Issue> getAssignedIssues(User user, Project project);

	public List<Issue> getAssignedIssues(User user);

	public List<Issue> getIssues(Project p);
	
	public int getProgressPercentage(Issue issue);
	
	public boolean canViewIssue(User user, Project project);
	
	public boolean canViewAssigned(User user, Project project);
	
	public boolean canAddIssue(User user, Project project);

	public boolean canEditIssue(User user, Project project);

	public boolean canMarkIssue(User user, Issue issue, State state);

	public boolean canAssignIssue(User user, Issue issue);
	
	public SortedSet<Comment> getComments(Issue issue);

	public Comment getComment(int id);

	public void updateComment(User user, Comment comment);

	public boolean canAddComment(User user, Issue issue);
	
	public SortedSet<Work> getWorks(User user);

	public SortedSet<Work> getWorks(Issue issue);

	public Work getWork(int id);
	
	public void updateWork(User source, Work work);

	public boolean canAddWork(User user, Issue issue);

	public boolean canEditWork(User user, Issue issue);
	
	public boolean filter(Filter filter, Issue issue);
	
	public boolean canViewHottestIssues(User user, Project project);
	
	public void addAccess(User user, Access access);
	
	public List<AccessPerIssue> getHottestIssues(Project project);

	public List<AccessPerIssue> getHottestOfMonthIssues(Project project);
	
	public List<AccessPerIssue> getHottestOfWeekIssues(Project project);
}
