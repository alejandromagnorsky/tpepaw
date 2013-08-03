package ar.edu.itba.it.paw.repo;

import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;

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

	public SortedMap<User, Time> getWorkReport(DateTime from, DateTime to,
			Project project);
	
	public void persist(Object object);
}
