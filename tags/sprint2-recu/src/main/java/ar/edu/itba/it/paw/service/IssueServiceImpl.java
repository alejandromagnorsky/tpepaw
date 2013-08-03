package ar.edu.itba.it.paw.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.paw.dao.IssueDAO;
import ar.edu.itba.it.paw.model.Access;
import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;

@Service
public class IssueServiceImpl implements IssueService {

	private IssueDAO issueDAO;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private IssueServiceImpl(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}

	public Issue getIssue(int id) {
		return issueDAO.load(id);
	}

	public boolean canViewIssue(User user, Project project) {
		return project.getIsPublic()
				|| (user != null && (projectService.isMember(user, project) || user
						.getAdmin()));
	}

	public boolean canAddIssue(User user, Project project) {
		return user != null
				&& (projectService.isMember(user, project) || user.getAdmin());
	}

	public boolean canEditIssue(User user, Project project) {
		return user != null
				&& (projectService.isMember(user, project) || user.getAdmin());
	}

	public boolean canViewAssigned(User user, Project project) {
		return user != null
				&& (projectService.isMember(user, project) || user.getAdmin());
	}

	public boolean canMarkIssue(User user, Issue issue, State state) {
		if (user == null
				|| (!projectService.isMember(user, issue.getProject()) && !user
						.getAdmin()))
			return false;
		switch (state) {
		case Open:
			return user != null && issue.getState().equals(Issue.State.Ongoing)
					&& issue.getAssignedUser() != null
					&& issue.getAssignedUser().equals(user);
		case Ongoing:
			return user != null && issue.getState().equals(Issue.State.Open)
					&& issue.getAssignedUser() != null
					&& issue.getAssignedUser().equals(user);
		case Completed:
			return user != null && issue.getState().equals(Issue.State.Ongoing);
		case Closed:
			Project project = issue.getProject();
			return projectService.hasLeaderRights(user, project)
					&& !issue.getState().equals(Issue.State.Closed);
		}
		return false;
	}

	public boolean canAssignIssue(User user, Issue issue) {
		return user != null
				&& (projectService.isMember(user, issue.getProject()) || user
						.getAdmin())
				&& (issue.getAssignedUser() == null || !issue.getAssignedUser()
						.equals(user));
	}

	public void assignUserToIssue(User user, Issue issue) {
		if (!canAssignIssue(user, issue))
			throw new IllegalArgumentException();
		issue.setAssignedUser(user);
		issueDAO.save(issue);
	}

	public void setPriorityAs(User user, Issue issue, Priority priority) {
		issue.setPriority(priority);
		issueDAO.save(issue);
	}

	public void updateIssue(User user, Issue issue) {
		if (!canAddIssue(user, issue.getProject()))
			throw new IllegalArgumentException();
		issueDAO.save(issue);
	}

	public void setResolutionAs(User user, Issue issue, Resolution resolution) {
		if (!canMarkIssue(user, issue, State.Completed))
			throw new IllegalArgumentException();

		issue.setResolution(resolution);
		issue.setState(State.Completed);
		issueDAO.save(issue);
	}

	public void markIssueAs(User user, Issue issue, State state) {
		if (!canMarkIssue(user, issue, state))
			throw new IllegalArgumentException();
		issue.setState(state);
		issueDAO.save(issue);
	}

	public List<Issue> getAssignedIssues(User user, Project project) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> issues = issueDAO.load();

		for (Issue i : issues)
			if (i.getAssignedUser() != null
					&& i.getAssignedUser().equals(user)
					&& (i.getState().equals(Issue.State.Open) || i.getState()
							.equals(Issue.State.Ongoing))
					&& i.getProject().equals(project))
				out.add(i);

		return out;
	}

	public List<Issue> getAssignedIssues(User user) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> issues = issueDAO.load();

		for (Issue i : issues)
			if (i.getAssignedUser() != null
					&& i.getAssignedUser().equals(user)
					&& (i.getState().equals(Issue.State.Open) || i.getState()
							.equals(Issue.State.Ongoing)))
				out.add(i);

		return out;
	}

	public List<Issue> getIssues(Project p) {
		List<Issue> out = new ArrayList<Issue>();
		List<Issue> tmp = issueDAO.load();

		for (Issue i : tmp)
			if (i.getProject().equals(p))
				out.add(i);
		return out;
	}

	public int getProgressPercentage(Issue issue) {
		Time estimatedTime = issue.getEstimatedTime();
		SortedSet<Work> works = getWorks(issue);
		Time dedicatedTime = new Time(0);
		for (Work work : works)
			dedicatedTime.add(work.getDedicatedTime());
		if (estimatedTime == null || estimatedTime.getMinutes() == 0)
			return -1;
		return (int) (((float) dedicatedTime.getMinutes() / estimatedTime
				.getMinutes()) * 100);
	}

	public SortedSet<Comment> getComments(Issue issue) {
		SortedSet<Comment> comments = issueDAO.loadComments();
		SortedSet<Comment> ans = new TreeSet<Comment>();
		for (Comment comment : comments)
			if (comment.getIssue().equals(issue))
				ans.add(comment);
		return ans;
	}

	public Comment getComment(int id) {
		return issueDAO.loadComment(id);
	}

	public void updateComment(User user, Comment comment) {
		if (!canAddComment(user, comment.getIssue()))
			throw new IllegalArgumentException();
		issueDAO.saveComment(comment);
	}

	public boolean canAddComment(User user, Issue issue) {
		return user != null
				&& (projectService.isMember(user, issue.getProject()) || user
						.getAdmin());
	}

	public SortedSet<Work> getWorks(User user) {
		SortedSet<Work> works = issueDAO.loadWorks();
		SortedSet<Work> ans = new TreeSet<Work>();
		for (Work work : works)
			if (work.getUser().equals(user))
				ans.add(work);
		return ans;
	}

	public SortedSet<Work> getWorks(Issue issue) {
		SortedSet<Work> works = issueDAO.loadWorks();
		SortedSet<Work> ans = new TreeSet<Work>();
		for (Work work : works)
			if (work.getIssue().equals(issue))
				ans.add(work);
		return ans;
	}

	public Work getWork(int id) {
		return issueDAO.loadWork(id);
	}

	public void updateWork(User source, Work work) {
		if (!canAddWork(source, work.getIssue()))
			throw new IllegalArgumentException();
		issueDAO.saveWork(work);
	}

	public boolean canAddWork(User user, Issue issue) {
		return user != null
				&& (projectService.isMember(user, issue.getProject()) || user
						.getAdmin());
	}

	public boolean canEditWork(User user, Issue issue) {
		return user != null
				&& (projectService.isMember(user, issue.getProject()) || user
						.getAdmin());
	}

	public boolean filter(Filter filter, Issue issue) {
		if (!projectService.getProject(issue.getProject().getCode()).equals(
				filter.getProject()))
			return false;

		if (!issue.getCode().contains(filter.getIssueCode()))
			return false;

		if (!issue.getTitle().contains(filter.getIssueTitle()))
			return false;

		if (!issue.getDescription().contains(filter.getIssueDescription()))
			return false;

		if (filter.getIssueReportedUser() != null
				&& !filter.getIssueReportedUser().equals(
						issue.getReportedUser()))
			return false;

		if (filter.getIssueAssignedUser() != null
				&& !filter.getIssueAssignedUser().equals(
						issue.getAssignedUser()))
			return false;

		if (filter.getIssueState() != null
				&& !filter.getIssueState().equals(issue.getState()))
			return false;

		if (filter.getIssueResolution() != null
				&& !filter.getIssueResolution().equals(issue.getResolution()))
			return false;

		DateTime creationDate = issue.getCreationDate();
		if (filter.getDateFrom() != null
				&& creationDate.isBefore(filter.getDateFrom()))
			return false;
		if (filter.getDateTo() != null
				&& creationDate.isAfter(filter.getDateTo()))
			return false;

		return true;
	}

	public boolean canViewHottestIssues(User user, Project project) {
		return canViewIssue(user, project);
	}

	public void addAccess(User user, Access access) {
		if (!canViewIssue(user, access.getIssue().getProject()))
			throw new IllegalArgumentException();
		issueDAO.saveAccess(access);
	}

	private List<AccessPerIssue> getOrderedAccessPerIssueList(
			List<Access> rawAccessList, Project project) {
		List<Access> access = rawAccessList;
		List<AccessPerIssue> list = new ArrayList<AccessPerIssue>();
		for (Access a : access) {
			Issue issue = a.getIssue();
			AccessPerIssue accessPerIssue = new AccessPerIssue(issue, null);
			if (!list.contains(accessPerIssue))
				list.add(new AccessPerIssue(issue, 0));
			list.get(list.indexOf(accessPerIssue)).addAccess();
		}
		return list;
	}

	public List<AccessPerIssue> getHottestOfMonthIssues(Project project) {
		List<Issue> issues = new ArrayList<Issue>(getIssues(project));
		List<Access> access = new ArrayList<Access>();
		DateTime today = new DateTime();
		for (Issue issue : issues)
			for (Access a : issueDAO.loadAccess(issue))
				if (a.getDate().getMonthOfYear() == today.getMonthOfYear())
					access.add(a);

		List<AccessPerIssue> list = getOrderedAccessPerIssueList(access,
				project);
		List<AccessPerIssue> ans = new ArrayList<AccessPerIssue>();
		Collections.sort(list);
		for (int i = 0; i < 5 && i < list.size(); i++)
			ans.add(list.get(i));
		return ans;
	}

	public List<AccessPerIssue> getHottestIssues(Project project) {
		List<Issue> issues = new ArrayList<Issue>(getIssues(project));
		List<Access> access = new ArrayList<Access>();
		for (Issue issue : issues)
			access.addAll(issueDAO.loadAccess(issue));

		List<AccessPerIssue> list = getOrderedAccessPerIssueList(access,
				project);
		List<AccessPerIssue> ans = new ArrayList<AccessPerIssue>();
		Collections.sort(list);
		for (int i = 0; i < 5 && i < list.size(); i++)
			ans.add(list.get(i));
		return ans;
	}
	


	public List<AccessPerIssue> getHottestOfWeekIssues(Project project) {
		List<Issue> issues = new ArrayList<Issue>(getIssues(project));
		List<Access> access = new ArrayList<Access>();
		DateTime today = new DateTime();
		for (Issue issue : issues)
			for (Access a : issueDAO.loadAccess(issue))
				if (a.getDate().getWeekOfWeekyear() == today.getWeekOfWeekyear())
					access.add(a);

		List<AccessPerIssue> list = getOrderedAccessPerIssueList(access, project);
		List<AccessPerIssue> ans = new ArrayList<AccessPerIssue>();
		Collections.sort(list);
		for (int i = 0; i < 5 && i < list.size(); i++)
			ans.add(list.get(i));
		return ans;
	}

	public class AccessPerIssue implements Comparable<AccessPerIssue> {

		private Issue issue;
		private Integer accessQuant;

		public AccessPerIssue(Issue issue, Integer accessQuant) {
			super();
			this.issue = issue;
			this.accessQuant = accessQuant;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AccessPerIssue other = (AccessPerIssue) obj;
			if (issue == null) {
				if (other.issue != null)
					return false;
			} else if (!issue.equals(other.issue))
				return false;
			return true;
		}

		public void addAccess() {
			this.accessQuant++;
		}

		public Integer getAccessQuant() {
			return accessQuant;
		}

		public Issue getIssue() {
			return issue;
		}

		public int compareTo(AccessPerIssue o) {
			return o.accessQuant - this.accessQuant;
		}
	}

}
