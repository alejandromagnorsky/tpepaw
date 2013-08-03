package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditProjectFormValidator extends ProjectFormValidator {
	private static EditProjectFormValidator instance = new EditProjectFormValidator();

	private EditProjectFormValidator() {
	}

	public static EditProjectFormValidator getInstance() {
		return instance;
	}

	// Project code validation: it is 'required field' and mustn't be in use by
	// any
	// other project (except actual one).
	@Override
	protected boolean validateProjectCode(String project_code,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!validateRequiredField(project_code)) {
			req.setAttribute("projectCodeError", "Campo requerido.");
			return false;
		} else {
			if (PS.getProject(project_code) != null
					&& !project_code.equals(req
							.getParameter("originalProjectCode"))) {
				req.setAttribute("projectCodeError", "Código en uso.");
				return false;
			} else {
				req.setAttribute("projectCodeError", "");
				return true;
			}
		}
	}
}
