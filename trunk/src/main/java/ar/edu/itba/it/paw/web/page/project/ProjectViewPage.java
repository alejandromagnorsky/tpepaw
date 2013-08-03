package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueLog;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.User.View;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.DateTimePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;
import ar.edu.itba.it.paw.web.page.filter.FilterListPage;
import ar.edu.itba.it.paw.web.page.filter.FilterListPanel;
import ar.edu.itba.it.paw.web.page.issue.IssueListPage;
import ar.edu.itba.it.paw.web.page.issue.IssueListPanel;
import ar.edu.itba.it.paw.web.page.issue.IssueViewPage;
import ar.edu.itba.it.paw.web.page.version.VersionListPage;
import ar.edu.itba.it.paw.web.page.version.VersionListPanel;

@SuppressWarnings("serial")
public class ProjectViewPage extends BasePage {

	private EntityModel<Project> projectModel;

	public ProjectViewPage(Project project) {
		WicketSession.get().setProject(project);
		User source = WicketSession.get().getUser();
		final IModel<User> userModel = new EntityModel<User>(User.class, source);
		this.projectModel = new EntityModel<Project>(Project.class, project);

		add(new Label("name", new PropertyModel<String>(projectModel, "name")));
		add(new ProjectPanel("projectPanel", projectModel));

		WebMarkupContainer recentChangesContainer = new WebMarkupContainer("recentChangesContainer");
		RefreshingView<IssueLog> recentChanges = new RefreshingView<IssueLog>("recentChanges") {
			@Override
			protected Iterator<IModel<IssueLog>> getItemModels() {
				SortedSet<IModel<IssueLog>> result = new TreeSet<IModel<IssueLog>>(
						(new Comparator<IModel<IssueLog>>() {
							public int compare(IModel<IssueLog> o1,
									IModel<IssueLog> o2) {
								return o2.getObject().getDate()
										.compareTo(o1.getObject().getDate());
							}
						}));

				for (IssueLog i : projectModel.getObject().getLastChanges(5))
					result.add(new EntityModel<IssueLog>(IssueLog.class, i));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<IssueLog> item) {
				Link<Issue> link = new Link<Issue>("link",
						new EntityModel<Issue>(Issue.class, item
								.getModelObject().getIssue())) {
					@Override
					public void onClick() {
						setResponsePage(new IssueViewPage(getModelObject()));
					}
				};
				link.add(new Label("issue", new PropertyModel<String>(item
						.getModel(), "issue.title")));
				item.add(link);
				item.add(new UsernamePanel("source", new EntityModel<User>(
						User.class, item.getModelObject().getSource())));
				item.add(new DateTimePanel("date", item.getModelObject()
						.getDate()));
				item.add(new Label("type", new PropertyModel<String>(item
						.getModel(), "type.caption")));

				item.add(new Label("previous", new PropertyModel<String>(item
						.getModel(), "previous")));
				item.add(new Label("actual", new PropertyModel<String>(item
						.getModel(), "actual")));
			}
		};
		recentChangesContainer.add(recentChanges);
		add(recentChangesContainer.setVisible(source != null && source.getDefaultViews().contains(View.RecentChanges)));
		
		Link<Void> viewUsersLink = new Link<Void>("viewUsersLink") {
			@Override
			public void onClick() {
				setResponsePage(new ProjectUsersPage(projectModel.getObject()));
			}
		};
		if(!projectModel.getObject().canAddUser(source))
			viewUsersLink.setVisible(false);
		add(viewUsersLink);
		
		Link<Void> viewFiltersLink = new Link<Void>("viewFiltersLink") {
			@Override
			public void onClick() {
				setResponsePage(new FilterListPage(projectModel.getObject()));
			}
		};
		if(source == null || !projectModel.getObject().canViewFilterManager(source))
			viewFiltersLink.setVisible(false);
		add(viewFiltersLink);
		
		Link<Void> viewIssuesLink = new Link<Void>("viewIssuesLink") {
			@Override
			public void onClick() {
				setResponsePage(new IssueListPage(projectModel.getObject()));
			}
		};
		add(viewIssuesLink);

		Link<Void> viewWorkReportLink = new Link<Void>("viewWorkReportLink") {
			@Override
			public void onClick() {
				setResponsePage(WorkReportPage.class);
			}
		};
		if(!projectModel.getObject().canViewWorkReport(source))
			viewWorkReportLink.setVisible(false);
		add(viewWorkReportLink);

		Link<Void> versionListLink = new Link<Void>("versionList") {
			@Override
			public void onClick() {
				setResponsePage(new VersionListPage(projectModel.getObject()));
			}
		};
		if (!projectModel.getObject().canViewVersionList(source))
			versionListLink.setVisible(false);
		add(versionListLink);

		Link<Void> editProjectLink = new Link<Void>("editProject") {
			@Override
			public void onClick() {
				setResponsePage(EditProjectPage.class);
			}
		};
		if (source == null || !source.canEditProject())
			editProjectLink.setVisible(false);
		add(editProjectLink);

		Link<Void> viewStateLink = new Link<Void>("viewStatus") {
			@Override
			public void onClick() {
				setResponsePage(ProjectStatusPage.class);
			}
		};
		if (!project.canViewStatus(source))
			viewStateLink.setVisible(false);
		add(viewStateLink);
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new ResourceModel("issues")) {
			@Override
			public Panel getPanel(String panelId) {
				return new IssueListPanel(panelId, projectModel, false);
			}
			
			@Override
			public boolean isVisible(){
				IModel<List<View>> viewsModel = new PropertyModel<List<View>>(userModel, "defaultViews");
				return viewsModel.getObject().contains(View.Issues);
			}
		});
		tabs.add(new AbstractTab(new ResourceModel("filters")) {
			@Override
			public Panel getPanel(String panelId) {
				return new FilterListPanel(panelId, projectModel, false);
			}
			
			@Override
			public boolean isVisible(){
				IModel<List<View>> viewsModel = new PropertyModel<List<View>>(userModel, "defaultViews");
				return viewsModel.getObject().contains(View.Filters);
			}
		});
		tabs.add(new AbstractTab(new ResourceModel("versions")) {
			@Override
			public Panel getPanel(String panelId) {
				return new VersionListPanel(panelId, projectModel, false);
			}
			
			@Override
			public boolean isVisible(){
				IModel<List<View>> viewsModel = new PropertyModel<List<View>>(userModel, "defaultViews");
				return viewsModel.getObject().contains(View.Versions);
			}
		});
		add(new AjaxTabbedPanel("tabs", tabs).setVisible(source != null && !source.getDefaultViews().isEmpty()));
		
	}
}
