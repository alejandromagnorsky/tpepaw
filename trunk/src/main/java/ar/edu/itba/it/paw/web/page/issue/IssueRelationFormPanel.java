package ar.edu.itba.it.paw.web.page.issue;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;

@SuppressWarnings("serial")
public class IssueRelationFormPanel extends Panel {
	
	public IssueRelationFormPanel(String id, final IModel<Project> projectModel, final IModel<Issue> issueModel){
		super(id);
		
		IModel<List<Issue>> issuesModel = new LoadableDetachableModel<List<Issue>>() {
			@Override
			protected List<Issue> load() {
				List<Issue> issues = projectModel.getObject().getIssues();
				issues.remove(issueModel.getObject());
				return issues;
			}
		};
		
		ListView<Issue> dependsOnView = new ListView<Issue>("dependsOnView", issuesModel) {
			@Override
			protected void populateItem(ListItem<Issue> item) {
				item.add(new Check<Issue>("checkbox", item.getModel()));
				item.add(new Label("title", new PropertyModel<String>(item.getModel(), "title")));
			}
		};
		
		ListView<Issue> necessaryForView = new ListView<Issue>("necessaryForView", issuesModel) {
			@Override
			protected void populateItem(ListItem<Issue> item) {
				item.add(new Check<Issue>("checkbox", item.getModel()));
				item.add(new Label("title", new PropertyModel<String>(item.getModel(), "title")));
			}
		};
		
		ListView<Issue> relatedToView = new ListView<Issue>("relatedToView", issuesModel) {
			@Override
			protected void populateItem(ListItem<Issue> item) {
				item.add(new Check<Issue>("checkbox", item.getModel()));
				item.add(new Label("title", new PropertyModel<String>(item.getModel(), "title")));
			}
		};
		
		ListView<Issue> duplicatedWithView = new ListView<Issue>("duplicatedWithView", issuesModel) {
			@Override
			protected void populateItem(ListItem<Issue> item) {
				item.add(new Check<Issue>("checkbox", item.getModel()));
				item.add(new Label("title", new PropertyModel<String>(item.getModel(), "title")));
			}
		};
						
		CheckGroup<Issue> dependsOnGroup = new CheckGroup<Issue>("dependsOn");
		dependsOnGroup.add(new Label("dependsOnLabel", new ResourceModel("dependsOn")));
		dependsOnGroup.add(new CheckGroupSelector("selector"));
		dependsOnGroup.add(dependsOnView); dependsOnView.setReuseItems(true);
		add(dependsOnGroup);
		
		CheckGroup<Issue> necessaryForGroup = new CheckGroup<Issue>("necessaryFor");
		necessaryForGroup.add(new Label("necessaryForLabel", new ResourceModel("necessaryFor")));
		necessaryForGroup.add(new CheckGroupSelector("selector"));
		necessaryForGroup.add(necessaryForView); necessaryForView.setReuseItems(true);
		add(necessaryForGroup);
						
		CheckGroup<Issue> relatedToGroup = new CheckGroup<Issue>("relatedTo");
		relatedToGroup.add(new Label("relatedToLabel", new ResourceModel("relatedTo")));
		relatedToGroup.add(new CheckGroupSelector("selector"));
		relatedToGroup.add(relatedToView); relatedToView.setReuseItems(true);
		add(relatedToGroup);
		
		CheckGroup<Issue> duplicatedWithGroup = new CheckGroup<Issue>("duplicatedWith");
		duplicatedWithGroup.add(new Label("duplicatedWithLabel", new ResourceModel("duplicatedWith")));
		duplicatedWithGroup.add(new CheckGroupSelector("selector"));
		duplicatedWithGroup.add(duplicatedWithView); duplicatedWithView.setReuseItems(true);
		add(duplicatedWithGroup);
		
		if(issuesModel.getObject() == null || issuesModel.getObject().isEmpty()){
			dependsOnGroup.setVisible(false);
			necessaryForGroup.setVisible(false);
			relatedToGroup.setVisible(false);
			duplicatedWithGroup.setVisible(false);
		}
				
		add(new FeedbackPanel("feedback"));
	}
}
