package ar.edu.itba.it.paw.service;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.Issue.Priority;
import ar.edu.itba.it.paw.Issue.Resolution;
import ar.edu.itba.it.paw.Issue.State;
import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.dao.IssueDAO;
import ar.edu.itba.it.paw.dao.IssueDAO_JDBC;

public class IssueServiceImpl implements IssueService {

	private static IssueServiceImpl instance = new IssueServiceImpl();
	private IssueDAO issueDAO = IssueDAO_JDBC.getInstance();
	private PermissionManager permissionManager = PermissionManager
			.getInstance();

	private IssueServiceImpl() {
	}

	public static IssueServiceImpl getInstance() {
		return instance;
	}

	public Issue getIssue(int id) {
		return issueDAO.load(id);
	}

	public void assignUserToIssue(User user, Issue issue) {
		if (!permissionManager.canAssignIssue(user, issue))
			throw new IllegalArgumentException();
		issue.setAssignedUser(user);
		issueDAO.save(issue);
	}

	public void setPriorityAs(User user, Issue issue, Priority priority) {
		issue.setPriority(priority);
		issueDAO.save(issue);
	}

	public void addIssue(User user, Issue issue) {
		issue.setId(0);
		updateIssue(user, issue);
	}

	public void updateIssue(User user, Issue issue) {
		if (!permissionManager.canAddIssue(user))
			throw new IllegalArgumentException();
		issueDAO.save(issue);
	}

	public void setResolutionAs(User user, Issue issue, Resolution resolution) {
		if (!permissionManager.canResolveIssue(user, issue))
			throw new IllegalArgumentException();

		issue.setResolution(resolution);
		issue.setState(State.Completed);
		issueDAO.save(issue);
	}

	public void markIssueAsClosed(User user, Issue issue) {
		if (!permissionManager.canCloseIssue(user, issue.getProject(), issue))
			throw new IllegalArgumentException();
		issue.setState(State.Closed);
		issueDAO.save(issue);
	}

	public void markIssueAsOpen(User user, Issue issue) {
		if (!permissionManager.canMarkIssueAsOpen(user, issue))
			throw new IllegalArgumentException();
		issue.setState(State.Open);
		issueDAO.save(issue);
	}

	public void markIssueAsOngoing(User user, Issue issue) {
		if (!permissionManager.canMarkIssueAsOngoing(user, issue))
			throw new IllegalArgumentException();
		issue.setState(State.Ongoing);
		issueDAO.save(issue);
	}

	public List<Issue> getAssignedIssues(User user, Project project) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> issues = issueDAO.load();

		for (Issue i : issues)
			if (i.getAssignedUser() != null && i.getAssignedUser().getName().equals(user.getName())
					&& (i.getState().equals(Issue.State.Open) || i.getState()
							.equals(Issue.State.Ongoing))
					&& i.getProject().getCode().equals(project.getCode()))
				out.add(i);

		return out;
	}

	public List<Issue> getAssignedIssues(User user) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> issues = issueDAO.load();

		for (Issue i : issues)
			if (i.getAssignedUser() != null && i.getAssignedUser().getName().equals(user.getName())
					&& (i.getState().equals(Issue.State.Open) || i.getState()
							.equals(Issue.State.Ongoing)))
				out.add(i);

		return out;
	}

	public List<Issue> getIssues(Project p) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> tmp = issueDAO.load();

		for (Issue i : tmp)
			if (i.getProject().getCode().equals(p.getCode()))
				out.add(i);
		return out;
	}

	public int[] getQuantPerState(Project p) {
		int stateQuant[] = { 0, 0, 0, 0 };
		List<Issue> issues = getIssues(p);
		int state = 0;
		for (Issue issue : issues) {
			switch (issue.getState()) {
			case Open:
				state = 0;
				break;
			case Ongoing:
				state = 1;
				break;
			case Completed:
				state = 2;
				break;
			case Closed:
				state = 3;
				break;
			}
			stateQuant[state]++;
		}
		return stateQuant;
	}

	public float[] getHoursPerState(Project p) {
		float stateHours[] = { 0, 0, 0, 0 };
		List<Issue> issues = getIssues(p);
		int state = 0;
		for (Issue issue : issues) {
			switch (issue.getState()) {
			case Open:
				state = 0;
				break;
			case Ongoing:
				state = 1;
				break;
			case Completed:
				state = 2;
				break;
			case Closed:
				state = 3;
				break;
			}
			stateHours[state] += issue.getEstimatedTime();
		}
		return stateHours;
	}
}
