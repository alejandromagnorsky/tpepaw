package ar.edu.itba.it.paw.web.page;

import org.apache.wicket.markup.html.link.Link;

import ar.edu.itba.it.paw.web.common.BasePage;

@SuppressWarnings("serial")
public class InternalErrorPage extends BasePage {
	
	public InternalErrorPage(Exception e){
		add(new Link<Void>("homeLink"){
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}			
		});
		e.printStackTrace();
	}
}
