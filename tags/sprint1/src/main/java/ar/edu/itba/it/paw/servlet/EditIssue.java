package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.Issue.Priority;
import ar.edu.itba.it.paw.Issue.State;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;
import ar.edu.itba.it.paw.validators.EditIssueFormValidator;

public class EditIssue extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final EditIssueFormValidator IV = EditIssueFormValidator.getInstance();
	private static final ProjectServiceImpl PS = ProjectServiceImpl.getInstance();
	private static final IssueServiceImpl IS = IssueServiceImpl.getInstance();
	private static final UserServiceImpl US = UserServiceImpl.getInstance();
	private static final PermissionManager permissionManager = PermissionManager.getInstance();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		String project_id				= req.getParameter("projectId");
		String issue_id					= req.getParameter("issueId");
		String issue_name				= req.getParameter("issueName");
		String issue_description		= req.getParameter("issueDescription");
		String issue_assigned_user_name	= req.getParameter("issueAssignedUser");
		String issue_estimated_time		= req.getParameter("issueEstimatedTime");
		String issue_priority			= req.getParameter("issuePriority");

		if (!IV.validate(req, resp)) {
			req.setAttribute("projectId", req.getParameter("projectId"));
			req.setAttribute("projectName", PS.getProject(Integer.valueOf(req.getParameter("projectId"))).getName());
			req.setAttribute("originalIssueName", req.getParameter("originalIssueName"));
			req.setAttribute("issueId", req.getParameter("issueId"));
			req.setAttribute("issueCode", req.getParameter("issueCode"));
			req.setAttribute("issueName", issue_name);
			req.setAttribute("issueDescription", issue_description);
			req.setAttribute("issueAssignedUser", issue_assigned_user_name);
			req.setAttribute("issueReportedUser", req.getParameter("issueReportedUser"));
			req.setAttribute("issueEstimatedTime", issue_estimated_time);
			req.setAttribute("issuePriority", issue_priority);
			req.setAttribute("priorityStates", Issue.Priority.values());
			req.getRequestDispatcher("editIssue.jsp").forward(req, resp);
		} else {
			Issue i						= IS.getIssue(Integer.valueOf(issue_id));
			Project p					= i.getProject();
			User issueReportedUser		= i.getReportedUser();
			User issueAssignedUser		= US.getUser(issue_assigned_user_name);
			float issueEstimatedTime	= (issue_estimated_time.equals("")) ? -1 : Float.valueOf(issue_estimated_time);
			State issueState			= i.getState();
			Priority issuePriority		= Issue.Priority.valueOf(issue_priority);
			
			Issue new_issue = new Issue(p, issue_name, issue_description, i.getCreationDate(), issueEstimatedTime, issueAssignedUser, issueReportedUser, issueState, issuePriority);
			new_issue.setId(Integer.valueOf(issue_id));
			IS.updateIssue(issueReportedUser, new_issue);
			
			req.getRequestDispatcher("project?id=" + project_id).forward(req, resp);
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser((String) req.getSession().getAttribute("name"));
		if (!permissionManager.canEditIssue(source)) {
			resp.sendRedirect("issue?id=" + req.getParameter("i_id"));
			return;
		}
		
		Issue i = IS.getIssue(Integer.valueOf(req.getParameter("i_id")));
		
		req.setAttribute("priorityStates", Issue.Priority.values());
		req.setAttribute("projectId", req.getParameter("p_id"));
		req.setAttribute("projectName", PS.getProject(Integer.valueOf(req.getParameter("p_id"))).getName());
		req.setAttribute("originalIssueName", i.getTitle());
		req.setAttribute("issueId", req.getParameter("i_id"));
		req.setAttribute("issueCode", i.getCode());
		req.setAttribute("issueName", i.getTitle());
		req.setAttribute("issueDescription", i.getDescription());
		req.setAttribute("issueAssignedUser", (i.getAssignedUser() == null) ? "" : i.getAssignedUser().getName());
		req.setAttribute("issueReportedUser", i.getReportedUser().getName());
		req.setAttribute("issueEstimatedTime", (i.getEstimatedTime() <= 0) ? "" : i.getEstimatedTime());
		req.setAttribute("issuePriority", i.getPriority().name());
		req.setAttribute("issueNameError", "");
		req.setAttribute("issueDescriptionError", "");
		req.setAttribute("issueAssignedUserError", "");
		req.setAttribute("issueEstimatedTimeError", "");
		req.setAttribute("issuePriorityError", "");

		req.getRequestDispatcher("editIssue.jsp").forward(req, resp);
	}
}
