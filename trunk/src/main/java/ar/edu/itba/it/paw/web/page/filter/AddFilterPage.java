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
public class AddFilterPage extends BasePage {

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
	
	public AddFilterPage(){
		Project project = WicketSession.get().getProject();
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, project);
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));
		
		if (WicketSession.get().logged())
			action = Action.Save;
		else {
			name = getString("anonymousFilter");
			action = Action.Apply;
		}
		
		Form<Filter> form = new Form<Filter>("addFilterForm", new CompoundPropertyModel<Filter>(this));
		form.add(new FilterFormPanel("filterFormPanel", projectModel));
		form.add(new Button("addFilter", new ResourceModel("add")){
			@Override
			public void onSubmit(){
				User source = WicketSession.get().getUser();
				Project project = projectModel.getObject();
				if(project.getFilter(name, source) != null){
					error(getString("ExistingFilter"));
					return;
				}
				if(dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)){
					error(getString("InvalidDateInterval"));
					return;
				}
				
				Filter filter = new Filter(	name, source, project, issueCode, issueTitle, issueDescription,
											issueReportedUser, issueAssignedUser, issueType, issueState,
											issueResolution, affectedVersion, fixedVersion,
											dateFrom, dateTo);
				
				if (action.equals(Action.Save)){
					project.addFilter(source, filter);
					setResponsePage(new FilterListPage(project));
				} else if (action.equals(Action.Apply)){
					WicketSession.get().addFilter(filter);
					setResponsePage(new ProjectViewPage(project));
				} else {
					project.addFilter(source, filter);
					WicketSession.get().addFilter(filter);
					setResponsePage(new ProjectViewPage(project));
				}
			}
		});
		
		add(form);
	}
	
}
