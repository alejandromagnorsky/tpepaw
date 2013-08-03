package ar.edu.itba.it.paw.web.page.project;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.convert.IConverter;

import ar.edu.itba.it.paw.web.converter.DateTimeConverter;

@SuppressWarnings("serial")
public class WorkReportFormPanel extends Panel {

	public WorkReportFormPanel(String id) {
		super(id);

		add(new DateTextField("fromDate") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return new DateTimeConverter();
			}
		}.setRequired(true));

		add(new DateTextField("toDate") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return new DateTimeConverter();
			}
		}.setRequired(true));

		add(new FeedbackPanel("feedback"));
	}

}
