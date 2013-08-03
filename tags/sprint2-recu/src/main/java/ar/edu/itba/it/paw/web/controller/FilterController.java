package ar.edu.itba.it.paw.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.FilterForm;
import ar.edu.itba.it.paw.web.validator.FilterFormValidator;

@Controller
public class FilterController {
	
	//private static final User DEFAULT_USER = new User(false, false, "Cualquiera", "Satan", "666");
	
	private UserService userService;
	private FilterFormValidator filterFormValidator;
	
	@Autowired
	public FilterController(	UserService userService,
								FilterFormValidator filterFormValidator) {
		this.userService = userService;
		this.filterFormValidator = filterFormValidator;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		User user = (User) req.getSession().getAttribute("user");
		Project project = (Project) req.getSession().getAttribute("project");

		ModelAndView mav = new ModelAndView();
		FilterForm filterForm = new FilterForm();
		filterForm.setUser(user);
		filterForm.setProject(project);
		mav.addObject("filterForm", filterForm);
		mav.addObject("projectName", project.getName());
		mav.addObject("stateList", State.values());
		mav.addObject("resolutionList", Resolution.values());
		mav.addObject("userList", userService.getValidUsers());
		
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String add(HttpServletRequest req, FilterForm form, Errors errors) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");
		
		filterFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			session.setAttribute("filter", form.build());
			return "redirect:../issue/list?code=" + project.getCode();
		} else {
			req.setAttribute("projectName", project.getName());
			req.setAttribute("stateList", State.values());
			req.setAttribute("resolutionList", Resolution.values());
			req.setAttribute("userList", userService.getValidUsers());
			return "filter/add";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String remove(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");
		
		session.setAttribute("filter", null);
		return "redirect:../issue/list?code=" + project.getCode();
	}
}
