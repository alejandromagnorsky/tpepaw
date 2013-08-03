package ar.edu.itba.it.paw.web.page.issue;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.EntityModel;

public class IssueListPage extends BasePage {
	
	private final EntityModel<Project> projectModel;

	public IssueListPage(Project project) {
		projectModel = new EntityModel<Project>(Project.class, project);	

		add(new IssueListPanel("issueListPanel", projectModel, true));
	}
}
