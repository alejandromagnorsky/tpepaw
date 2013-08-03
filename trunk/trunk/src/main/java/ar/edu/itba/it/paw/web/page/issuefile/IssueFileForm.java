package ar.edu.itba.it.paw.web.page.issuefile;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Bytes;

import ar.edu.itba.it.paw.model.IssueFile;

@SuppressWarnings("serial")
public class IssueFileForm extends Form<IssueFile> {
	
	public IssueFileForm(String id, IModel<IssueFile> model) {
		super(id, model);
		setMultiPart(true);
		add(new FileUploadField("file"));
        //add(new UploadProgressBar("progress", this)); //NO ME ANDA NO SE POR QUë, no registra el porcentaje
		setMaxSize(Bytes.megabytes(1));
	}
}
