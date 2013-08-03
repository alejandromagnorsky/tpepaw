package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ProjectFormValidator extends FormValidator {
	
	abstract protected boolean validateProjectCode(String project_code, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
	
	protected boolean validateExistingProject(String p_code){
		return PS.getProject(p_code) != null;
	}
	
	public boolean validate(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
		boolean isValid = true;
		String project_name = req.getParameter("projectName");
		String project_code = req.getParameter("projectCode");
		String project_leader_name = req.getParameter("projectLeader");
			
		// Project name validation: it is 'required field'.
		if (!validateRequiredField(project_name)) {
			req.setAttribute("projectNameError", "Campo requerido.");
			isValid = false;
		} else
			req.setAttribute("projectNameError", "");
		
		// Project code validation
		if (!validateProjectCode(project_code, req, resp))
			isValid = false;
		
		// Project leader validation: it is 'required field' and must be an exiting user.
		if (!validateRequiredField(project_leader_name)){
			req.setAttribute("projectLeaderError", "Campo requerido.");
			isValid = false;
		} else {
			if (!validateExistingUser(project_leader_name)){
				req.setAttribute("projectLeaderError", "Usuario inexistente.");
				isValid = false;
			} else if (!US.getUser(project_leader_name).getValid()) {
				req.setAttribute("projectLeaderError", "Usuario invalidado.");
				isValid = false;
			} else {
				req.setAttribute("projectLeaderError", "");
			}
		}
		
		return isValid;
	}
}
