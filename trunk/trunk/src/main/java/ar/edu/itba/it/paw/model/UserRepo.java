package ar.edu.itba.it.paw.model;

import java.util.List;

public interface UserRepo {

	public List<User> get();

	public User get(int id);

	public User get(String name);

	public void add(User source, User target);
	
	public List<User> getValidUsers();
}
