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
import ar.edu.itba.it.paw.service.IssueService;
import ar.edu.itba.it.paw.web.command.WorkForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.WorkFormValidator;

@Controller
public class WorkController {
	
	private IssueService issueService;
	private WorkFormValidator workFormValidator;
	private TimeFormatter timeFormatter;
	
	@Autowired
	public WorkController(IssueService issueService,
			WorkFormValidator workFormValidator, TimeFormatter timeFormatter) {
		this.issueService = issueService;
		this.workFormValidator = workFormValidator;
		this.timeFormatter = timeFormatter;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getSession().getAttribute("user");
		ModelAndView mav = new ModelAndView();
		SortedMap<Work, String> works = new TreeMap<Work, String>();
		for(Work work: issueService.getWorks(issue))
			works.put(work, timeFormatter.print(work.getDedicatedTime(), null));
		
		mav.addObject("canAddWork", issueService.canAddWork(user, issue));
		mav.addObject("canEditWork", issueService.canAddWork(user, issue));
		mav.addObject("works", works);
		mav.addObject("worksQuant", works.size());
		mav.addObject("issue", issue);

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getSession().getAttribute("user");
		if (!issueService.canAddWork(user, issue))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		WorkForm workForm = new WorkForm();
		workForm.setIssue(issue);
		mav.addObject("workForm", workForm);
		mav.addObject("issue", issue);

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(HttpServletRequest req, WorkForm form, Errors errors) {
		workFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User user = (User) req.getSession().getAttribute("user");
			Work work = form.build(user);
			issueService.updateWork(user, work);
			return "redirect:../work/list?code=" + form.getIssue().getCode();
		} else {
			req.setAttribute("issue", form.getIssue());
			return "work/add";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req,
			@RequestParam("id") Work work) {
		User user = (User) req.getSession().getAttribute("user");
		if (!issueService.canEditWork(user, work.getIssue()))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		WorkForm workForm = new WorkForm();
		workForm.setIssue(work.getIssue());
		workForm.setDedicatedTime(work.getDedicatedTime());
		workForm.setDescription(work.getDescription());
		workForm.setOriginalWork(work);
		mav.addObject("workForm", workForm);
		mav.addObject("issue", work.getIssue());

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String edit(HttpServletRequest req, WorkForm form, Errors errors) {
		workFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User user = (User) req.getSession().getAttribute("user");
			form.update();			
			issueService.updateWork(user, form.getOriginalWork());
			return "redirect:../work/list?code=" + form.getIssue().getCode();
		} else {
			req.setAttribute("issue", form.getIssue());
			return "work/edit";
		}
	}

}
