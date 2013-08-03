package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.Issue.Priority;
import ar.edu.itba.it.paw.Issue.State;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;
import ar.edu.itba.it.paw.validators.AddIssueFormValidator;

public class AddIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final AddIssueFormValidator IV = AddIssueFormValidator.getInstance();
	private static final ProjectServiceImpl PS = ProjectServiceImpl.getInstance();
	private static final IssueServiceImpl IS = IssueServiceImpl.getInstance();
	private static final UserServiceImpl US = UserServiceImpl.getInstance();
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		String project_id				= req.getParameter("projectId");
		String issue_reported_user		= req.getParameter("issueReportedUser");
		String issue_name				= req.getParameter("issueName");
		String issue_description		= req.getParameter("issueDescription");
		String issue_assigned_user_name	= req.getParameter("issueAssignedUser");
		String issue_estimated_time		= req.getParameter("issueEstimatedTime");
		String issue_priority			= req.getParameter("issuePriority");

		if (!IV.validate(req, resp)) {
			req.setAttribute("projectId", project_id);
			req.setAttribute("projectName", req.getParameter("projectName"));
			req.setAttribute("issueReportedUser", issue_reported_user);
			req.setAttribute("issueName", issue_name);
			req.setAttribute("issueDescription", issue_description);
			req.setAttribute("issueAssignedUser", issue_assigned_user_name);
			req.setAttribute("issueEstimatedTime", issue_estimated_time);
			req.setAttribute("issuePriority", issue_priority);
			req.setAttribute("priorityStates", Issue.Priority.values());
			req.getRequestDispatcher("addIssue.jsp").forward(req, resp);
		} else {
			Project p					= PS.getProject(Integer.valueOf(project_id));
			User reportedUser			= US.getUser(issue_reported_user);
			User issueUser				= US.getUser(issue_assigned_user_name);
			float issueEstimatedTime	= (issue_estimated_time.equals("")) ? -1 : Float.valueOf(issue_estimated_time);
			State issueState			= State.Open;
			Priority issuePriority		= Issue.Priority.valueOf(issue_priority);
			
			Issue i = new Issue(p, issue_name, issue_description, new DateTime(), issueEstimatedTime, issueUser, reportedUser, issueState, issuePriority);
			IS.addIssue(reportedUser, i);
			
			req.getRequestDispatcher("project?id=" + project_id).forward(req, resp);
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser((String) req.getSession().getAttribute("name"));
		if (!permissionManager.canAddIssue(source)) {
			resp.sendRedirect("project?id=" + req.getParameter("id"));
			return;
		}
		
		req.setAttribute("priorityStates", Issue.Priority.values());
		req.setAttribute("projectId", req.getParameter("id"));
		req.setAttribute("projectName", PS.getProject(Integer.valueOf(req.getParameter("id"))).getName());
		req.setAttribute("issueReportedUser", req.getParameter("reported_user"));
		req.setAttribute("issueName", "");
		req.setAttribute("issueDescription", "");
		req.setAttribute("issueAssignedUser", "");
		req.setAttribute("issueEstimatedTime", "");
		req.setAttribute("issuePriority", Issue.Priority.Normal.name());
		req.setAttribute("issueNameError", "");
		req.setAttribute("issueDescriptionError", "");
		req.setAttribute("issueUserError", "");
		req.setAttribute("issueEstimatedTimeError", "");
		req.setAttribute("issuePriorityError", "");

		req.getRequestDispatcher("addIssue.jsp").forward(req, resp);
	}
}
