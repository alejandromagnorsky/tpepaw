package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
public class DateTimePanel extends Panel {

	public DateTimePanel(String id, DateTime date) {
		super(id);
		if (date != null){
			add(new Label("dateLabel", date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear()));
			add(new Label("timeLabel", date.getHourOfDay() + ":" + date.getMinuteOfHour() + "hs"));
		} else {
			add(new Label("dateLabel", ""));
			add(new Label("timeLabel", ""));
		}
		add(new Label("atLabel", new ResourceModel("at")));
	}

}
