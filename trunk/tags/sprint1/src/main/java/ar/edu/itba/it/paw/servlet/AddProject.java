package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;
import ar.edu.itba.it.paw.validators.AddProjectFormValidator;

public class AddProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final AddProjectFormValidator PV = AddProjectFormValidator.getInstance();
	private static final ProjectServiceImpl PS = ProjectServiceImpl.getInstance();
	private static final UserServiceImpl US = UserServiceImpl.getInstance();
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser((String) req.getSession().getAttribute("name"));
		String project_name			= req.getParameter("projectName");
		String project_description	= req.getParameter("projectDescription");
		String project_code			= req.getParameter("projectCode");
		String project_leader_name	= req.getParameter("projectLeader");
		User project_leader			= US.getUser(project_leader_name);
		Boolean project_visibility	= Boolean.valueOf(req.getParameter("projectVisibility").equals("public"));

		if (!PV.validate(req, resp)) {			
			req.setAttribute("projectName", project_name);
			req.setAttribute("projectCode", project_code);
			req.setAttribute("projectDescription", project_description);
			req.setAttribute("projectLeader", project_leader_name);
			req.setAttribute("projectVisibility", project_visibility);
			req.getRequestDispatcher("addProject.jsp").forward(req, resp);
		} else {			
			Project p = new Project(project_code, project_name, project_description, project_leader, project_visibility);
			PS.addProject(source, p);
			req.getRequestDispatcher("projects").forward(req, resp);
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser((String) req.getSession().getAttribute("name"));
		if (!permissionManager.canAddProject(source)) {
			resp.sendRedirect("projects");
			return;
		}
		
		req.setAttribute("projectNameError", "");
		req.setAttribute("projectDescriptionError", "");
		req.setAttribute("projectCodeError", "");
		req.setAttribute("projectLeaderError", "");
		req.setAttribute("projectVisibilityError", "");
		req.setAttribute("projectName", "");
		req.setAttribute("projectDescription", "");
		req.setAttribute("projectCode", "");
		req.setAttribute("projectLeader", "");
		req.setAttribute("projectVisibility", false);

		req.getRequestDispatcher("addProject.jsp").forward(req, resp);
	}
}
