package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.page.HelpPage;
import ar.edu.itba.it.paw.web.page.user.InvalidateUserPage;
import ar.edu.itba.it.paw.web.page.user.LoginPage;
import ar.edu.itba.it.paw.web.page.user.RegisterPage;
import ar.edu.itba.it.paw.web.page.user.UserProfilePage;

@SuppressWarnings("serial")
public class BasePage extends WebPage {

	public BasePage() {
		Link<Void> homeLink = new Link<Void>("home") {
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}
		};
		homeLink.add(new Label("header", ""));
		add(homeLink);

		add(new Link<Void>("help") {
			@Override
			public void onClick() {
				setResponsePage(HelpPage.class);
			}
		});

		final User source = WicketSession.get().getUser();

		Label usernameLabel = new Label("username", new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				return WicketSession.get().logged()? WicketSession.get().getUser().getName() : new ResourceModel("visitor").getObject();
			}			
		});
		Link<Void> loginLink = new Link<Void>("login") {
			@Override
			public void onClick() {
				setResponsePage(LoginPage.class);
			}
		};
		Link<Void> logoutLink = new Link<Void>("logout") {
			@Override
			public void onClick() {
				WicketSession.get().logout();
				setResponsePage(getApplication().getHomePage());
			}
		};
		Link<Void> userProfileLink = new Link<Void>("userProfileLink") {
			@Override
			public void onClick() {
				if(WicketSession.get().logged())
					setResponsePage(new UserProfilePage(WicketSession.get().getUser()));					
			}			
		};
		if (source != null)
			loginLink.setVisible(false);
		else
			logoutLink.setVisible(false);
		userProfileLink.add(usernameLabel);
		add(userProfileLink);
		add(loginLink);
		add(logoutLink);

		Link<Void> registerLink = new Link<Void>("register") {
			@Override
			public void onClick() {
				setResponsePage(RegisterPage.class);
			}
		};
		if (source == null || !source.canRegisterUser())
			registerLink.setVisible(false);
		add(registerLink);

		Link<Void> invalidateUserLink = new Link<Void>("invalidate") {
			@Override
			public void onClick() {
				setResponsePage(InvalidateUserPage.class);
			}
		};
		if (source == null || !source.canInvalidateUser())
			invalidateUserLink.setVisible(false);
		add(invalidateUserLink);
	}

}
