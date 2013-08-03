package ar.edu.itba.it.paw.web.page.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.page.issue.IssueViewPage;

@SuppressWarnings("serial")
public class UserProfilePage extends BasePage {

	public UserProfilePage(User user) {
		User source = WicketSession.get().getUser();
		final EntityModel<User> userModel = new EntityModel<User>(User.class, user);

		Link<Void> editUserLink = new Link<Void>("editUser") {
			@Override
			public void onClick() {
				setResponsePage(new EditUserPage(userModel.getObject()));
			}
		};
		if (source == null || !source.canEditUser(user))
			editUserLink.setVisible(false);
		add(editUserLink);

		add(new Label("rank", new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				return userModel.getObject().getAdmin() ? new ResourceModel("adminUser").getObject() : new ResourceModel("commonUser").getObject();
			}
		}));
		add(new Label("username", new PropertyModel<String>(userModel, "name")));
		add(new Label("fullname", new PropertyModel<String>(userModel, "fullname")));
		add(new Label("email", new PropertyModel<String>(userModel, "email")));
		Label invalidated = new Label("invalidated",
				new LoadableDetachableModel<String>() {
					@Override
					protected String load() {
						return userModel.getObject().getValid() ? "" : new ResourceModel("invalidatedUser").getObject();
					}
				});
		invalidated.add(new SimpleAttributeModifier("style", "color: red;"));
		add(invalidated);

		add(new RefreshingView<Issue>("followingIssues") {

			@Override
			protected Iterator<IModel<Issue>> getItemModels() {
				List<IModel<Issue>> result = new ArrayList<IModel<Issue>>();

				for (Issue i : userModel.getObject().getFollowingIssues())
					result.add(new EntityModel<Issue>(Issue.class, i));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<Issue> item) {
				Link<Issue> link = new Link<Issue>("link", item.getModel()) {
					@Override
					public void onClick() {
						setResponsePage(new IssueViewPage(getModelObject()));
					}
				};
				link.add(new Label("title", new PropertyModel<String>(item.getModel(), "title")));
				item.add(link);
			}
		});
	}

}
