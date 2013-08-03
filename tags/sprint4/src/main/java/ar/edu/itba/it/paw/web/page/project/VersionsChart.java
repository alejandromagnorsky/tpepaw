package ar.edu.itba.it.paw.web.page.project;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.LoadableDetachableModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.model.Work;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class VersionsChart extends Image {

	public VersionsChart(String id, final EntityModel<Project> projectModel) {
		super(id);
		add(new AttributeModifier("src", new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				Project project = projectModel.getObject();
				if(project.getVersions().size() == 0)
					return "";
				StringBuilder url = new StringBuilder();
				url.append("http://chart.apis.google.com/chart?");
				// Size
				url.append("chs=675x315");
				// Vertical bars
				url.append("&cht=bvg");

				// Title
				url.append("&chtt=Tiempos de cada versión en horas&chts=FFFFFF");
				// References
				url.append("&chdl=Estimado|Dedicado");
				// Spacing
				url.append("&chbh=a,0,15");
				// Background
				url.append("&chf=bg,s,1F1F1F");
				// Color
				url.append("&chco=3072F3,FF9900&chxs=0,FFFFFF,11.5,0,lt,FFFFFF|1,FFFFFF,11.5,0,lt,FFFFFF");

				StringBuilder estimatedTimes = new StringBuilder();
				StringBuilder dedicatedTimes = new StringBuilder();
				int i = 0, maxTime = 0;
				for (Version version : project.getVersions()) {
					Time estimatedTime = new Time(0);
					Time dedicatedTime = new Time(0);
					if (i != 0) {
						estimatedTimes.append(",");
						dedicatedTimes.append(",");
					}

					for (Issue issue : version.getAffectingIssues()) {
						estimatedTime.add(issue.getEstimatedTime());
						for (Work work : issue.getWorks())
							dedicatedTime.add(work.getDedicatedTime());
					}
					for (Issue issue : version.getFixedIssues()) {
						if (!version.getAffectingIssues().contains(issue)) {
							estimatedTime.add(issue.getEstimatedTime());
							for (Work work : issue.getWorks())
								dedicatedTime.add(work.getDedicatedTime());
						}
					}
					if (estimatedTime.getMinutes() / 60 > maxTime)
						maxTime = estimatedTime.getMinutes() / 60;
					if (dedicatedTime.getMinutes() / 60 > maxTime)
						maxTime = dedicatedTime.getMinutes() / 60;
					estimatedTimes.append(estimatedTime.getMinutes() / 60);
					dedicatedTimes.append(dedicatedTime.getMinutes() / 60);
					i++;
				}
				// Data
				url.append("&chd=t:" + estimatedTimes + "|" + dedicatedTimes);
				// Axis
				url.append("&chxt=y,x");
				StringBuilder versionNames = new StringBuilder();
				for (Version version : project.getVersions())
					versionNames.append("|" + version.getName());
				// Y Axis
				url.append("&chxr=0,0," + maxTime);
				url.append("&chds=0," + maxTime);
				// X Axis
				url.append("&chxl=1:" + versionNames.toString());
				
				return url.toString();
			}
		}));

	}
}
