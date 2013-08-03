package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class LoginFilter implements Filter {

	private static final PermissionManager permissionManager = PermissionManager
			.getInstance();

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) req).getSession();
		String uri = ((HttpServletRequest) req).getRequestURI();

		// Prepare common user data for jsps...
		if (session.getAttribute("logged") != null) {
			String name = (String) session.getAttribute("name");
			User user = UserServiceImpl.getInstance().getUser(name);

			req.setAttribute("logged", true);
			req.setAttribute("username", name);
			req.setAttribute("fullname", UserServiceImpl.getInstance().getUser(
					name).getFullname());

			req.setAttribute("canAddProject", permissionManager
					.canAddProject(user));
			req.setAttribute("canEditProject", permissionManager
					.canEditProject(user));
			req
					.setAttribute("canAddIssue", permissionManager
							.canAddIssue(user));
			req.setAttribute("canEditIssue", permissionManager
					.canEditIssue(user));
			req.setAttribute("canMarkIssue", permissionManager
					.canMarkIssue(user));
			req.setAttribute("canInvalidateUser", permissionManager
					.canInvalidateUser(user));
			req
					.setAttribute("canRegister", permissionManager
							.canRegister(user));
		} else {
			req.setAttribute("logged", false);
		}

		// Filter
		if (uri.equals("/tpepaw/projects") || uri.equals("/tpepaw/login")
				|| uri.equals("/tpepaw/help.jsp") || uri.contains("css")
				|| uri.contains("resources")
				|| (session.getAttribute("logged") != null))
			chain.doFilter(req, resp);
		else if (uri.equals("/tpepaw/project")) {
			int projectId = Integer.valueOf(req.getParameter("id"));
			if (ProjectServiceImpl.getInstance().getProject(projectId)
					.getIsPublic())
				chain.doFilter(req, resp);
			else
				((HttpServletResponse) resp).sendRedirect("/tpepaw/projects");
		} else if (uri.equals("/tpepaw/issue")) {
			int issueId = Integer.valueOf(req.getParameter("id"));
			if (IssueServiceImpl.getInstance().getIssue(issueId).getProject()
					.getIsPublic())
				chain.doFilter(req, resp);
			else
				((HttpServletResponse) resp).sendRedirect("/tpepaw/projects");
		} else
			((HttpServletResponse) resp).sendRedirect("/tpepaw/projects");
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
