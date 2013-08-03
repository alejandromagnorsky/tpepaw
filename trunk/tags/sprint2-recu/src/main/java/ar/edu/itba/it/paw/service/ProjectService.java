package ar.edu.itba.it.paw.service;

import java.util.List;
import java.util.SortedMap;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectStatus;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;

public interface ProjectService {

	public Project getProject(String code);

	public Project getProject(int id);

	public List<Project> getProjects();

	public List<Project> getPublicList();

	public List<Project> getViewableList(User user);

	public boolean isMember(User user, Project project);
	
	public void addProjectUser(User source, Project project, User user);
	
	public void removeProjectUser(User source, Project project, User user);

	public void addProject(User source, Project p);

	public void updateProject(User source, Project p);

	public boolean canEditProject(User user);

	public boolean canAddProject(User user);
	
	public ProjectStatus getProjectStatus(Project p);

	public boolean canViewProject(User user, Project project);
	
	public boolean canViewWorkReport(User user, Project project);

	public boolean canViewStatus(User user, Project project);
	
	public boolean hasLeaderRights(User user, Project project);

	public boolean canAddUserToProject(User user, Project project);

	public boolean canRemoveUserFromProject(User user, Project project);

	public SortedMap<User, Time> getWorkReport(Project project, DateTime from,
			DateTime to);

}
