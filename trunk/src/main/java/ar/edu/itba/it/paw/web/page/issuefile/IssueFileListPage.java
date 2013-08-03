package ar.edu.itba.it.paw.web.page.issuefile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.model.IssueRepo;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class IssueFileListPage extends BasePage {

	@SpringBean
	private IssueRepo issueRepo;
	
	public transient FileUpload file;
	
	public IssueFileListPage(Issue issue) {
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, issue.getProject());
		final EntityModel<Issue> issueModel = new EntityModel<Issue>(Issue.class, issue);
		
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel, issueModel));		

		final IssueFileForm form = new IssueFileForm("uploadIssueFileForm", new CompoundPropertyModel<IssueFile>(this));
		if (!issue.canAddIssueFile(WicketSession.get().getUser()))
			form.setVisible(false);
		
		form.add(new Button("uploadIssueFile", new ResourceModel("upload")){
			@Override
			public void onSubmit(){
				if (file != null){
					IssueFile uploaded = new IssueFile(	issueModel.getObject(), file.getBytes(),
											file.getClientFileName(), file.getSize(),
											new DateTime(), WicketSession.get().getUser() );
					issueRepo.persist(uploaded);
				}
			}
		});
		form.add(new FeedbackPanel("feedback"));
		add(form);
		
		add(new RefreshingView<IssueFile>("issueFiles") {
			@Override
			protected Iterator<IModel<IssueFile>> getItemModels() {
				List<IModel<IssueFile>> result = new ArrayList<IModel<IssueFile>>();
				for (IssueFile f : issueModel.getObject().getFiles())
					result.add(new EntityModel<IssueFile>(IssueFile.class, f));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<IssueFile> item) {
				item.add(new IssueFilePanel("issueFilePanel", item.getModel()));
			}
		});
	}
}
