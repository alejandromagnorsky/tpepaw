package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.IssueRepo;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;
import ar.edu.itba.it.paw.web.converter.TimeConverter;

@SuppressWarnings("serial")
public class WorkReportPage extends BasePage {

	private EntityModel<Project> projectModel;

	@SpringBean
	private transient IssueRepo issueRepo;

	private transient DateTime fromDate;
	private transient DateTime toDate;

	private WorkReportPage(DateTime from, DateTime to) {
		Project project = WicketSession.get().getProject();
		projectModel = new EntityModel<Project>(Project.class, project);
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));

		fromDate = from;
		toDate = to;
		addForm();
		addList(true);
	}

	public WorkReportPage() {
		Project project = WicketSession.get().getProject();
		projectModel = new EntityModel<Project>(Project.class, project);
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));
		addForm();
		addList(false);
	}

	private void addList(final boolean visible) {
		add(new RefreshingView<User>("reports") {
			@Override
			protected Iterator<IModel<User>> getItemModels() {
				List<IModel<User>> result = new ArrayList<IModel<User>>();
				for (User i : projectModel.getObject().getUsers())
					if (issueRepo.getWorkByUser(fromDate, toDate,
							projectModel.getObject(), i).getMinutes() > 0)
						result.add(new EntityModel<User>(User.class, i));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<User> item) {
				item.add(new UsernamePanel("user", new EntityModel<User>(
						User.class, item.getModelObject())));
				Time workTime = issueRepo.getWorkByUser(fromDate, toDate,
						projectModel.getObject(), item.getModelObject());
				item.add(new Label("time", new TimeConverter().convertToString(
						workTime, null)));
			}

		}.setVisible(visible));
	}

	private void addForm() {
		Form<Project> form = new Form<Project>("workReportForm",
				new CompoundPropertyModel<Project>(this));

		form.add(new WorkReportFormPanel("workReportFormPanel"));
		form.add(new Button("getReport", new ResourceModel("accept")) {
			@Override
			public void onSubmit() {
				setResponsePage(new WorkReportPage(fromDate, toDate));
			}
		});
		add(form);

	}
}
