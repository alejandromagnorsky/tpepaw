package ar.edu.itba.it.paw.web.page.user;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.UserRepo;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;

@SuppressWarnings("serial")
public class InvalidateUserPage extends BasePage {
	
	@SpringBean
	private transient UserRepo userRepo;
	
	private transient User user;
	
	public InvalidateUserPage(){
		Form<Void> form = new Form<Void>("invalidateUserForm", new CompoundPropertyModel<Void>(this)) {
			@Override
			protected void onSubmit(){
				User source = WicketSession.get().getUser();
				if(user.isActive())
					error(getString("ActiveUser"));
				if(user.equals(source))
					error(getString("SourceUser"));
				if(!hasError()){
					user.invalidate(source);
					setResponsePage(getApplication().getHomePage());
				}
			}
		};
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				return userRepo.getValidUsers();
			}			
		};
		form.add(new DropDownChoice<User>("user", usersModel).setRequired(true));
		form.add(new FeedbackPanel("feedback"));
		form.add(new Button("invalidateUser", new ResourceModel("invalidateUser")));
		add(form);
	}

}
