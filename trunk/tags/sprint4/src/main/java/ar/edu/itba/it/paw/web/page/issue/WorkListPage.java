package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.DateTimePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;

@SuppressWarnings("serial")
public class WorkListPage extends BasePage {
	
	public EntityModel<Issue> issueModel;
	
	public WorkListPage(Issue issue) {
		issueModel = new EntityModel<Issue>(Issue.class, issue);
		User source = WicketSession.get().getUser();
		
		add(new BreadcrumbsPanel("breadcrumbsPanel", new EntityModel<Project>(Project.class, issueModel.getObject().getProject()), issueModel));
		
		Link<Void> addWorkLink = new Link<Void>("addWork") {
			@Override
			public void onClick() {
				setResponsePage(new AddWorkPage(issueModel.getObject()));
			}
		};
		if(source == null || !issue.canAddWork(source))
			addWorkLink.setVisible(false);
		add(addWorkLink);
		
		

		add(new RefreshingView<Work>("works") {
			@Override
			protected Iterator<IModel<Work>> getItemModels() {
				List<IModel<Work>> result = new ArrayList<IModel<Work>>();
				for (Work w: issueModel.getObject().getWorks())
					result.add(new EntityModel<Work>(Work.class, w));
				return result.iterator();
			}

			@Override
			protected void populateItem(final Item<Work> item) {
				Link<Void> editWorkLink = new Link<Void>("editWork") {
					@Override
					public void onClick() {
						setResponsePage(new EditWorkPage(item.getModelObject()));
					}
				};
				if(WicketSession.get().getUser() == null || !issueModel.getObject().canEditWork(WicketSession.get().getUser()))
					editWorkLink.setVisible(false);
				item.add(editWorkLink);
				item.add(new UsernamePanel("user", new EntityModel<User>(User.class, item.getModelObject().getUser())));
				item.add(new DateTimePanel("date", item.getModelObject().getDate()));
				item.add(new Label("dedicatedTime", new PropertyModel<String>(item.getModel(), "dedicatedTime")));
				item.add(new Label("description", new PropertyModel<String>(item.getModel(), "description")));
			}
		});
	}

}
