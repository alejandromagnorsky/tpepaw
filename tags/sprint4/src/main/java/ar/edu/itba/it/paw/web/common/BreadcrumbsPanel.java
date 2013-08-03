package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.page.issue.IssueViewPage;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class BreadcrumbsPanel extends Panel {
	
	public BreadcrumbsPanel(String id, IModel<Project> projectModel, IModel<Issue> issueModel){
		super(id);
		constructBreadcrumbs(projectModel, issueModel);
	}
	
	public BreadcrumbsPanel(String id, IModel<Project> projectModel){
		super(id);
		constructBreadcrumbs(projectModel, null);
		
	}
	
	private void constructBreadcrumbs(final IModel<Project> projectModel, final IModel<Issue> issueModel){
		Link<Project> pb = new Link<Project>("projectBreadcrumb", projectModel){
			@Override
			public void onClick() {
				setResponsePage(new ProjectViewPage(projectModel.getObject()));
			}			
		};
		Link<Issue> ib = new Link<Issue>("issueBreadcrumb", issueModel){
			@Override
			public void onClick() {
				if(issueModel != null)
					setResponsePage(new IssueViewPage(issueModel.getObject()));
			}			
		};
		pb.add(new Label("projectName", new PropertyModel<String>(projectModel, "name")));
		if(issueModel != null){
			add(new Label("separator", " > "));
			ib.add(new Label("issueTitle", new PropertyModel<String>(issueModel, "title")));
		} else {
			add(new Label("separator", " > ").setVisible(false));
			ib.add(new Label("issueTitle", ""));
			ib.setVisible(false);
		}
		add(pb);
		add(ib);
	}
}
