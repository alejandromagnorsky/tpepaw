package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Validator {
	public boolean validate(HttpServletRequest req, HttpServletResponse resp)
							throws ServletException, IOException;
}
