package ar.edu.itba.it.paw.dao;

import java.util.List;

import ar.edu.itba.it.paw.Issue;

public interface IssueDAO {

	public List<Issue> load();
	
	public Issue load(int id);

	public void save(Issue p);

}
