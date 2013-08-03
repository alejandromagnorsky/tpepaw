package ar.edu.itba.it.paw.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.repo.UserRepo;
import ar.edu.itba.it.paw.web.command.InvalidateUserForm;
import ar.edu.itba.it.paw.web.command.LoginForm;
import ar.edu.itba.it.paw.web.command.RegisterForm;
import ar.edu.itba.it.paw.web.validator.InvalidateUserFormValidator;
import ar.edu.itba.it.paw.web.validator.LoginFormValidator;
import ar.edu.itba.it.paw.web.validator.RegisterFormValidator;

@Controller
public class UserController {

	private UserRepo userRepo;
	private LoginFormValidator loginFormValidator;
	private RegisterFormValidator registerFormValidator;
	private InvalidateUserFormValidator invalidateUserFormValidator;

	@Autowired
	public UserController(UserRepo userRepo,
			LoginFormValidator loginFormValidator,
			RegisterFormValidator registerFormValidator,
			InvalidateUserFormValidator invalidateUserFormValidator) {
		this.userRepo = userRepo;
		this.loginFormValidator = loginFormValidator;
		this.registerFormValidator = registerFormValidator;
		this.invalidateUserFormValidator = invalidateUserFormValidator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView invalidate(HttpServletRequest req) {
		User source = (User) req.getAttribute("user");
		
		ModelAndView mav = new ModelAndView();
		InvalidateUserForm invalidateUserForm = new InvalidateUserForm();
		invalidateUserForm.setSource(source);

		List<User> users = userRepo.getValidUsers();
		
		mav.addObject("users", users);
		mav.addObject("invalidateUserForm", invalidateUserForm);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView invalidate(HttpServletRequest req, InvalidateUserForm form,
			Errors errors) {
		invalidateUserFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User source = (User) req.getAttribute("user");
			User target = form.getTarget();
			target.invalidate(source);
			return new ModelAndView(new RedirectView("../project/view"));
		} else {
			ModelAndView mav = new ModelAndView();
			List<User> users = userRepo.getValidUsers();
			mav.addObject("users", users);
			return mav;
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("loginForm", new LoginForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String login(HttpServletRequest req, LoginForm form, Errors errors) {

		loginFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			HttpSession session = req.getSession();
			session.setAttribute("userId", userRepo.get(form.getUsername()).getId());
			return "redirect:../project/view";
		} else
			return "user/login";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.invalidate();

		return new ModelAndView(new RedirectView("../project/view"));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView register(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("registerForm", new RegisterForm());

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String register(HttpServletRequest req, RegisterForm form,
			Errors errors) {

		registerFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User source = (User) req.getAttribute("user");
			User target = form.buildUser();
			userRepo.add(source, target);
			return "redirect:../project/view";
		} else {
			return "user/register";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView help() {
		ModelAndView mav = new ModelAndView();
		return mav;
	}
}
