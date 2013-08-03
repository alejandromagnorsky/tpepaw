package ar.edu.itba.it.paw.web.page.user;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.UserRepo;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;

@SuppressWarnings("serial")
public class LoginPage extends BasePage {

	@SpringBean
	private transient UserRepo userRepo;

	private transient String username;
	private transient String password;

	public LoginPage() {		
		Form<LoginPage> form = new Form<LoginPage>("loginForm", new CompoundPropertyModel<LoginPage>(this)) {
			@Override
			protected void onSubmit() {
				WicketSession session = WicketSession.get();
				if (session.login(username, password, userRepo) && !continueToOriginalDestination())
					setResponsePage(getApplication().getHomePage());
				else
					error(getString("InvalidLogin"));
			}
		};

		form.add(new RequiredTextField<String>("username"));
		form.add(new PasswordTextField("password").setRequired(true));
		form.add(new Button("login", new ResourceModel("login")));
		form.add(new FeedbackPanel("feedback"));
		add(form);
	}

}
