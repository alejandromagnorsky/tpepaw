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
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class EditUserPage extends BasePage {
	
	@SpringBean
	private transient UserRepo userRepo;

	private transient String username;
	private transient String password;
	private transient String confirmPassword;
	private transient String fullname;
	private transient String email;
	private transient List<View> defaultViews;
	
	public EditUserPage(User user){
		final EntityModel<User> userModel = new EntityModel<User>(User.class, user);
		setDefaultModel(userModel);
		username = user.getName();
		fullname = user.getFullname();
		email = user.getEmail();
		if (userModel.getObject().getDefaultViews() == null)
			defaultViews = new ArrayList<View>();
		else
			defaultViews = userModel.getObject().getDefaultViews();
		
		Form<RegisterPage> form = new Form<RegisterPage>("editUserForm", new CompoundPropertyModel<RegisterPage>(this)) {
			@Override
			protected void onSubmit() {
				User source = WicketSession.get().getUser();
				User target = userModel.getObject();
				if(!target.verifyPassword(password) && !confirmPassword.equals(password))
					error(getString("EqualPasswordInputValidator"));
				if(!username.equals(target.getName()) && userRepo.get(username) != null)
					error(getString("UserExists"));					
				if(!hasError()) {
					target.setName(source, username);
					target.setPassword(source, password);
					target.setFullname(source, fullname);
					target.setEmail(source, email);
					target.setDefaultViews(defaultViews);
					setResponsePage(new UserProfilePage(target));
				}					
			}
		};

		form.add(new UserFormPanel("userFormPanel"));
		form.add(new Button("editUser", new ResourceModel("edit")));
		add(form);
	}
	

}
