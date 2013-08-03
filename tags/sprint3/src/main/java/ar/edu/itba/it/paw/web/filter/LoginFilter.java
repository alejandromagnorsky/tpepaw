package ar.edu.itba.it.paw.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.repo.UserRepo;

@Component
public class LoginFilter extends OncePerRequestFilter {

	private UserRepo userRepo;
	
	@Autowired
	public LoginFilter(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public void destroy() {
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		HttpSession session = ((HttpServletRequest) req).getSession();
		String uri = ((HttpServletRequest) req).getRequestURI();

		Integer userId = (Integer) session.getAttribute("userId");
		User user = (userId == null) ? null : userRepo.get(userId);
		req.setAttribute("user", user);
			
		if (user != null) {
			req.setAttribute("username", user.getName());
			req.setAttribute("fullname", user.getFullname());
			req.setAttribute("canAddProject", user.canAddProject());
			req.setAttribute("canEditProject", user.canEditProject());
			req.setAttribute("canInvalidateUser", user.canInvalidateUser());
			req.setAttribute("canRegister", user.canRegisterUser());
		}

		String context = uri.substring(0, uri.indexOf('/', 1) + 1);
		if (user != null) {	
			// Must be logged and must have permissions
			if  (
			   (uri.contains("bin/project/add") && !user.canAddProject())
			|| (uri.contains("bin/project/edit") && !user.canEditProject())
						
			|| (uri.contains("bin/user/invalidate") && !user.canInvalidateUser())
			|| (uri.contains("bin/user/register") && !user.canRegisterUser())
			){
				((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
				return;
			}	
		}
		
		chain.doFilter(req, resp);		
	}

}
