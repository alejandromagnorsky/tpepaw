package ar.edu.itba.it.paw.model;

import java.util.List;
import java.util.SortedSet;

import org.joda.time.DateTime;

public interface IssueRepo {

	public List<Issue> get();

	public Issue get(int id);

	public List<Issue> get(Project project);

	public void add(Issue i);

	public Comment getComment(int id);

	public List<Work> getWorks();

	public List<Work> getWorks(User user);

	public Work getWork(int id);

	public IssueFile getIssueFile(int id);

	public SortedSet<User> getCollaboratorCandidates(Issue issue);

	public Time getWorkByUser(DateTime from, DateTime to, Project project,
			User user);

	public void persist(Object object);
}
