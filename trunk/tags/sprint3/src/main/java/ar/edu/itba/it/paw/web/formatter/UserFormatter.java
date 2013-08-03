package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.repo.UserRepo;

@Component
public class UserFormatter implements Formatter<User> {

	private UserRepo userRepo;

	@Autowired
	public UserFormatter(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public String print(User user, Locale l) {
		return user.getName();
	}

	public User parse(String name, Locale l) throws ParseException {
		return userRepo.get(name);
	}

}
