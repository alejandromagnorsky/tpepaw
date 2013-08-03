package ar.edu.itba.it.paw.dao;

import java.util.List;

import ar.edu.itba.it.paw.User;

public interface UserDAO {

	public List<User> load();

	public User load(int id);

	public User load(String name);

	public void save(User user);
}
