package ar.edu.itba.it.paw.repo;

import java.util.List;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

public interface ProjectRepo {

	public List<Project> get();

	public Project get(String code);

	public Project get(int id);

	public List<Project> getPublic();

	public List<Project> getViewableList(User user);

	public void add(User source, Project p);

	public Version getVersion(int id);

	public Filter getFilter(int id);

	public void persist(Object object);
}
