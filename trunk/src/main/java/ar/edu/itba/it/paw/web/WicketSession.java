package ar.edu.itba.it.paw.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.UserRepo;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class WicketSession extends WebSession {

	private EntityModel<User> userModel;
	private EntityModel<Project> projectModel;
	private Map<Project, EntityModel<Filter>> filtersMap;

	public static WicketSession get() {
		return (WicketSession) Session.get();
	}

	public WicketSession(Request request) {
		super(request);
		this.filtersMap = new HashMap<Project, EntityModel<Filter>>();
	}

	public User getUser() {
		return (userModel == null) ? null : userModel.getObject();
	}
	
	public Project getProject() {
		return (projectModel == null) ? null : projectModel.getObject();
	}
	
	public Filter getFilter() {
		EntityModel<Filter> filterModel = filtersMap.get(getProject());
		return (filterModel == null) ? null : filterModel.getObject();
	}
	
	public void removeFilter(Filter filter){
		filtersMap.remove(filter.getProject());
	}
	
	public boolean login(String name, String password, UserRepo userRepo) {
		User user = userRepo.get(name);
		if (user == null || !user.verifyPassword(password) || !user.getValid())
			return false;
		this.userModel = new EntityModel<User>(User.class, user);
		return true;
	}

	public void setProject(Project project){
		this.projectModel = new EntityModel<Project>(Project.class, project);
	}

	public void addFilter(Filter filter){
		filtersMap.put(filter.getProject(), new EntityModel<Filter>(Filter.class, filter));
	}
	
	public boolean logged() {
		return userModel != null;
	}

	public void logout() {
        Session.get().invalidate();
        clear();
	}
	
	@Override
	public void detach(){
		super.detach();
		if(userModel != null)
			userModel.detach();
		if(projectModel != null)
			projectModel.detach();
		//if(filterModel != null)
		//	filterModel.detach();
	}
}
