package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AddProjectFormValidator extends ProjectFormValidator {
	private static AddProjectFormValidator instance = new AddProjectFormValidator();
	
	private AddProjectFormValidator(){}

	public static AddProjectFormValidator getInstance(){
		return instance;
	}
	
	// Project code validation: it is 'required field' and mustn't be in use by other project.
	@Override
	protected boolean validateProjectCode(String project_code, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!validateRequiredField(project_code)){
			req.setAttribute("projectCodeError", "Campo requerido.");
			return false;
		} else {
			if (validateExistingProject(project_code)){
				req.setAttribute("projectCodeError", "Código en uso.");
				return false;
			} else {
				req.setAttribute("projectCodeError", "");
				return true;
			}
		}
	}
}
