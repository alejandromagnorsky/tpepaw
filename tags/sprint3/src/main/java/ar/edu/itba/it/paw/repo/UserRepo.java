package ar.edu.itba.it.paw.repo;

import java.util.List;

import ar.edu.itba.it.paw.model.User;

public interface UserRepo {

	public List<User> get();

	public User get(int id);

	public User get(String name);

	public void add(User source, User target);
	
	public List<User> getValidUsers();
}
