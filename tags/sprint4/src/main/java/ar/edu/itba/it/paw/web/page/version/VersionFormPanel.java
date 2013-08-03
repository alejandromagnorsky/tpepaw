package ar.edu.itba.it.paw.web.page.version;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.model.Version.VersionState;
import ar.edu.itba.it.paw.web.converter.DateTimeConverter;

@SuppressWarnings("serial")
public class VersionFormPanel extends Panel {

	public VersionFormPanel(String id) {
		super(id);
		add(new RequiredTextField<String>("name"));
		add(new TextArea<String>("description")
				.add(new StringValidator.MaximumLengthValidator(255)));
		add(new DateTextField("releaseDate") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return new DateTimeConverter();
			}
		}.setRequired(true));

		IModel<List<VersionState>> stateModel = new LoadableDetachableModel<List<VersionState>>() {
			@Override
			protected List<VersionState> load() {
				return Arrays.asList(VersionState.values());
			}
		};
		add(new DropDownChoice<VersionState>("state", stateModel)
				.setChoiceRenderer(new ChoiceRenderer<VersionState>("caption"))
				.setRequired(true));

		add(new FeedbackPanel("feedback"));
	}

}
