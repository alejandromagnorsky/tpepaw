package ar.edu.itba.it.paw;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;

public class TestUser {

	private User kking;
	private User jhanneman;
	private User taraya;
	private Project project;
	private Issue issue;

	@Before
	public void initialize() {
		jhanneman = new User(true, true, "jhanneman", "slowdeath", "Jeff Hanneman", "jhanneman@joinus.com");
		kking = new User(false, true, "kking", "immensedecay", "Kerry King", "kking@joinus.com");
		taraya = new User(false, true, "taraya", "meaningofpain", "Tom Araya", "taraya@joinus.com");
		
		project = new Project("1986", "Reign In Blood", "Def Jam", jhanneman, false, new SimpleListener());
		issue = new Issue(project, "Angel Of Death", "1", new DateTime(), new Time(451), null, jhanneman, Issue.State.Open, Issue.Priority.Critical, Issue.Type.NewFeature, new SimpleListener());
	}

	@Test
	public void passwordTest() {
		assertEquals(jhanneman.verifyPassword("slowdeath"), true);
		assertEquals(kking.verifyPassword("immensedecay"), true);
		assertEquals(taraya.verifyPassword("meaningofpain"), true);

		assertEquals(jhanneman.verifyPassword("Justin Bieber"), false);
		assertEquals(kking.verifyPassword("Justin Bieber"), false);
		assertEquals(taraya.verifyPassword("Justin Bieber"), false);
	}

	@Test
	public void addAndRemoveProjectTest() {
		jhanneman.addProject(project);
		assertEquals(jhanneman.getProjects().size() == 1, true);
		kking.addProject(project);
		assertEquals(kking.getProjects().size() == 1, true);
		taraya.addProject(project);
		assertEquals(taraya.getProjects().size() == 1, true);

		jhanneman.removeProject(project);
		assertEquals(jhanneman.getProjects().size() == 0, true);
		kking.removeProject(project);
		assertEquals(kking.getProjects().size() == 0, true);
		taraya.removeProject(project);
		assertEquals(taraya.getProjects().size() == 0, true);
	}
	
	@Test
	public void isAdminTest(){
		assertEquals(jhanneman.getAdmin(), true);
		assertEquals(kking.getAdmin(), false);
		assertEquals(taraya.getAdmin(), false);
	}

	@Test
	public void canAddProjectTest() {
		isAdminTest();
	}

	@Test
	public void canEditProjectTest() {
		isAdminTest();
	}

	@Test
	public void canRegisterUserTest() {
		isAdminTest();
	}

	@Test
	public void canInvalidateUserTest() {
		assertEquals(jhanneman.getAdmin() && !kking.isActive() && kking.getValid(), true);
		kking.invalidate(jhanneman);
		assertEquals(kking.getAdmin(), false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cantInvalidateTest1() {
		jhanneman.invalidate(taraya);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cantInvalidateTest2() {
		taraya.addIssue(issue);
		assertEquals(taraya.isActive() && taraya.getValid(), true);
		jhanneman.invalidate(taraya);
	}

	@Test
	public void issueRelatedTest() {
		assertEquals(kking.isActive(), false);
		assertEquals(jhanneman.isActive(), false);

		kking.addIssue(issue);
		assertEquals(kking.getIssues().contains(issue), true);

		assertEquals(kking.isActive(), true);
		assertEquals(jhanneman.isActive(), false);
	}
}