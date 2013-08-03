package ar.edu.itba.it.paw.web.page.version;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.EntityModel;

public class VersionListPage extends BasePage {

	public VersionListPage(Project project) {
		add(new VersionListPanel("versionListPanel", new EntityModel<Project>(Project.class, project), true));
	}	
}
