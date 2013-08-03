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

@SuppressWarnings("serial")
public class EditVersionPage extends BasePage {

	private transient String name;
	private transient String description;
	private transient DateTime releaseDate;
	public transient VersionState state;

	public EditVersionPage(Version version) {
		Project project = WicketSession.get().getProject();
		final EntityModel<Project> projectModel = new EntityModel<Project>(
				Project.class, project);
		final EntityModel<Version> versionModel = new EntityModel<Version>(
				Version.class, version);
		setDefaultModel(versionModel);

		name = version.getName();
		description = version.getDescription();
		state = version.getState();
		releaseDate = version.getReleaseDate();

		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));

		Form<EditVersionPage> form = new Form<EditVersionPage>(
				"editVersionForm", new CompoundPropertyModel<EditVersionPage>(
						this)) {
			@Override
			public void onSubmit() {
				Version v = versionModel.getObject();

				if (!name.equals(v.getName()))
					for(Version version: projectModel.getObject().getVersions())
						if(version.getName().equals(name))
							error(getString("VersionExists"));

				if (!hasError()) {

					v.setName(name);
					v.setDescription(description);
					v.setReleaseDate(releaseDate);
					v.setState(state);
					setResponsePage(new VersionListPage(
							projectModel.getObject()));
				}
			}
		};

		form.add(new VersionFormPanel("versionFormPanel"));
		form.add(new Button("editVersion", new ResourceModel("edit")));

		add(form);
	}
}
