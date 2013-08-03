package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class ProjectDataProvider extends SortableDataProvider<Project> {

	private ProjectRepo projectRepo;
	
	public ProjectDataProvider(ProjectRepo projectRepo) {
		setSort("name", true);
		this.projectRepo = projectRepo;
	}
	
	private List<Project> getProjects() {
		User user = WicketSession.get().getUser();
		List<Project> projects = projectRepo.getViewableList(user);
		if (projects == null)
			return new ArrayList<Project>();
		sort(projects);
		return projects;
	}

	public Iterator<Project> iterator(int first, int count) {
		return getProjects().subList(first, first + count).iterator();
	}

	public int size() {
		return getProjects().size();
	}

	public IModel<Project> model(Project project) {
		return new EntityModel<Project>(Project.class, project);
	}
	
	private void sort(List<Project> projects){
		final boolean isAsc = getSort().isAscending();
		String property = getSort().getProperty();
		
		if (property.equals("code")){
			if (isAsc)
				Collections.sort(projects);
			else
				Collections.reverse(projects);
		} else if (property.equals("name")){
			Collections.sort(projects, new Comparator<Project>(){
				public int compare(Project p1, Project p2) {
					if (!isAsc)
						return p2.getName().compareTo(p1.getName());
					return p1.getName().compareTo(p2.getName());
				}				
			});
		} else if (property.equals("leader")) {
			Collections.sort(projects, new Comparator<Project>(){
				public int compare(Project p1, Project p2) {
					if (!isAsc)
						return p2.getLeader().compareTo(p1.getLeader());
					return p1.getLeader().compareTo(p2.getLeader());
				}				
			});
		}
	}

}