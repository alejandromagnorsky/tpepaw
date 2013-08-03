package ar.edu.itba.it.paw.web.page.issue;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.AjaxUtils;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.CurrentFilterPanel;
import ar.edu.itba.it.paw.web.common.DateTimePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.IssuePriorityPanel;
import ar.edu.itba.it.paw.web.common.IssueStateLabel;
import ar.edu.itba.it.paw.web.common.IssueTypePanel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;
import ar.edu.itba.it.paw.web.converter.TimeConverter;

@SuppressWarnings("serial")
public class IssueListPanel extends Panel {
	
	private static final int MAX_ROWS_PER_PAGE = 10;
	
	private IssueDataProvider provider;

	public IssueListPanel(String id, IModel<Project> projectModel, boolean showBreadcrumbs){
		super(id);
		this.provider = new IssueDataProvider(projectModel);
		constructPanel(projectModel, showBreadcrumbs);
	}
	
	private void constructPanel(final IModel<Project> projectModel, boolean showBreadcrumbs){
		User source = WicketSession.get().getUser();
		Project project = projectModel.getObject();
		
		BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbs", projectModel);
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		Link<Void> addIssueLink = new Link<Void>("addIssueLink") {
			@Override
			public void onClick() {
				setResponsePage(AddIssuePage.class);
			}
		};
		
		AjaxFallbackLink<Void> allIssuesLink = new AjaxFallbackLink<Void>("allIssues") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				provider.setAssignedOnly(false);
				target.addComponent(container);
			}
		};
		
		AjaxFallbackLink<Void> assignedIssuesLink = new AjaxFallbackLink<Void>("assignedIssues") {					
			@Override
			public void onClick(AjaxRequestTarget target) {
				provider.setAssignedOnly(true);
				target.addComponent(container);
			}
		};
		
		final DataView<Issue> dataView = new DataView<Issue>("issues", provider){
			@Override
			protected void populateItem(final Item<Issue> item) {
				Issue issue = item.getModelObject();
				
				Link<Issue> viewIssueLink = new Link<Issue>("viewIssueLink", item.getModel()) {
					@Override
					public void onClick() {
						setResponsePage(new IssueViewPage(getModelObject()));
					}
				};
				
				item.add(new Label("code", issue.getCode()));
				item.add(viewIssueLink.add(new Label("title", new PropertyModel<String>(item.getModel(), "title"))));
				item.add(new DateTimePanel("creationDate", item.getModelObject().getCreationDate()));
				item.add(new UsernamePanel("reportedUser", new EntityModel<User>(User.class, item.getModelObject().getReportedUser())));
				item.add(new UsernamePanel("assignedUser", new EntityModel<User>(User.class, item.getModelObject().getAssignedUser())));
				item.add(new IssuePriorityPanel("priority", new PropertyModel<Issue.Priority>(item.getModel(), "priority")));
				item.add(new IssueTypePanel("type", new PropertyModel<Issue.Type>(item.getModel(), "type")));				
				item.add(new IssueStateLabel("state", new PropertyModel<Issue.State>(item.getModel(), "state")));
				item.add(new Label("resolution", new PropertyModel<Issue.State>(item.getModel(), "resolution.caption")));
				item.add(new Label("estimatedTime", new LoadableDetachableModel<String>(){
					@Override
					protected String load() {
						Time time = item.getModelObject().getEstimatedTime();
						return (time == null)? "-" : new TimeConverter().convertToString(time, null);
					}					
				}));
				
				if (item.getIndex() % 2 == 0)
	                  item.add(new AttributeModifier("class", new Model<String>("content-row even-row")));
	              else
	                  item.add(new AttributeModifier("class", new Model<String>("content-row odd-row")));
			}
		};
        dataView.setItemsPerPage(MAX_ROWS_PER_PAGE);

		container.add(AjaxUtils.getOrderLink("orderByCode", "code", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByTitle", "title", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByAssignedUser", "assignedUser", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByEstimatedTime", "estimatedTime", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByPriority", "priority", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByType", "type", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByState", "state", dataView, container, provider));
        container.add(AjaxUtils.getPager("navigator", dataView, container));
		container.add(dataView);

		add(allIssuesLink);
		add(breadcrumbs.setVisible(showBreadcrumbs));
		add(addIssueLink.setVisible(project.canAddIssue(source)));
		add(new CurrentFilterPanel("currentFilterPanel"));
		add(assignedIssuesLink.setVisible(source != null && project.canViewAssigned(source)));
	}
}
