package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.model.Progress;

@SuppressWarnings("serial")
public class ProgressBarPanel extends Panel {
	
	public ProgressBarPanel(String id, final IModel<? extends Progress> model){
		super(id);
		
		add(new AttributeModifier("class", true, new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				int progressPercentage = model.getObject().getProgressPercentage();
				if(progressPercentage <= 50)
					return "barContent blue halfBar";
				else if(progressPercentage <= 100)
					return "barContent blue";
				else 
					return "barContentExceeded red";
			}			
		}));
		add(new AttributeModifier("style", true, new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				int progressPercentage = model.getObject().getProgressPercentage();
				if(progressPercentage < 0)
					return "width:0%;";
				else if(progressPercentage > 100)
					return "width:100%;";				
				return "width:"+progressPercentage+"%;";
			}			
		}));
		add(new Label("progressPercentage", new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				int progressPercentage = model.getObject().getProgressPercentage();
				if(progressPercentage < 0)
					return "0";
				else
					return String.valueOf(progressPercentage);
			}			
		}));
	}

}
