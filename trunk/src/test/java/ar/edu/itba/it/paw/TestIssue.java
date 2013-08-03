package ar.edu.itba.it.paw;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueLog;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

public class TestIssue {

	private Issue issue;
	private Project project;
	private User user;
	private User leader;

	private Version beta;
	private Version alpha;
	private Version rc;

	@Before
	public void initialize() {
		leader = new User(false, true, "Lider", "Lider",
				"Nanananananana LIDER!", "lider@lider.com");
		user = new User(false, true, "Usuario", "Usuario", "Usuario Oirausu",
				"usuario@oirausu.com");
		project = new Project("test", "Proyecto de prueba",
				"Solo un proyecto mas", leader, false, new SimpleListener());
		issue = new Issue(project, "Probando Issue", "Probando esta tarea",
				new DateTime(), new Time(120), null, user, Issue.State.Open,
				Issue.Priority.High, Issue.Type.Error, new SimpleListener());

		alpha = new Version("0.1 Alpha", "Version alpha", new DateTime());
		beta = new Version("0.5 Beta", "Version beta", new DateTime());
		rc = new Version("1.0 RC", "Release Candidate", new DateTime());
	}
	
	
	@Test
	public void TestVersionPercentage(){
		project.addVersion(beta);
		assertEquals(project.getVersions().contains(beta), true);
		
		project.addUser(leader, user);
		assertEquals(project.getUsers().contains(user),true);

		beta.addFixedIssue(user, issue);
		assertEquals(issue.getFixedVersions().contains(beta), true);
		assertEquals(beta.getFixedIssues().contains(issue), true);

		issue.setAssignedUser(user, user);
		assertEquals(issue.getAssignedUser(), user);

		assertEquals(issue.canMark(user, Issue.State.Ongoing), true);
		issue.mark(user, Issue.State.Ongoing);
		assertEquals(issue.getState(), Issue.State.Ongoing);
		
		assertEquals(beta.getProgressPercentage() == 0, true);

		assertEquals(issue.canMark(user, Issue.State.Completed), true);
		issue.mark(user, Issue.State.Completed);
		assertEquals(issue.getState(), Issue.State.Completed);
		
		assertEquals(beta.getProgressPercentage() == 100, true);
	}

	@Test
	public void TestAffectedVersions() {
		project.addVersion(beta);
		project.addVersion(alpha);
		assertEquals(project.getVersions().contains(beta), true);
		assertEquals(project.getVersions().contains(alpha), true);

		beta.addAffectingIssue(user, issue);
		assertEquals(issue.getAffectedVersions().contains(beta), true);
		assertEquals(beta.getAffectingIssues().contains(issue), true);

		beta.addAffectingIssue(user, issue);
		assertEquals(issue.getAffectedVersions().size() == 1, true);

		alpha.addAffectingIssue(user, issue);
		assertEquals(issue.getAffectedVersions().contains(alpha), true);
		assertEquals(alpha.getAffectingIssues().contains(issue), true);

		project.addVersion(rc);
		assertEquals(project.getVersions().contains(rc), true);

		alpha.removeAffectingIssue(user,issue);
		assertEquals(issue.getAffectedVersions().contains(alpha), false);
		assertEquals(alpha.getAffectingIssues().contains(issue), false);

		beta.removeAffectingIssue(user,issue);
		assertEquals(issue.getAffectedVersions().contains(beta), false);
		assertEquals(beta.getAffectingIssues().contains(issue), false);
		
	}

	@Test
	public void TestFixedVersions() {
		project.addVersion(beta);
		project.addVersion(alpha);
		assertEquals(project.getVersions().contains(beta), true);
		assertEquals(project.getVersions().contains(alpha), true);

		beta.addFixedIssue(user, issue);
		assertEquals(issue.getFixedVersions().contains(beta), true);
		assertEquals(beta.getFixedIssues().contains(issue), true);

		beta.addFixedIssue(user, issue);
		assertEquals(issue.getFixedVersions().size() == 1, true);

		alpha.addFixedIssue(user, issue);
		assertEquals(issue.getFixedVersions().contains(alpha), true);
		assertEquals(alpha.getFixedIssues().contains(issue), true);

		project.addVersion(rc);
		assertEquals(project.getVersions().contains(rc), true);

		alpha.removeFixedIssue(user,issue);
		assertEquals(issue.getFixedVersions().contains(alpha), false);
		assertEquals(alpha.getFixedIssues().contains(issue), false);

		beta.removeFixedIssue(user,issue);
		assertEquals(issue.getFixedVersions().contains(beta), false);
		assertEquals(beta.getFixedIssues().contains(issue), false);
	}


	@Test
	public void TestIssueLog() {
		project.addUser(leader, user);
		issue.setAssignedUser(user, user);
		assertEquals(issue.getIssueLogs().size() == 1, true);
		assertEquals(issue.getIssueLogs().get(0).getType(),
				IssueLog.ChangeType.Assigned);

		issue.setEstimatedTime(user, new Time(60));
		assertEquals(issue.getIssueLogs().size() == 2, true);
		assertEquals(issue.getIssueLogs().get(1).getType(),
				IssueLog.ChangeType.Estimated);

		issue.setDescription(user, "Buen dia!");
		assertEquals(issue.getIssueLogs().size() == 3, true);
		assertEquals(issue.getIssueLogs().get(2).getType(),
				IssueLog.ChangeType.Description);

		issue.setTitle(user, "Corregir ortographia");
		assertEquals(issue.getIssueLogs().size() == 4, true);
		assertEquals(issue.getIssueLogs().get(3).getType(),
				IssueLog.ChangeType.Title);

		// In this case it fails because previous is already an Error
		issue.setType(user, Issue.Type.Error);
		assertEquals(issue.getIssueLogs().size() == 4, true);

		issue.setType(user, Issue.Type.Improvement);
		assertEquals(issue.getIssueLogs().size() == 5, true);
		assertEquals(issue.getIssueLogs().get(4).getType(),
				IssueLog.ChangeType.Type);

		issue.setPriority(user, Issue.Priority.Critical);
		assertEquals(issue.getIssueLogs().size() == 6, true);
		assertEquals(issue.getIssueLogs().get(5).getType(),
				IssueLog.ChangeType.Priority);
	}

	@Test
	public void TestIssueType() {

		issue.setType(user, Issue.Type.Error);
		assertEquals(issue.getType(), Issue.Type.Error);

		try {
			issue.setType(user, null);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getType(), Issue.Type.Error);
	}

	@Test
	public void TestIssuePriority() {

		issue.setPriority(user, Issue.Priority.High);
		assertEquals(issue.getPriority(), Issue.Priority.High);

		try {
			issue.setPriority(user, null);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getPriority(), Issue.Priority.High);
	}

	@Test
	public void TestIssueStates() {

		try {
			assertEquals(issue.canMark(user, Issue.State.Ongoing), false);
			issue.mark(user, Issue.State.Ongoing);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Open);

		try {
			assertEquals(issue.canMark(user, Issue.State.Closed), false);
			issue.mark(user, Issue.State.Closed);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Open);

		project.addUser(leader, user);
		assertEquals(project.getUsers().contains(user), true);

		assertEquals(issue.canAssignIssue(user), true);
		issue.setAssignedUser(user, user);
		assertEquals(issue.getAssignedUser(), user);

		try {
			assertEquals(issue.canMark(user, Issue.State.Completed), false);
			issue.mark(user, Issue.State.Completed);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Open);

		try {
			assertEquals(issue.canMark(user, Issue.State.Closed), false);
			issue.mark(user, Issue.State.Closed);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Open);

		try {
			assertEquals(issue.canMark(user, Issue.State.Completed), false);
			issue.setResolution(user, Issue.Resolution.Irreproducible);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Open);
		assertEquals(issue.getResolution(), null);

		assertEquals(issue.canMark(user, Issue.State.Ongoing), true);
		issue.mark(user, Issue.State.Ongoing);
		assertEquals(issue.getState(), Issue.State.Ongoing);

		assertEquals(issue.canMark(user, Issue.State.Completed), true);
		issue.setResolution(user, Issue.Resolution.Resolved);
		assertEquals(issue.getState(), Issue.State.Completed);
		assertEquals(issue.getResolution(), Issue.Resolution.Resolved);

		try {
			assertEquals(issue.canMark(user, Issue.State.Ongoing), false);
			issue.mark(user, Issue.State.Ongoing);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Completed);

		try {
			assertEquals(issue.canMark(user, Issue.State.Open), false);
			issue.mark(user, Issue.State.Open);
		} catch (IllegalArgumentException e) {
		}
		assertEquals(issue.getState(), Issue.State.Completed);

		assertEquals(issue.canMark(leader, Issue.State.Closed), true);
		issue.mark(leader, Issue.State.Closed);
		assertEquals(issue.getState(), Issue.State.Closed);
	}
}
