package ar.edu.itba.it.paw.web.page.version;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class IssueDistributionChart extends Image {

	public IssueDistributionChart(String id, final EntityModel<Version> versionModel) {
		super(id);
		setDefaultModel(versionModel);
		add(new AttributeModifier("src", new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				Set<Issue> issues = new HashSet<Issue>(versionModel.getObject().getAffectingIssues());
				issues.addAll(versionModel.getObject().getFixedIssues());

				if (issues.size() == 0)
					return "";

				StringBuilder url = new StringBuilder();
				url.append("http://chart.apis.google.com/chart?");
				// Size
				url.append("chs=300x225");
				// Pie chart
				url.append("&cht=p");
				// Title
				url.append("&chtt=Distribución de tareas&chts=FFFFFF");

				// Data
				List<Integer> values = new ArrayList<Integer>();
				for (Issue.Priority priority : Issue.Priority.values())
					values.add(getQuant(issues, priority));
				StringBuilder data = new StringBuilder();
				for (Integer value : values)
					data.append(value + ",");
				url.append("&chd=t:"
						+ data.toString().substring(0, data.length() - 1));
				// Labels
				StringBuilder labels = new StringBuilder();
				for (Integer value : values)
					if (value != 0)
						labels.append(value + "|");
				url.append("&chl="
						+ labels.toString().substring(0, labels.length() - 1));
				// References
				StringBuilder names = new StringBuilder();
				for (Issue.Priority priority : Issue.Priority.values())
					names.append(priority.getCaption() + "|");
				url.append("&chdl="
						+ names.toString().substring(0, names.length() - 1));
				// Background
				url.append("&chf=bg,s,67676700");
				// Style
				url.append("&chxs=0,FFFFFF,11.5&chxt=x&chco=FFFFFF|2B90D8|56E06F|EAE95A|F54242");

				return url.toString();
			}
		}));

	}

	private int getQuant(Set<Issue> issues, Issue.Priority p) {
		int count = 0;
		for (Issue i : issues)
			if (i.getPriority().equals(p))
				count++;
		return count;
	}
}
