package ar.edu.itba.it.paw.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.it.paw.dao.UserDAO;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

@Service
public class UserServiceImpl implements UserService {

	private UserDAO userDAO;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private IssueService issueService;

	@Autowired
	private UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void register(User source, User target) {
		if (!canRegisterUser(source) || getUser(target.getName()) != null)
			throw new IllegalArgumentException();
		userDAO.save(target);
	}

	public User getUser(int id) {
		return userDAO.load(id);
	}

	public User getUser(String name) {
		return userDAO.load(name);
	}

	public boolean canInvalidateUser(User user) {
		return user != null && user.getAdmin();
	}

	public boolean canRegisterUser(User user) {
		return user != null && user.getAdmin();
	}

	public boolean isLeader(User user) {
		List<Project> projects = projectService.getProjects();
		for (Project project : projects)
			if (project.getLeader().getId() == user.getId())
				return true;
		return false;

	}

	public boolean isActive(User user) {
		return issueService.getAssignedIssues(user).size() != 0;
	}

	public boolean verifyUser(String name, String password) {
		User user = getUser(name);
		if (user == null || !user.verifyPassword(password) || !user.getValid())
			return false;
		return true;
	}

	public void invalidateUser(User source, User target) {
		if (!canInvalidateUser(source) || getUser(target.getName()) == null
				|| isActive(target) || !target.getValid())
			throw new IllegalArgumentException();
		target.setValid(false);
		userDAO.save(target);
	}

	public List<User> getValidUsers() {
		List<User> out = new ArrayList<User>();
		for (User user : userDAO.load())
			if (user.getValid())
				out.add(user);
		Collections.sort(out);
		return out;
	}
}
