package ar.edu.itba.it.paw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.paw.dao.ProjectDAO;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectStatus;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectDAO projectDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private IssueService issueService;

	@Autowired
	public ProjectServiceImpl(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public Project getProject(String code) {
		return projectDAO.load(code);
	}

	public Project getProject(int id) {
		return projectDAO.load(id);
	}

	public List<Project> getProjects() {
		return projectDAO.load();
	}

	public List<Project> getPublicList() {
		List<Project> publicProjects = new ArrayList<Project>();
		List<Project> projects = getProjects();

		for (Project p : projects)
			if (p.getIsPublic())
				publicProjects.add(p);

		return publicProjects;
	}

	public void addProject(User source, Project p) {
		if (!p.isNew()
				|| getProject(p.getCode()) != null
				|| !canAddProject(source)
				|| userService.getUser(p.getLeader().getName()) == null
				|| !p.getLeader().getValid())
			throw new IllegalArgumentException();
		projectDAO.save(p);
	}

	public void updateProject(User source, Project p) {
		Project other = getProject(p.getCode());
		if (p.isNew()
				|| (other != null && other.getId() != p.getId())
				|| (!canEditProject(source) && !canAddUserToProject(source, p))
				|| userService.getUser(p.getLeader().getName()) == null
				|| !p.getLeader().getValid()) {

			throw new IllegalArgumentException();
		}

		projectDAO.save(p);
	}

	public boolean hasLeaderRights(User user, Project project) {
		return user != null && user.getAdmin()
				|| project.getLeader().equals(user);
	}

	public boolean canEditProject(User user) {
		return user != null && user.getAdmin();
	}

	public boolean canAddProject(User user) {
		return user != null && user.getAdmin();
	}

	public boolean canViewProject(User user, Project project) {
		return project.getIsPublic()
				|| (user != null && (isMember(user, project) || user.getAdmin()));
	}

	public boolean isMember(User user, Project project) {
		return project.getUsers().contains(user) || project.getLeader().equals(user);
	}

	public List<Project> getViewableList(User user) {
		List<Project> out = new ArrayList<Project>();
		List<Project> projects = getProjects();

		for (Project p : projects)
			if (canViewProject(user, p))
				out.add(p);

		return out;
	}
	
	public ProjectStatus getProjectStatus(Project p){
		return new ProjectStatus(issueService.getIssues(p));
	}

	public boolean canAddUserToProject(User user, Project project) {
		return hasLeaderRights(user, project);
	}

	public boolean canRemoveUserFromProject(User user, Project project) {
		return hasLeaderRights(user, project);
	}

	public SortedMap<User, Time> getWorkReport(Project project, DateTime from,
			DateTime to) {
		SortedMap<User, Time> out = new TreeMap<User, Time>();

		for (User user : project.getUsers()) {

			Time totalTime = new Time(0);

			SortedSet<Work> works = issueService.getWorks(user);
			for (Work work : works)
				if (work.getIssue().getProject().equals(project)
						&& work.getDate().isAfter(from.toInstant())
						&& work.getDate().isBefore(to.toInstant()))
					totalTime.add(work.getDedicatedTime());

			if (totalTime.getMinutes() > 0)
				out.put(user, totalTime);
		}

		return out;
	}

	public boolean canViewWorkReport(User user, Project project) {
		return hasLeaderRights(user, project);
	}
	
	public boolean canViewStatus(User user, Project project) {
		return hasLeaderRights(user, project);
	}
	
	public void addProjectUser(User source, Project project, User user){
		if(!canAddUserToProject(source, project))
			throw new IllegalArgumentException();
		project.addUser(user);
		updateProject(source, project);	
	}
	
	public void removeProjectUser(User source, Project project, User user){
		if(!canRemoveUserFromProject(source, project))
			throw new IllegalArgumentException();
		project.removeUser(user);
		updateProject(source, project);		
	}
}
