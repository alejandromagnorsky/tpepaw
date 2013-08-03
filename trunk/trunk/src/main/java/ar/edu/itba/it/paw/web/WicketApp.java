package ar.edu.itba.it.paw.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.convert.ConverterLocator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.common.HibernateWebRequestCycle;
import ar.edu.itba.it.paw.web.converter.ProjectConverter;
import ar.edu.itba.it.paw.web.converter.TimeConverter;
import ar.edu.itba.it.paw.web.converter.UserConverter;
import ar.edu.itba.it.paw.web.page.InternalErrorPage;
import ar.edu.itba.it.paw.web.page.PageNotFound;
import ar.edu.itba.it.paw.web.page.project.ProjectListPage;

@Component
public class WicketApp extends WebApplication {

	@SpringBean
	private ProjectRepo projectRepo;

	@Override
	public Class<? extends Page> getHomePage() {
		return ProjectListPage.class;
	}

	private final SessionFactory sessionFactory;

	@Autowired
	public WicketApp(SessionFactory sessionFactory, ProjectRepo projectRepo) {
		this.sessionFactory = sessionFactory;
		this.projectRepo = projectRepo;
	}

	@Override
	protected void init() {
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
		mount(new HybridUrlCodingStrategy("/404", PageNotFound.class));
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new WicketSession(request);
	}

	@Override
	public RequestCycle newRequestCycle(Request request, Response response) {
		return new HibernateWebRequestCycle(this, (WebRequest) request,
				response, sessionFactory);
	}
	
	// Encrypted URLs
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor() { 
		return new WebRequestCycleProcessor() {
			protected IRequestCodingStrategy newRequestCodingStrategy() {
				return new CryptedUrlWebRequestCodingStrategy(
					new WebRequestCodingStrategy());
			}
		};
	}

	@Override
	protected IConverterLocator newConverterLocator() {
		ConverterLocator converterLocator = new ConverterLocator();
		converterLocator.set(User.class, new UserConverter());
		converterLocator.set(Project.class, new ProjectConverter(projectRepo));
		converterLocator.set(Time.class, new TimeConverter());
		return converterLocator;
	}
	
	@Override
	protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
		return new UploadWebRequest(servletRequest);
	}
}
