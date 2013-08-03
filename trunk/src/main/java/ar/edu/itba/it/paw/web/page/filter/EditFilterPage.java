package ar.edu.itba.it.paw.web.page.filter;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.page.filter.ActionPanel.Action;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class EditFilterPage extends BasePage {
	
	public transient String name;
	public transient String issueCode;
	public transient String issueTitle;
	public transient String issueDescription;
	public transient User issueReportedUser;
	public transient User issueAssignedUser;
	public transient Type issueType;
	public transient State issueState;
	public transient Resolution issueResolution;
	public transient Version affectedVersion;
	public transient Version fixedVersion;
	public transient DateTime dateFrom;
	public transient DateTime dateTo;
	public transient Action action;
	
	public EditFilterPage(Filter filter){
		Project project = WicketSession.get().getProject();
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, project);
		final EntityModel<Filter> filterModel = new EntityModel<Filter>(Filter.class, filter);
		initForm(filter);
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));
		setDefaultModel(filterModel);
		
		Form<Filter> form = new Form<Filter>("editFilterForm", new CompoundPropertyModel<Filter>(this));
		form.add(new FilterFormPanel("filterFormPanel", projectModel));
		form.add(new Button("editFilter", new ResourceModel("edit")){
			@Override
			public void onSubmit(){
				User source = WicketSession.get().getUser();
				Project project = projectModel.getObject();
				Filter originalFilter = filterModel.getObject();
				
				if(!originalFilter.getName().equals(name) && project.getFilter(name, source) != null){
					error(getString("ExistingFilter"));
					return;
				}
				if(dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)){
					error(getString("InvalidDateInterval"));
					return;
				}
				
				if (action.equals(Action.Save)){
					updateFilter(originalFilter);
					setResponsePage(new FilterListPage(project));
				} else if (action.equals(Action.Apply)){
					WicketSession.get().addFilter(originalFilter);
					setResponsePage(new ProjectViewPage(project));
				} else {
					updateFilter(originalFilter);
					WicketSession.get().addFilter(originalFilter);
					setResponsePage(new ProjectViewPage(project));
				}
			}
		});
		
		add(form);
	}
	
	private void initForm(Filter filter){
		name = filter.getName();
		issueCode = filter.getIssueCode();
		issueTitle = filter.getIssueTitle();
		issueDescription = filter.getIssueDescription();
		issueReportedUser = filter.getIssueReportedUser();
		issueAssignedUser = filter.getIssueAssignedUser();
		issueType = filter.getIssueType();
		issueState = filter.getIssueState();
		issueResolution = filter.getIssueResolution();
		affectedVersion = filter.getAffectedVersion();
		fixedVersion = filter.getFixedVersion();
		dateFrom = filter.getDateFrom();
		dateTo = filter.getDateTo();
		action = Action.Save;
	}
	
	private void updateFilter(Filter originalFilter){
		originalFilter.setName(name);
		originalFilter.setIssueCode(issueCode);
		originalFilter.setIssueTitle(issueTitle);
		originalFilter.setIssueDescription(issueDescription);					
		originalFilter.setIssueReportedUser(issueReportedUser);				
		originalFilter.setIssueAssignedUser(issueAssignedUser);				
		originalFilter.setIssueType(issueType);				
		originalFilter.setIssueState(issueState);				
		originalFilter.setIssueResolution(issueResolution);				
		originalFilter.setAffectedVersion(affectedVersion);		
		originalFilter.setFixedVersion(fixedVersion);
		originalFilter.setDateFrom(dateFrom);
		originalFilter.setDateTo(dateTo);
	}
}
