package ar.edu.itba.it.paw.repo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

@Repository
public class HibernateProjectRepo extends GenericHibernateRepo implements
		ProjectRepo {

	@Autowired
	public HibernateProjectRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<Project> get() {
		return find("FROM Project");
	}

	public List<Project> getPublic() {
		return find("FROM Project WHERE ispublic = TRUE");
	}

	public List<Project> getViewableList(User user) {
		List<Project> out = new ArrayList<Project>();
		List<Project> projects = get();

		for (Project p : projects)
			if (p.canViewProject(user))
				out.add(p);

		return out;
	}

	public Project get(String code) {
		List<Project> projects = find("FROM Project WHERE code = ?", code);
		if (projects.size() == 1)
			return projects.get(0);
		return null;
	}

	public Project get(int id) {
		return get(Project.class, id);
	}

	public void add(User source, Project p) {
		if(source == null || !source.canAddProject())
			throw new IllegalArgumentException();
		set(p);
	}

	public Version getVersion(int id) {
		List<Version> versions = find("FROM Version WHERE id = ?", id);
		if (versions.size() == 1)
			return versions.get(0);
		return null;
	}

	public Filter getFilter(int id) {
		List<Filter> filters = find("FROM Filter WHERE id = ?", id);
		if (filters.size() == 1)
			return filters.get(0);
		return null;
	}
}
