package ar.edu.itba.it.paw;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

public class TestProject {

	private Project reignInBlood;
	private Project seasonsInTheAbyss;
	private User jhanneman;
	private User kking;
	private User taraya;
	private Issue issue;

	@Before
	public void initialize() {
		jhanneman = new User(true, true, "jhanneman", "slowdeath", "Jeff Hanneman", "jhanneman@joinus.com");
		kking = new User(false, true, "kking", "immensedecay", "Kerry King", "kking@joinus.com");
		taraya = new User(false, true, "taraya", "meaningofpain", "Tom Araya", "taraya@joinus.com");

		reignInBlood = new Project("1986", "Reign In Blood", "Def Jam", jhanneman, true, new SimpleListener());
		seasonsInTheAbyss = new Project("1990", "Seasons In The Abyss", "Def Jam", kking, false, new SimpleListener());

		issue = new Issue(reignInBlood, "Angel Of Death", "1", new DateTime(), new Time(451), null, jhanneman, Issue.State.Open, Issue.Priority.Critical, Issue.Type.NewFeature, new SimpleListener());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setNameTest(){
		reignInBlood.setName("Justin Bieber Justin Bieber");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setDescriptionTest(){
		String jb = "Justin Bieber";
		for (int i=0; i < 20; i++)
			jb = jb + "Justin Bieber";
		reignInBlood.setDescription(jb);
	}
	
	@Test
	public void addIssueTest(){
		assertEquals(reignInBlood.getIssues().size() == 1, true);
		
		reignInBlood.addIssue(issue);
		assertEquals(reignInBlood.getIssues().size() == 2, true);
		
		reignInBlood.addIssue(issue);
		assertEquals(reignInBlood.getIssues().size() == 3, true);
	}
	
	@Test
	public void addAndRemoveUserTest(){
		reignInBlood.addUser(jhanneman, kking);
		assertEquals(reignInBlood.getUsers().contains(kking) && reignInBlood.getUsers().size() == 1, true);
		seasonsInTheAbyss.addUser(jhanneman, kking);
		assertEquals(seasonsInTheAbyss.getUsers().contains(kking) && seasonsInTheAbyss.getUsers().size() == 1, true);
		seasonsInTheAbyss.addUser(kking, jhanneman);
		assertEquals(seasonsInTheAbyss.getUsers().contains(jhanneman) && seasonsInTheAbyss.getUsers().size() == 2, true);
		
		reignInBlood.removeUser(jhanneman, kking);
		assertEquals(reignInBlood.getUsers().isEmpty(), true);
		seasonsInTheAbyss.removeUser(kking, jhanneman);
		assertEquals(seasonsInTheAbyss.getUsers().contains(kking) && seasonsInTheAbyss.getUsers().size() == 1, true);
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotAddUserTest(){
		reignInBlood.addUser(kking, jhanneman);
	}
	
	@Test
	public void addVersionTest(){
		reignInBlood.addVersion(new Version("alpha", "alpha", new DateTime()));
		assertEquals(reignInBlood.getVersions().size() == 1, true);
		
		seasonsInTheAbyss.addVersion(new Version("alpha", "alpha", new DateTime()));
		assertEquals(seasonsInTheAbyss.getVersions().size() == 1, true);
		seasonsInTheAbyss.addVersion(new Version("beta", "alpha", new DateTime()));
		assertEquals(seasonsInTheAbyss.getVersions().size() == 2, true);
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotRemoveUserTest(){
		reignInBlood.addUser(jhanneman, kking);
		assertEquals(reignInBlood.getUsers().contains(kking) && reignInBlood.getUsers().size() == 1, true);
		reignInBlood.removeUser(kking, kking);
	}
	
	@Test
	public void addAndDeleteFilterTest(){
		Filter f1 = new Filter("Enter", jhanneman, reignInBlood, "", "", "", null, null, Issue.Type.Error, Issue.State.Closed, Issue.Resolution.Duplicated, null, null, null, null);
		Filter f2 = new Filter("the", kking, seasonsInTheAbyss, "", "", "", null, null, Issue.Type.Error, Issue.State.Closed, Issue.Resolution.Duplicated, null, null, null, null);
		Filter f3 = new Filter("realm", kking, seasonsInTheAbyss, "", "", "", null, null, Issue.Type.Error, Issue.State.Closed, Issue.Resolution.Duplicated, null, null, null, null);
		Filter f4 = new Filter("of", jhanneman, reignInBlood, "", "", "", null, null, Issue.Type.Error, Issue.State.Closed, Issue.Resolution.Duplicated, null, null, null, null);
		Filter f5 = new Filter("Satan", jhanneman, reignInBlood, "", "", "", null, null, Issue.Type.Error, Issue.State.Closed, Issue.Resolution.Duplicated, null, null, null, null);
		
		reignInBlood.addFilter(jhanneman, f1);
		assertEquals(reignInBlood.getFilters().size() == 1, true);
		reignInBlood.addFilter(jhanneman, f1);
		assertEquals(reignInBlood.getFilters().size() == 1, true);
		reignInBlood.addFilter(jhanneman, f1);
		assertEquals(reignInBlood.getFilters().size() == 1, true);
		reignInBlood.addFilter(kking, f2);
		assertEquals(reignInBlood.getFilters().size() == 2, true);
		reignInBlood.addFilter(taraya, f3);
		assertEquals(reignInBlood.getFilters().size() == 3, true);
		
		seasonsInTheAbyss.addFilter(jhanneman, f4);
		assertEquals(seasonsInTheAbyss.getFilters().size() == 1, true);
		seasonsInTheAbyss.addFilter(kking, f5);
		assertEquals(seasonsInTheAbyss.getFilters().size() == 2, true);
		
		reignInBlood.deleteFilter(jhanneman, f1);
		assertEquals(reignInBlood.getFilters().size() == 2, true);
		reignInBlood.deleteFilter(kking, f2);
		assertEquals(reignInBlood.getFilters().size() == 1, true);
		reignInBlood.deleteFilter(taraya, f3);
		assertEquals(reignInBlood.getFilters().size() == 0, true);

		seasonsInTheAbyss.deleteFilter(jhanneman, f4);
		assertEquals(seasonsInTheAbyss.getFilters().size() == 1, true);
		seasonsInTheAbyss.deleteFilter(kking, f5);
		assertEquals(seasonsInTheAbyss.getFilters().size() == 0, true);	
		
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotAddFilterTest1(){
		seasonsInTheAbyss.addFilter(taraya, new Filter("a", taraya, seasonsInTheAbyss, "a", "a", "a", null, null, null, null, null, null, null, null, null));
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotAddFilterTest2(){
		reignInBlood.addFilter(null, new Filter("a", taraya, reignInBlood, "a", "a", "a", null, null, null, null, null, null, null, null, null));
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotDeleteFilterTest1(){
		Filter f = new Filter("a", taraya, reignInBlood, "a", "a", "a", null, null, null, null, null, null, null, null, null);
		reignInBlood.addFilter(jhanneman, f);
		assertEquals(reignInBlood.getFilters().size() == 1, true);
		reignInBlood.deleteFilter(null, f);
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void cannotDeleteFilterTest2(){
		Filter f = new Filter("a", taraya, seasonsInTheAbyss, "a", "a", "a", null, null, null, null, null, null, null, null, null);
		seasonsInTheAbyss.addFilter(jhanneman, f);
		assertEquals(seasonsInTheAbyss.getFilters().size() == 1, true);
		seasonsInTheAbyss.deleteFilter(taraya, f);
	}
	
	@Test
	public void isMemberTest(){
		assertEquals(reignInBlood.isMember(jhanneman), true);
		assertEquals(reignInBlood.isMember(kking), false);
		assertEquals(reignInBlood.isMember(taraya), false);
		assertEquals(reignInBlood.isMember(null), false);
		assertEquals(seasonsInTheAbyss.isMember(jhanneman), false);
		assertEquals(seasonsInTheAbyss.isMember(kking), true);
		assertEquals(seasonsInTheAbyss.isMember(taraya), false);
		assertEquals(seasonsInTheAbyss.isMember(null), false);
		
		reignInBlood.addUser(jhanneman, kking);
		assertEquals(reignInBlood.isMember(kking), true);
	}
	
	// PERMISSION TESTS DOWN BELOW
	@Test
	public void hasLeaderRightsTest(){
		assertEquals(reignInBlood.hasLeaderRights(jhanneman), true);
		assertEquals(seasonsInTheAbyss.hasLeaderRights(jhanneman), true);
		assertEquals(reignInBlood.hasLeaderRights(kking), false);
		assertEquals(seasonsInTheAbyss.hasLeaderRights(kking), true);
		assertEquals(reignInBlood.hasLeaderRights(taraya), false);
		assertEquals(seasonsInTheAbyss.hasLeaderRights(taraya), false);
		assertEquals(reignInBlood.hasLeaderRights(null), false);
		assertEquals(seasonsInTheAbyss.hasLeaderRights(null), false);
	}
	
	@Test
	public void canAddUserTest(){
		hasLeaderRightsTest();
	}

	@Test
	public void canRemoveUserTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canAddVersionTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canEditVersionTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canDeleteVersionTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canViewWorkReportTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canViewStatusTest(){
		hasLeaderRightsTest();
	}
	
	@Test
	public void canViewIssueTest(){
		assertEquals(reignInBlood.canViewIssue(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canViewIssue(jhanneman), true);
		assertEquals(reignInBlood.canViewIssue(kking), true);
		assertEquals(seasonsInTheAbyss.canViewIssue(kking), true);
		assertEquals(reignInBlood.canViewIssue(taraya), true);
		assertEquals(seasonsInTheAbyss.canViewIssue(taraya), false);
		assertEquals(reignInBlood.canViewIssue(null), true);
		assertEquals(seasonsInTheAbyss.canViewIssue(null), false);
	}
	
	@Test
	public void canAddIssueTest(){
		assertEquals(reignInBlood.canAddIssue(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canAddIssue(jhanneman), true);
		assertEquals(reignInBlood.canAddIssue(kking), false);
		assertEquals(seasonsInTheAbyss.canAddIssue(kking), true);
		assertEquals(reignInBlood.canAddIssue(taraya), false);
		assertEquals(seasonsInTheAbyss.canAddIssue(taraya), false);
		assertEquals(reignInBlood.canAddIssue(null), false);
		assertEquals(seasonsInTheAbyss.canAddIssue(null), false);
	}
	
	@Test
	public void canEditIssueTest(){
		canAddIssueTest();
	}
	
	@Test
	public void canViewAssignedTest(){
		canAddIssueTest();
	}
	
	@Test
	public void canAddFilterTest(){
		assertEquals(reignInBlood.canAddFilter(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canAddFilter(jhanneman), true);
		assertEquals(reignInBlood.canAddFilter(kking), true);
		assertEquals(seasonsInTheAbyss.canAddFilter(kking), true);
		assertEquals(reignInBlood.canAddFilter(taraya), true);
		assertEquals(seasonsInTheAbyss.canAddFilter(taraya), false);
		assertEquals(reignInBlood.canAddFilter(null), false);
		assertEquals(seasonsInTheAbyss.canAddFilter(null), false);
	}
	
	@Test
	public void canViewFilterManagerTest(){
		canAddFilterTest();
	}
	
	@Test
	public void canEditFilterTest(){
		canAddFilterTest();
	}
	
	@Test
	public void canDeleteFilterTest(){
		canAddFilterTest();
	}
	
	@Test
	public void canViewVersionListTest(){
		assertEquals(reignInBlood.canViewVersionList(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canViewVersionList(jhanneman), true);
		assertEquals(reignInBlood.canViewVersionList(kking), true);
		assertEquals(seasonsInTheAbyss.canViewVersionList(kking), true);
		assertEquals(reignInBlood.canViewVersionList(taraya), true);
		assertEquals(seasonsInTheAbyss.canViewVersionList(taraya), false);
		assertEquals(reignInBlood.canViewVersionList(null), true);
	}
	
	@Test
	public void canViewIssueFilesTest(){
		assertEquals(reignInBlood.canViewIssueFiles(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canViewIssueFiles(jhanneman), true);
		assertEquals(reignInBlood.canViewIssueFiles(kking), true);
		assertEquals(seasonsInTheAbyss.canViewIssueFiles(kking), true);
		assertEquals(reignInBlood.canViewIssueFiles(taraya), true);
		assertEquals(seasonsInTheAbyss.canViewIssueFiles(taraya), false);
		assertEquals(reignInBlood.canViewIssueFiles(null), true);
		assertEquals(seasonsInTheAbyss.canViewIssueFiles(null), false);
	}
	
	@Test
	public void canRelateIssuesTest(){
		assertEquals(reignInBlood.canRelateIssues(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canRelateIssues(jhanneman), true);
		assertEquals(reignInBlood.canRelateIssues(kking), false);
		assertEquals(seasonsInTheAbyss.canRelateIssues(kking), true);
		assertEquals(reignInBlood.canRelateIssues(taraya), false);
		assertEquals(seasonsInTheAbyss.canRelateIssues(taraya), false);
		assertEquals(reignInBlood.canRelateIssues(null), false);
		assertEquals(seasonsInTheAbyss.canRelateIssues(null), false);
	}
	
	@Test
	public void canViewProjectTest(){
		assertEquals(reignInBlood.canViewProject(jhanneman), true);
		assertEquals(seasonsInTheAbyss.canViewProject(jhanneman), true);
		assertEquals(reignInBlood.canViewProject(kking), true);
		assertEquals(seasonsInTheAbyss.canViewProject(kking), true);
		assertEquals(reignInBlood.canViewProject(taraya), true);
		assertEquals(seasonsInTheAbyss.canViewProject(taraya), false);
		assertEquals(reignInBlood.canViewProject(null), true);
		assertEquals(seasonsInTheAbyss.canViewProject(null), false);
	}

}
