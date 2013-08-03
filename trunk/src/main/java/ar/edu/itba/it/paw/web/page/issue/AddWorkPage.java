package ar.edu.itba.it.paw.web.page.issue;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class AddWorkPage extends BasePage {
	
	public transient Time dedicatedTime;
	public transient String description;
	
	public AddWorkPage(Issue issue){
		final EntityModel<Issue> issueModel = new EntityModel<Issue>(Issue.class, issue);
		add(new BreadcrumbsPanel("breadcrumbsPanel", new EntityModel<Project>(Project.class, issueModel.getObject().getProject()), issueModel));
				
		Form<Work> form = new Form<Work>("addWorkForm", new CompoundPropertyModel<Work>(this));
		form.add(new WorkFormPanel("workFormPanel"));
		form.add(new Button("addWork", new ResourceModel("add")){
			@Override
			public void onSubmit(){
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();
				Work work = new Work(source, new DateTime(),  description,  dedicatedTime, issue);			
				
				issue.addWork(source, work);
				setResponsePage(new WorkListPage(issue));
			}
		});
		
		add(form);
	}

}
