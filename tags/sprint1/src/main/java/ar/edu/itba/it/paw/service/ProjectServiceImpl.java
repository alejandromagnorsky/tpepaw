package ar.edu.itba.it.paw.service;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.dao.ProjectDAO;
import ar.edu.itba.it.paw.dao.ProjectDAO_JDBC;

public class ProjectServiceImpl implements ProjectService {
	private static ProjectServiceImpl instance = new ProjectServiceImpl();
	private ProjectDAO projectDAO = ProjectDAO_JDBC.getInstance();
	private PermissionManager permissionManager = PermissionManager.getInstance();
	
	public static ProjectServiceImpl getInstance() {
		return instance;
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
		if (getProject(p.getId()) != null
				|| !permissionManager.canAddProject(source)
				|| UserServiceImpl.getInstance().getUser(
						p.getLeader().getName()) == null
				|| !p.getLeader().getValid())
			throw new IllegalArgumentException();
		p.setId(0);
		projectDAO.save(p);
	}

	public void updateProject(User source, Project p) {
		Project other = getProject(p.getCode());
		if (getProject(p.getId()) == null
				|| (other != null && other.getId() != p.getId())
				|| !permissionManager.canAddProject(source)
				|| UserServiceImpl.getInstance().getUser(
						p.getLeader().getName()) == null
				|| !p.getLeader().getValid()){
			
			System.out.println(p.getId());
			System.out.println(other.getId());
			throw new IllegalArgumentException();			
		}
		
		projectDAO.save(p);
	}
}
