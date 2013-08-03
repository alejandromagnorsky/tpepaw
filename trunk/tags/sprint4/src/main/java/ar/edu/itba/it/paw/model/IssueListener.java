package ar.edu.itba.it.paw.model;

public interface IssueListener {

	public void onIssueStateChange(Issue issue, Issue.State state);

	public void onIssueCreate(Issue issue);

}
