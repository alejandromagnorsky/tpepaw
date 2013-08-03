package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
public class DatePanel extends Panel {

	public DatePanel(String id, DateTime date){
		super(id);
		add(new Label("date", date.getDayOfMonth()+"/"+date.getMonthOfYear()+"/"+date.getYear()));
	}
}
