package ar.edu.itba.it.paw.validators;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IssueFormValidator extends FormValidator {

	protected boolean validateEstimatedTime(String estimated_time) {
		try {
			float hours = Float.valueOf(estimated_time);
			if (hours < 0)
				return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean validate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isValid = true;
		String issue_name = req.getParameter("issueName");
		String issue_assigned_user_name = req.getParameter("issueAssignedUser");
		String issue_estimated_time = req.getParameter("issueEstimatedTime");

		// Issue name validation: it is 'required field'.
		if (!validateRequiredField(issue_name)) {
			req.setAttribute("issueNameError", "Campo requerido.");
			isValid = false;
		} else
			req.setAttribute("issueNameError", "");

		// Issue code validation --> senseless validation as issue code is
		// auto-generated
		// Issue priority validation --> senseless validation as issue priority
		// cannot be wrong (comes from a 'select')

		// Issue user validation: if introduced, must be an exiting user.
		if (!issue_assigned_user_name.equals("")
				&& !validateExistingUser(issue_assigned_user_name)) {
			req.setAttribute("issueAssignedUserError", "Usuario inexistente.");
			isValid = false;
		} else if (!issue_assigned_user_name.equals("")
				&& !US.getUser(issue_assigned_user_name).getValid()) {
			req.setAttribute("issueAssignedUserError", "Usuario invalidado.");
			isValid = false;
		} else {
			req.setAttribute("issueAssignedUserError", "");
		}

		// Issue estimated time validation: if introduced, must be a positive
		// float.
		if (!issue_estimated_time.equals("")
				&& !validateEstimatedTime(issue_estimated_time)) {
			req.setAttribute("issueEstimatedTimeError", "Decimal positivo.");
			isValid = false;
		} else {
			req.setAttribute("issueEstimatedTimeError", "");
		}

		return isValid;
	}

}
