package ar.edu.itba.it.paw.service;

import java.util.List;

import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;

public interface ProjectService {
	
	public Project getProject(String code);
	
	public Project getProject(int id);
	
	public List<Project> getProjects();
	
	public List<Project> getPublicList();
	
	public void addProject(User source, Project p);
	
	public void updateProject(User source, Project p);
	
}
