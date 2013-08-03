package ar.edu.itba.it.paw.dao;

import java.util.List;
import java.util.SortedSet;

import ar.edu.itba.it.paw.model.Access;
import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Work;

public interface IssueDAO {

	public List<Issue> load();
	
	public Issue load(int id);

	public void save(Issue p);

	public SortedSet<Work> loadWorks();

	public Work loadWork(int id);

	public void saveWork(Work w);
	
	public SortedSet<Comment> loadComments();

	public Comment loadComment(int id);

	public void saveComment(Comment w);
	
	public List<Access> loadAccess(Issue issue);
	
	public List<Access> loadAccess();
	
	public void saveAccess(Access access);
}
