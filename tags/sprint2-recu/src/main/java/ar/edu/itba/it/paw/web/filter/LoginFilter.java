package ar.edu.itba.it.paw.web.filter;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.ProjectService;
import ar.edu.itba.it.paw.service.UserService;

@Component
public class LoginFilter implements Filter {

	private UserService userService;
	private ProjectService projectService;

	@Autowired
	public LoginFilter(	UserService userService,
						ProjectService projectService) {
		this.userService = userService;
		this.projectService = projectService;
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) req).getSession();
		String uri = ((HttpServletRequest) req).getRequestURI();

		// Prepare common user data for jsps...
		User user = (User) session.getAttribute("user");
		if (user != null) {		
			req.setAttribute("username", user.getName());
			req.setAttribute("fullname", user.getFullname());
			req.setAttribute("canAddProject", projectService.canAddProject(user));
			req.setAttribute("canEditProject", projectService.canEditProject(user));
			req.setAttribute("canInvalidateUser", userService.canInvalidateUser(user));
			req.setAttribute("canRegister", userService.canRegisterUser(user));			
		}

		String context = uri.substring(0, uri.indexOf('/', 1) + 1);
		if (
			(uri.endsWith("bin/project/add") && !projectService.canAddProject(user))
			|| (uri.contains("bin/project/edit") && !projectService.canEditProject(user))
     
			|| (uri.contains("bin/user/invalidate") && !userService.canInvalidateUser(user))
            || (uri.contains("bin/user/register") && !userService.canRegisterUser(user))
			){
			((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
			return;
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
