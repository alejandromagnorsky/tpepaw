package ar.edu.itba.it.paw.web.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.repo.IssueRepo;
import ar.edu.itba.it.paw.web.command.IssueFileForm;
import ar.edu.itba.it.paw.web.validator.IssueFileFormValidator;

@Controller
public class IssueFileController{
	
	private IssueRepo issueRepo;
	private IssueFileFormValidator issueFileFormValidator;
	
	@Autowired
	public IssueFileController(IssueRepo issueRepo, IssueFileFormValidator issueFileFormValidator){
		this.issueRepo = issueRepo;
		this.issueFileFormValidator = issueFileFormValidator;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req, IssueFileForm form, Errors errors) {
		issueFileFormValidator.validate(form, errors);
		if (!errors.hasErrors()){
			IssueFile file = form.build();
			issueRepo.persist(file);
			return list(req, form.getIssue());
		}
		return new ModelAndView();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, @RequestParam("id") IssueFile issueFile) {
		Issue issue = issueFile.getIssue();
		User user = (User) req.getAttribute("user");
		
		if (!issue.canDeleteIssueFile(user))
			return new ModelAndView(new RedirectView("../issuefile/list?code=" + issue.getCode()));
		
		issueFile.setIssue(null);
		return list(req, issue);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, @RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		
		IssueFileForm issueFileForm = new IssueFileForm();
		issueFileForm.setIssue(issue);
		issueFileForm.setUploader(user);
		ModelAndView mav = new ModelAndView("issuefile/list");
		mav.addObject("canAddIssueFile", issue.canAddIssueFile(user));
		mav.addObject("canDeleteIssueFile", issue.canDeleteIssueFile(user));
		mav.addObject("canDownloadIssueFile", issue.canDownloadIssueFile(user));
		mav.addObject("issue", issue);
		mav.addObject("files", issue.getFiles());
		mav.addObject("projectName", issue.getProject().getName());
		mav.addObject("issueFileForm", issueFileForm);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView get(HttpServletRequest req, HttpServletResponse resp, @RequestParam("id") IssueFile issueFile) {
		User user = (User) req.getAttribute("user");
		Issue issue = issueFile.getIssue();
		
		if (!issue.canDownloadIssueFile(user))
			return new ModelAndView(new RedirectView("../issuefile/list?code=" + issue.getCode()));
		
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment;filename=" + issueFile.getFilename());
		try {
			ServletOutputStream stream = resp.getOutputStream();
			stream.write(issueFile.getFile());
			stream.close();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		return null;
	}
}
