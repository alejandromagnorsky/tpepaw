package ar.edu.itba.it.paw.service;

import java.util.List;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.dao.UserDAO;
import ar.edu.itba.it.paw.dao.UserDAO_JDBC;

public class UserServiceImpl implements UserService {

	private static UserServiceImpl instance = new UserServiceImpl();
	private UserDAO userDAO = UserDAO_JDBC.getInstance();;
	private PermissionManager permissionManager = PermissionManager.getInstance();
	
	private UserServiceImpl() {

	}

	public static UserServiceImpl getInstance() {
		return instance;
	}

	public void register(User source, User target) {
		if (!permissionManager.canRegister(source)
				|| getUser(target.getName()) != null)
			throw new IllegalArgumentException();
		target.setId(0);
		userDAO.save(target);
	}

	public void editUser(User source, User target) {
		// TODO
	}

	public User getUser(int id) {
		return userDAO.load(id);
	}

	public User getUser(String name) {
		return userDAO.load(name);
	}

	public boolean isLeader(User user) {
		ProjectService projectService = ProjectServiceImpl.getInstance();
		List<Project> projects = projectService.getProjects();
		for (Project project : projects)
			if (project.getLeader().getId() == user.getId())
				return true;
		return false;

	}

	public boolean isActive(User user) {
		return IssueServiceImpl.getInstance().getAssignedIssues(user).size() != 0;
	}

	public boolean verifyUser(String name, String password) {
		User user = getUser(name);
		if (user == null || !user.verifyPassword(password) || !user.getValid())
			return false;
		return true;
	}

	public void invalidateUser(User source, User target) {
		if (!permissionManager.canInvalidateUser(source)
				|| getUser(target.getName()) == null || isActive(target)
				|| !target.getValid())
			throw new IllegalArgumentException();
		target.setValid(false);
		userDAO.save(target);
	}
}
