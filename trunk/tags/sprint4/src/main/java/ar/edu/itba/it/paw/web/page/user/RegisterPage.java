package ar.edu.itba.it.paw.web.page.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.User.View;
import ar.edu.itba.it.paw.model.UserRepo;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.CaptchaPanel;

@SuppressWarnings("serial")
public class RegisterPage extends BasePage {

	@SpringBean
	private transient UserRepo userRepo;

	private transient String username;
	private transient String password;
	private transient String confirmPassword;
	private transient String captchaPassword;
	private transient String fullname;
	private transient String email;
	private transient List<View> defaultViews;

	public RegisterPage() {
		final CaptchaPanel captchaPanel = new CaptchaPanel("captchaPanel");
		
		this.defaultViews = new ArrayList<View>();
		
		Form<RegisterPage> form = new Form<RegisterPage>("registerForm", new CompoundPropertyModel<RegisterPage>(this)) {
			@Override
			protected void onSubmit() {
				if(!confirmPassword.equals(password))
					error(getString("EqualPasswordInputValidator"));
				if(!captchaPassword.equals(captchaPanel.getPassword()))
					error(getString("InvalidCaptcha"));
				if(userRepo.get(username) != null)
					error(getString("UserExists"));					
				if(!hasError()) {
					User source = WicketSession.get().getUser();
					User target = new User(false, true, username, password, fullname, email);
					target.setDefaultViews(defaultViews);
					userRepo.add(source, target);
					setResponsePage(getApplication().getHomePage());
				}					
			}
		};

		form.add(new UserFormPanel("userFormPanel"));
		form.add(captchaPanel);
		form.add(new Button("register", new ResourceModel("register")));
		add(form);
	}
	
}
