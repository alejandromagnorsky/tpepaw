package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.UserRepo;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class ProjectUsersPage extends BasePage {

	@SpringBean
	private UserRepo userRepo;

	private EntityModel<Project> projectModel;

	public ProjectUsersPage(Project project) {
		projectModel = new EntityModel<Project>(Project.class, project);

		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));

		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				return new ArrayList<User>(projectModel.getObject().getUsers());
			}
		};

		IModel<List<User>> candidatesModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				List<User> result = new ArrayList<User>(projectModel
						.getObject().getUsers());
				result.addAll(userRepo.getValidUsers());
				result.remove(projectModel.getObject().getLeader());
				return result;
			}
		};

		ChoiceRenderer<User> choiceRenderer = new ChoiceRenderer<User>("name",
				"name");
		final Palette<User> usersPalette = new Palette<User>("users",
				usersModel, candidatesModel, choiceRenderer, 10, false);

		Form<ProjectUsersPage> form = new Form<ProjectUsersPage>("userForm",
				new CompoundPropertyModel<ProjectUsersPage>(this)) {
			@Override
			protected void onSubmit() {
				Iterator<User> users = usersPalette.getSelectedChoices();

				Project project = projectModel.getObject();
				User source = WicketSession.get().getUser();

				// Add new user
				while (users.hasNext()) {
					User user = users.next();
					if (!project.getUsers().contains(user)
							&& project.canAddUser(source))
						project.addUser(source, user);
				}

				// Remove old users
				Iterator<User> commonUsers = usersPalette
						.getUnselectedChoices();
				while (commonUsers.hasNext()) {
					User commonUser = commonUsers.next();
					if (project.getUsers().contains(commonUser)
							&& project.canRemoveUser(source))
						project.removeUser(source, commonUser);
				}

				setResponsePage(new ProjectViewPage(projectModel.getObject()));
			}
		};

		form.add(usersPalette);
		form.add(new Button("accept", new ResourceModel("accept")));
		add(form);
	}
}
