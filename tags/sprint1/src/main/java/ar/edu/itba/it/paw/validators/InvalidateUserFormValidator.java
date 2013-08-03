package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class InvalidateUserFormValidator extends FormValidator {
	private static InvalidateUserFormValidator instance = new InvalidateUserFormValidator();

	private InvalidateUserFormValidator() {
	}

	public static InvalidateUserFormValidator getInstance() {
		return instance;
	}

	public boolean validate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("usernameInvalidate");

		req.setAttribute("usernameInvalidate", username);

		if (!validateRequiredField(username)) {
			req.setAttribute("invalidateNameError", "Campo requerido.");
			return false;
		}

		if (req.getSession().getAttribute("name").equals(username)) {
			req.setAttribute("invalidateNameError",
					"No puede invalidarse a si mismo.");
			return false;
		}

		if (!validateExistingUser(username)) {
			req.setAttribute("invalidateNameError", "Usuario inexistente.");
			return false;
		}

		UserService userService = UserServiceImpl.getInstance();
		User user = userService.getUser(username);
		if (userService.isActive(user)) {
			req.setAttribute("invalidateNameError", "Usuario activo.");
			return false;
		}

		if (!user.getValid()) {
			req.setAttribute("invalidateNameError", "Usuario ya invalidado.");
			return false;
		}

		return true;
	}

}
