package ar.edu.itba.it.paw.web.page.version;

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

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.AjaxUtils;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.DatePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.ProgressBarPanel;
import ar.edu.itba.it.paw.web.common.TextPropertyPanel;

@SuppressWarnings("serial")
public class VersionListPanel extends Panel {

	private static final int MAX_ROWS_PER_PAGE = 10;
	
	private VersionDataProvider provider;
	
	public VersionListPanel(String id, IModel<Project> projectModel, boolean showBreadcrumbs){
		super(id);
		this.provider = new VersionDataProvider(projectModel);
		constructPanel(projectModel, showBreadcrumbs);
	}
	
	private void constructPanel(final IModel<Project> projectModel, boolean showBreadcrumbs){
		final IModel<User> userModel = new EntityModel<User>(User.class, WicketSession.get().getUser());
		
		BreadcrumbsPanel breadcrumbs = new BreadcrumbsPanel("breadcrumbs", projectModel);
		final WebMarkupContainer container = new WebMarkupContainer("container");
		
        Link<Void> addVersionLink = new Link<Void>("addVersionLink") {
			@Override
			public void onClick() {
				setResponsePage(AddVersionPage.class);
			}
		};
		
		AjaxFallbackLink<Void> allVersionsLink = new AjaxFallbackLink<Void>("allVersions") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				provider.setNonReleasedOnly(false);
				target.addComponent(container);
			}
		};
		
		AjaxFallbackLink<Void> nonReleasedVersionsLink = new AjaxFallbackLink<Void>("nonReleasedVersions") {					
			@Override
			public void onClick(AjaxRequestTarget target) {
				provider.setNonReleasedOnly(true);
				target.addComponent(container);
			}
		};
			
		final DataView<Version> dataView = new DataView<Version>("versions", provider){
			@Override
			protected void populateItem(final Item<Version> item) {
				IndicatingAjaxLink<Version> deleteVersionLink = new IndicatingAjaxLink<Version>("deleteVersionLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						projectModel.getObject().deleteVersion(userModel.getObject(), item.getModel().getObject());
						if(target != null)
							target.addComponent(container);
					}
					
					public IAjaxCallDecorator getAjaxCallDecorator(){
						return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
							@Override
							public CharSequence preDecorateScript(CharSequence script) {
								return "if(!confirm('" + getString("versionDeleteConfirmation") + "')) return false;" + script;
							}
						};
					}
				};

				Link<Void> editVersionLink = new Link<Void>("editVersionLink") {
					@Override
					public void onClick() {
						setResponsePage(new EditVersionPage(item.getModel().getObject()));
					}
				};
				
				final WebMarkupContainer hideable = new WebMarkupContainer("hideable");
				final Label showHideLabel = new Label("showHideLabel", "+ " + new ResourceModel("showHideDetails").getObject());
				
				AjaxFallbackLink<Void> showHideDetails = new AjaxFallbackLink<Void>("showHideDetails") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						String op = (hideable.isVisible()) ? "+ ": "- ";
						showHideLabel.setDefaultModelObject(op + new ResourceModel("showHideDetails").getObject());
						hideable.setVisible(!hideable.isVisible());
						target.addComponent(hideable);
						target.addComponent(showHideLabel);
					}					
				};
				
				hideable.add(new IssueDistributionChart("chart", new EntityModel<Version>(Version.class, item.getModelObject())));
				hideable.add(new TextPropertyPanel("descriptionPanel", new PropertyModel<String>(item.getModel(), "description"), "description"));
				
				if (item.getIndex() % 2 == 0)
					item.add(new AttributeModifier("class", new Model<String>("content-row even-row")));
				else
					item.add(new AttributeModifier("class", new Model<String>("content-row odd-row")));

				item.add(deleteVersionLink.setVisible(userModel.getObject() != null && projectModel.getObject().canDeleteVersion(userModel.getObject(), item.getModel().getObject())));
				item.add(editVersionLink.setVisible(userModel.getObject() != null && projectModel.getObject().canEditVersion(userModel.getObject())));
				item.add(new ProgressBarPanel("progressBarContent", item.getModel()));
				item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
				item.add(new DatePanel("releaseDate", item.getModelObject().getReleaseDate()));
				item.add(new Label("state", new PropertyModel<String>(item.getModel(), "state.caption")));
				item.add(showHideDetails.add(showHideLabel.setOutputMarkupPlaceholderTag(true)));
				item.add(hideable.setOutputMarkupPlaceholderTag(true).setVisible(false));
			}
		};
        dataView.setItemsPerPage(MAX_ROWS_PER_PAGE);
        
        Label editVersionTitle = new Label("editVersionTitle", new ResourceModel("edit"));
		
		container.add(editVersionTitle.setVisible(projectModel.getObject().canEditVersion(userModel.getObject())));
		container.add(AjaxUtils.getOrderLink("orderByName", "name", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByReleaseDate", "releaseDate", dataView, container, provider));
		container.add(AjaxUtils.getOrderLink("orderByState", "state", dataView, container, provider));
        container.add(AjaxUtils.getPager("navigator", dataView, container));
		container.add(dataView);

		add(allVersionsLink);
		add(nonReleasedVersionsLink.setVisible(projectModel.getObject().canViewVersionList(userModel.getObject())));
		add(addVersionLink.setVisible(projectModel.getObject().canAddVersion(userModel.getObject())));
		add(breadcrumbs.setVisible(showBreadcrumbs));
		add(container.setOutputMarkupId(true));
	}
}
