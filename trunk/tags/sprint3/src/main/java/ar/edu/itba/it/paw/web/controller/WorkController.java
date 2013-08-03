package ar.edu.itba.it.paw.web.controller;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.repo.IssueRepo;
import ar.edu.itba.it.paw.web.command.WorkForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.WorkFormValidator;

@Controller
public class WorkController {

	private IssueRepo issueRepo;
	private WorkFormValidator workFormValidator;
	private TimeFormatter timeFormatter;

	@Autowired
	public WorkController(IssueRepo issueRepo, WorkFormValidator workFormValidator,
			TimeFormatter timeFormatter) {
		this.issueRepo = issueRepo;
		this.workFormValidator = workFormValidator;
		this.timeFormatter = timeFormatter;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		ModelAndView mav = new ModelAndView("work/list");
		SortedMap<Work, String> works = new TreeMap<Work, String>();
		for (Work work : issue.getWorks())
			works.put(work, timeFormatter.print(work.getDedicatedTime(), null));
	
		mav.addObject("canAddWork", issue.canAddWork(user));
		mav.addObject("canEditWork", issue.canAddWork(user));
		mav.addObject("works", works);
		mav.addObject("worksQuant", works.size());
		mav.addObject("issue", issue);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		if (!issue.canAddWork(user))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		WorkForm workForm = new WorkForm();
		workForm.setIssue(issue);
		mav.addObject("workForm", workForm);
		mav.addObject("issue", issue);

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req, WorkForm form, Errors errors) {
		workFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User user = (User) req.getAttribute("user");
			Work work = form.build(user);
			issueRepo.persist(work);
			return list(req, form.getIssue());
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("issue", form.getIssue());
			return mav;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req,
			@RequestParam("id") Work work) {
		User user = (User) req.getAttribute("user");
		if (!work.getIssue().canEditWork(user))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		WorkForm workForm = new WorkForm();
		workForm.setIssue(work.getIssue());
		workForm.setOriginalWork(work);
		workForm.setDedicatedTime(work.getDedicatedTime());
		workForm.setDescription(work.getDescription());
		mav.addObject("workForm", workForm);
		mav.addObject("issue", work.getIssue());

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView edit(HttpServletRequest req, WorkForm form, Errors errors) {
		workFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			form.update();
			return list(req, form.getIssue());
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("issue", form.getIssue());
			return mav;
		}
	}

}
