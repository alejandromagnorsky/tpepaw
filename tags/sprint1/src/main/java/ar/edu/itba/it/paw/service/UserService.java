package ar.edu.itba.it.paw.service;

import ar.edu.itba.it.paw.User;

public interface UserService {

	public User getUser(int id);

	public User getUser(String name);
	
	public void register(User source, User target);
	
	public void editUser(User source, User target);

	public boolean verifyUser(String name, String password);

	public boolean isLeader(User user);
	
	public boolean isActive(User user);
	
	public void invalidateUser(User source, User target);
}
