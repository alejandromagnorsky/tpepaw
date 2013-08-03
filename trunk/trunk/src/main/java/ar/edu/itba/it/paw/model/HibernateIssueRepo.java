package ar.edu.itba.it.paw.model;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateIssueRepo extends GenericHibernateRepo implements
		IssueRepo {

	@Autowired
	private HibernateIssueRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<Issue> get() {
		return find("FROM Issue");
	}

	public List<Issue> get(Project project) {
		return find("FROM Issue WHERE projectid = " + project.getId());
	}

	public Issue get(int id) {
		return get(Issue.class, id);
	}

	public void add(Issue i) {
		set(i);
	}

	public Comment getComment(int id) {
		return get(Comment.class, id);
	}

	public List<Work> getWorks() {
		return find("FROM Work");
	}

	public Work getWork(int id) {
		return get(Work.class, id);
	}

	public List<Work> getWorks(User user) {
		return find("FROM Work WHERE userid = " + user.getId());
	}

	public SortedSet<User> getCollaboratorCandidates(Issue issue) {
		SortedSet<User> ans = new TreeSet<User>();
		ans.addAll(issue.getProject().getUsers());
		ans.add(issue.getProject().getLeader());
		if (issue.getAssignedUser() != null)
			ans.remove(issue.getAssignedUser());
		if (issue.getCollaborators() != null)
			ans.removeAll(issue.getCollaborators());
		return ans;
	}

	public Time getWorkByUser(DateTime from, DateTime to, Project project,
			User user) {

		Time totalTime = new Time(0);

		for (Work work : getWorks(user)) {
			if (work.getIssue().getProject().equals(project)
					&& work.getDate().isAfter(from.toInstant())
					&& work.getDate().isBefore(to.toInstant()))
				totalTime.add(work.getDedicatedTime());
		}

		return totalTime;
	}

	public IssueFile getIssueFile(int id) {
		return get(IssueFile.class, id);
	}
}
