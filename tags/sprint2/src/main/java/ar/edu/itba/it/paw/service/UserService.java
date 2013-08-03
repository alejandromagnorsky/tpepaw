package ar.edu.itba.it.paw.service;

import java.util.List;

import ar.edu.itba.it.paw.model.User;

public interface UserService {

	public User getUser(int id);

	public User getUser(String name);

	public void register(User source, User target);

	public boolean verifyUser(String name, String password);

	public boolean isLeader(User user);

	public boolean isActive(User user);

	public void invalidateUser(User source, User target);

	public boolean canInvalidateUser(User user);

	public boolean canRegisterUser(User user);
	
	public List<User> getValidUsers();
	
}
