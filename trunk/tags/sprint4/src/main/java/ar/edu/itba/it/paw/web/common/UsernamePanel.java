package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.page.user.UserProfilePage;

@SuppressWarnings("serial")
public class UsernamePanel extends Panel {
	
	public UsernamePanel(String id, final IModel<User> userModel){
		super(id);
		setDefaultModel(userModel);
		
		Link<Void> link = new Link<Void>("link"){
			@Override
			public void onClick() {
				if(userModel.getObject() != null)
					setResponsePage(new UserProfilePage(userModel.getObject()));
			}			
		};
		link.add(new Label("username-value", new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				return (userModel.getObject() == null) ? "-" : userModel.getObject().getName();
			}			
		}));
		
		if(userModel.getObject() == null)
			link.setEnabled(false);
		
		Label leaderImage = new Label("leaderImage", "");
		Project project = WicketSession.get().getProject();
		if(project == null || !project.getLeader().equals(userModel.getObject()))
			leaderImage.setVisible(false);
		
		link.add(leaderImage);
		add(link);
	}

}
