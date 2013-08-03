package ar.edu.itba.it.paw.web.page.project;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.UserRepo;

@SuppressWarnings("serial")
public class ProjectFormPanel extends Panel {

	@SpringBean
	private UserRepo userRepo;
	
	public ProjectFormPanel(String id) {
		super(id);
		
		add(new RequiredTextField<String>("name"));
		add(new RequiredTextField<String>("code"));
		add(new TextArea<String>("description").add(new StringValidator.MaximumLengthValidator(255)));
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				return userRepo.getValidUsers();
			}			
		};
		add(new DropDownChoice<User>("leader", usersModel).setRequired(true));
		
		RadioChoice<Visibility> radioChoice = new RadioChoice<Visibility>("visibility", Arrays.asList(Visibility.values()));
		radioChoice.setRequired(true);
		radioChoice.setChoiceRenderer(new ChoiceRenderer<Visibility>("caption", "isPublic"));
		add(radioChoice);
		add(new FeedbackPanel("feedback"));		
	}
	
	public enum Visibility {
		Public("Público", true), Private("Privado", false);

		private String caption;
		private Boolean isPublic;
		
		Visibility(String caption, Boolean isPublic) {
			this.caption = caption;
			this.isPublic = isPublic;
		}

		public String getCaption() {
			return caption;
		}

		public Boolean getIsPublic() {
			return isPublic;
		}
	}
}
