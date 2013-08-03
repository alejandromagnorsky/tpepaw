package ar.edu.itba.it.paw.web.page.issue;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class EditWorkPage extends BasePage {
	
	public EditWorkPage(Work work){
		final EntityModel<Work> workModel = new EntityModel<Work>(Work.class, work);
		add(new BreadcrumbsPanel("breadcrumbsPanel", new EntityModel<Project>(Project.class, workModel.getObject().getIssue().getProject()), 
													 new EntityModel<Issue>(Issue.class, workModel.getObject().getIssue())));
		
		Form<Work> form = new Form<Work>("editWorkForm", new CompoundPropertyModel<Work>(workModel));
		form.add(new WorkFormPanel("workFormPanel"));
		form.add(new Button("editWork", new ResourceModel("edit")){
			@Override
			public void onSubmit(){
				setResponsePage(new WorkListPage(workModel.getObject().getIssue()));
			}
		});
		
		add(form);
	}

}
