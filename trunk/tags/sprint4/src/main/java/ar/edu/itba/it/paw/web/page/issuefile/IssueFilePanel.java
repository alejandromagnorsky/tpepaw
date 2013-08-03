package ar.edu.itba.it.paw.web.page.issuefile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.DateTimePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;

@SuppressWarnings("serial")
public class IssueFilePanel extends Panel {

	public IssueFilePanel(String id, final IModel<IssueFile> issueFileModel){
		super(id);
		IssueFile issueFile = issueFileModel.getObject();
		
		File file = null;
		try {
			file = File.createTempFile("tmp_", ".trackr");
			FileOutputStream output = new FileOutputStream(file);
			output.write(issueFile.getFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Link<Void> deleteIssueFileLink = new Link<Void>("deleteIssueFileLink") {
			@Override
			public void onClick() {
				issueFileModel.getObject().getIssue().deleteIssueFile(WicketSession.get().getUser(), issueFileModel.getObject());
			}
		};
		deleteIssueFileLink.add(new SimpleAttributeModifier("onclick", "return confirm('"+ new ResourceModel("issueFileDeleteConfirmation").getObject() +"');"));
		if(!issueFileModel.getObject().getIssue().canDeleteIssueFile(WicketSession.get().getUser()))
			deleteIssueFileLink.setVisible(false);
		add(deleteIssueFileLink);
		
		DownloadLink downloadLink = new DownloadLink("downloadLink", file, issueFile.getFilename());
		downloadLink.add(new Label("filename", new PropertyModel<String>(issueFileModel, "filename")));
		if (!issueFileModel.getObject().getIssue().canDownloadIssueFile(WicketSession.get().getUser()))
			downloadLink.setVisible(false);
		add(downloadLink);

		add(new Label("sizeInKilobytes", new PropertyModel<Integer>(issueFileModel, "sizeInKilobytes")));
		add(new DateTimePanel("datePanel", issueFile.getUploadDate()));
		add(new UsernamePanel("uploaderPanel", new EntityModel<User>(User.class, issueFile.getUploader())));
	}
}
