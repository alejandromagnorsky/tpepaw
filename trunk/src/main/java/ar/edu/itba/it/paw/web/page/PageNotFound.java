package ar.edu.itba.it.paw.web.page;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.link.Link;

import ar.edu.itba.it.paw.web.common.BasePage;

@SuppressWarnings("serial")
public class PageNotFound extends BasePage {
	
	public PageNotFound() {
		add(new Link<Void>("homeLink"){
			@Override
			public void onClick() {
				setResponsePage(getApplication().getHomePage());
			}			
		});
	}
	
	@Override
	protected void configureResponse() {
		super.configureResponse();
		getWebRequestCycle().getWebResponse().getHttpServletResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	@Override
	public boolean isVersioned() {
		return false;
	}

	@Override
	public boolean isErrorPage() {
		return true;
	}
}
