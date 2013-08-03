package ar.edu.itba.it.paw.web.page.filter;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;

import ar.edu.itba.it.paw.web.WicketSession;

@SuppressWarnings("serial")
public class ActionPanel extends Panel {

	public ActionPanel(String id){
		super(id);
		RadioChoice<Action> actionChoice;
		if (WicketSession.get().logged())
			actionChoice = new RadioChoice<Action>("action", Arrays.asList(Action.values()));
		else {
			actionChoice = new RadioChoice<Action>("action", Arrays.asList(new Action[]{Action.Apply}));
			setVisible(false);
		}
		actionChoice.setChoiceRenderer(new ChoiceRenderer<Action>("caption"));
		add(actionChoice);
	}
	
	public enum Action {
		Save("Sólo guardar"),
		Apply("Sólo aplicar"),
		SaveAndApply("Guardar y aplicar");
		
		private String caption;
		
		Action(String caption){
			this.caption = caption;
		}
		
		public String getCaption(){
			return caption;
		}
	}
}
