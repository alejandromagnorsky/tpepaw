package ar.edu.itba.it.paw.web.page.version;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.model.Version.VersionState;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

public class AddVersionPage extends BasePage {

	private transient String name;
	private transient String description;
	private transient DateTime releaseDate;
	public transient VersionState state;

	private EntityModel<Project> projectModel;

	@SuppressWarnings("serial")
	public AddVersionPage() {
		Project project = WicketSession.get().getProject();
		projectModel = new EntityModel<Project>(Project.class, project);
		setDefaultModel(projectModel);

		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));

		Form<Version> form = new Form<Version>("addVersionForm",
				new CompoundPropertyModel<Version>(this)) {
			@Override
			public void onSubmit() {

				for(Version version: projectModel.getObject().getVersions())
					if(version.getName().equals(name))
						error(getString("VersionExists"));

				if (!hasError()) {
					Version version = new Version(name, description,
							releaseDate);
					version.setState(state);
					projectModel.getObject().addVersion(version);
					setResponsePage(new VersionListPage(
							projectModel.getObject()));
				}
			}
		};

		form.add(new VersionFormPanel("versionFormPanel"));
		form.add(new Button("addVersion", new ResourceModel("add")));
		add(form);
	}
}
