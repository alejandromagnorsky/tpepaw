package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterFormValidator extends FormValidator {

	private static RegisterFormValidator instance = new RegisterFormValidator();

	private RegisterFormValidator() {

	}

	public static RegisterFormValidator getInstance() {
		return instance;
	}

	public boolean validate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("usernameRegister");
		String password = req.getParameter("passwordRegister");
		String fullname = req.getParameter("fullnameRegister");
		boolean isValid = true;

		req.setAttribute("usernameRegister", username);
		req.setAttribute("passwordRegister", password);
		req.setAttribute("fullnameRegister", fullname);

		if (!validateRequiredField(username)) {
			req.setAttribute("registerNameError", "Campo requerido.");
			isValid = false;
		} else if (validateExistingUser(username)) {
			req.setAttribute("registerNameError", "Usuario existente.");
			isValid = false;
		}

		if (!validateRequiredField(password)) {
			req.setAttribute("registerPasswordError", "Campo requerido.");
			isValid = false;
		}

		if (!validateRequiredField(fullname)) {
			req.setAttribute("registerFullnameError", "Campo requerido.");
			isValid = false;
		}

		return isValid;
	}

}
