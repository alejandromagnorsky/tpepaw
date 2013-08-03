package ar.edu.itba.it.paw.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.UserService;

@Component
public class UserFormatter implements Formatter<User> {

	private UserService userService;

	@Autowired
	public UserFormatter(UserService userService) {
		this.userService = userService;
	}

	public String print(User user, Locale l) {
		return user.getName();
	}

	public User parse(String name, Locale l) throws ParseException {
		return userService.getUser(name);
	}

}
