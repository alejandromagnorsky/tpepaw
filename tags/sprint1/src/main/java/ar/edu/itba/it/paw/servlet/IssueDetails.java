package ar.edu.itba.it.paw.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.Issue.Resolution;
import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class IssueDetails extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int issueId = Integer.valueOf(req.getParameter("id"));
		String op = req.getParameter("operation");
		String resolution = req.getParameter("resolution");

		HttpSession session = req.getSession();
		String username = (String) session.getAttribute("name");

		User user = UserServiceImpl.getInstance().getUser(username);
		Issue issue = IssueServiceImpl.getInstance().getIssue(issueId);

		if (op != null && issue != null && user != null) {
			if (op.equals("open")) {
				IssueServiceImpl.getInstance().markIssueAsOpen(user, issue);
			} else if (op.equals("ongoing")) {
				IssueServiceImpl.getInstance().markIssueAsOngoing(user, issue);
			} else if (op.equals("close")) {
				IssueServiceImpl.getInstance().markIssueAsClosed(user, issue);
			} else if (op.equals("resolve") && resolution != null) {
				Resolution r = Resolution.valueOf(resolution);
				IssueServiceImpl.getInstance().setResolutionAs(user, issue, r);
			}
		}
		resp.sendRedirect("issue?id=" + issueId);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int issueId = Integer.valueOf(req.getParameter("id"));
		Issue issue = IssueServiceImpl.getInstance().getIssue(issueId);
		Project project = issue.getProject();
		HttpSession session = ((HttpServletRequest) req).getSession();

		if (issue.getResolution() != null)
			req.setAttribute("hasResolution", true);
		
		// No other way to tell jsp of data...
		if (session.getAttribute("logged") != null) {
			String name = (String) session.getAttribute("name");
			User user = UserServiceImpl.getInstance().getUser(name);

			req.setAttribute("canCloseIssue",
					permissionManager.canCloseIssue(user, project, issue));
			req.setAttribute("canMarkIssueAsOpen",
					permissionManager.canMarkIssueAsOpen(user, issue));
			req.setAttribute("canMarkIssueAsOngoing",
					permissionManager.canMarkIssueAsOngoing(user, issue));
			req.setAttribute("canResolveIssue",
					permissionManager.canResolveIssue(user, issue));
			req.setAttribute("canAssignIssue",
					permissionManager.canAssignIssue(user, issue));

			HashMap<String, String> tmp = new HashMap<String, String>();
			for (Issue.Resolution r : Issue.Resolution.values())
				tmp.put(r.name(), r.toString());
			req.setAttribute("resolutionStates", tmp.entrySet());

		}

		req.setAttribute("project", project);
		req.setAttribute("issue", issue);
		req.getRequestDispatcher("issue.jsp").forward(req, resp);

	}
}
