package ar.edu.itba.it.paw.web.page.filter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.AjaxUtils;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.TextPropertyPanel;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class FilterListPanel extends Panel {

	private static final int MAX_ROWS_PER_PAGE = 10;
	
	private FilterDataProvider provider;
	
	public FilterListPanel(String id, IModel<Project> projectModel, boolean showBreadcrumbs){
		super(id);
		this.provider = new FilterDataProvider(projectModel);
		constructPanel(projectModel, showBreadcrumbs);
	}
	
	private void constructPanel(final IModel<Project> projectModel, boolean showBreadcrumbs){
		final IModel<User> userModel = new EntityModel<User>(User.class, WicketSession.get().getUser());
		User user = userModel.getObject();
		Project project = projectModel.getObject();
		
		BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbs", projectModel);

        Link<Void> addFilterLink = new Link<Void>("addFilterLink") {
			@Override
			public void onClick() {
				setResponsePage(AddFilterPage.class);
			}
		};
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
			
		Label editFilterTitle = new Label("editFilterTitle", new ResourceModel("edit"));
        
		final DataView<Filter> dataView = new DataView<Filter>("filters", provider){
			@Override
			protected void populateItem(final Item<Filter> item) {
				IndicatingAjaxLink<Void> deleteFilterLink = new IndicatingAjaxLink<Void>("deleteFilterLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						WicketSession.get().removeFilter(item.getModelObject());
						projectModel.getObject().deleteFilter(userModel.getObject(), item.getModelObject());
						if(target != null)
							target.addComponent(container);
					}
					
					public IAjaxCallDecorator getAjaxCallDecorator(){
						return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
							@Override
							public CharSequence preDecorateScript(CharSequence script) {
								return "if(!confirm('" + getString("filterDeleteConfirmation") + "')) return false;" + script;
							}
						};
					}
				};
				
				Link<Void> editFilterLink = new Link<Void>("editFilterLink") {
					@Override
					public void onClick() {
						setResponsePage(new EditFilterPage(item.getModel().getObject()));
					}
				};
				
				Link<Filter> applyFilterLink = new Link<Filter>("applyFilterLink", item.getModel()) {
					@Override
					public void onClick() {
						WicketSession.get().addFilter(item.getModelObject());
						setResponsePage(new ProjectViewPage(projectModel.getObject()));
					}
				};
				
				Filter filter = item.getModel().getObject();		
				final WebMarkupContainer hideable = new WebMarkupContainer("hideable");				
				final Label showHideLabel = new Label("showHideLabel", "+ " + new ResourceModel("showHideDetails").getObject());
				
				AjaxFallbackLink<Void> showHideLink = new AjaxFallbackLink<Void>("showHideDetails") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						String op = (hideable.isVisible()) ? "+ ": "- ";
						showHideLabel.setDefaultModelObject(op + new ResourceModel("showHideDetails").getObject());
						hideable.setVisible(!hideable.isVisible());
						target.addComponent(hideable);
						target.addComponent(showHideLabel);
					}					
				};
				
				applyFilterLink.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
				hideable.add(new TextPropertyPanel("issueCodePanel", new PropertyModel<String>(item.getModel(), "issueCode"), "issueCode"));
				hideable.add(new TextPropertyPanel("issueTitlePanel", new PropertyModel<String>(item.getModel(), "issueTitle"), "issueTitle"));
				hideable.add(new TextPropertyPanel("issueDescriptionPanel", new PropertyModel<String>(item.getModel(), "issueDescription"), "issueDescription"));
				hideable.add(new TextPropertyPanel("issueReportedUserPanel", new EntityModel<User>(User.class, filter.getIssueReportedUser()), "issueReportedUser"));
				hideable.add(new TextPropertyPanel("issueAssignedUserPanel", new EntityModel<User>(User.class, filter.getIssueAssignedUser()), "issueAssignedUser"));
				hideable.add(new TextPropertyPanel("issueTypePanel", new PropertyModel<String>(item.getModel(), "issueType.caption"), "issueType"));
				hideable.add(new TextPropertyPanel("issueStatePanel", new PropertyModel<String>(item.getModel(), "issueState.caption"), "issueState"));
				hideable.add(new TextPropertyPanel("issueResolutionPanel", new PropertyModel<String>(item.getModel(), "issueResolution.caption"), "issueResolution"));
				hideable.add(new TextPropertyPanel("affectedVersionPanel", new PropertyModel<String>(item.getModel(), "affectedVersion.name"), "affectedVersion"));
				hideable.add(new TextPropertyPanel("fixedVersionPanel", new PropertyModel<String>(item.getModel(), "fixedVersion.name"), "fixedVersion"));
				hideable.add(new TextPropertyPanel("dateFromPanel", filter.getDateFrom(), "dateFrom"));
				hideable.add(new TextPropertyPanel("dateToPanel", filter.getDateTo(), "dateTo"));
				
				if (item.getIndex() % 2 == 0)
	                  item.add(new AttributeModifier("class", new Model<String>("content-row even-row")));
	              else
	                  item.add(new AttributeModifier("class", new Model<String>("content-row odd-row")));
				
				// Filter row
				item.add(deleteFilterLink.setVisible(projectModel.getObject().canDeleteFilter(userModel.getObject())));
				item.add(editFilterLink.setVisible(projectModel.getObject().canEditFilter(userModel.getObject())));
				item.add(applyFilterLink);
				item.add(showHideLink.add(showHideLabel.setOutputMarkupPlaceholderTag(true)));
				item.add(hideable.setOutputMarkupPlaceholderTag(true).setVisible(false));
			}
		};
		
        dataView.setItemsPerPage(MAX_ROWS_PER_PAGE);
		container.add(editFilterTitle.setVisible(project.canEditFilter(user)));
		container.add(AjaxUtils.getOrderLink("orderByName", "name", dataView, container, provider));
        container.add(AjaxUtils.getPager("navigator", dataView, container));
		container.add(dataView);
        
		add(breadcrumbs.setVisible(showBreadcrumbs));
		add(addFilterLink);
		add(container.setOutputMarkupId(true));
	}
}
