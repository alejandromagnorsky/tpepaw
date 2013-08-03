package ar.edu.itba.it.paw.dao;

import java.util.List;

import ar.edu.itba.it.paw.Project;

public interface ProjectDAO {
	
	public List<Project> load();

	public Project load(String code);
	
	public Project load(int id);
	
	public void save(Project p);

}
