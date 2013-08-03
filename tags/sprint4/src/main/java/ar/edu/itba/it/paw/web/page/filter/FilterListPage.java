package ar.edu.itba.it.paw.web.page.filter;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.EntityModel;

public class FilterListPage extends BasePage {

	private final EntityModel<Project> projectModel;

	public FilterListPage(Project project) {
		projectModel = new EntityModel<Project>(Project.class, project);	

		add(new FilterListPanel("filterListPanel", projectModel, true));
	}
}
