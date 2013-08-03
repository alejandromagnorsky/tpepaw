package ar.edu.itba.it.paw.model;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.model.User;

@Repository
public class HibernateUserRepo extends GenericHibernateRepo implements UserRepo {	
	
	@Autowired
	public HibernateUserRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<User> get() {
		return find("FROM User");
	}

	public User get(int id) {
		return get(User.class, id);
	}

	public User get(String name) {
		List<User> users = find("FROM User WHERE name = ?", name);
		if(users.size() == 1)
			return users.get(0);
		return null;
	}

	public void add(User source, User target) {
		if (!source.canRegisterUser()
				|| get(target.getName()) != null)
			throw new IllegalArgumentException();
		set(target);
	}
	
	public List<User> getValidUsers() {
		List<User> users = find("FROM User WHERE valid = true");
		Collections.sort(users);
		return users;
	}
}
