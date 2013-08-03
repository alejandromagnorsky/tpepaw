package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class IssueDataProvider extends SortableDataProvider<Issue> {
	
	private IModel<Project> projectModel;
	private boolean assignedOnly;
	
	public IssueDataProvider(IModel<Project> projectModel) {
		setSort("title", true);
		this.projectModel = projectModel;
		this.assignedOnly = false;
	}
	
	public void setAssignedOnly(boolean assignedOnly){
		this.assignedOnly = assignedOnly;
	}

	private List<Issue> getIssues() {
		if (assignedOnly)
			return getAssignedIssues();
		return getAllIssues();
	}
	
	private List<Issue> getAllIssues(){
		Filter filter = WicketSession.get().getFilter();
		List<Issue> issues = new ArrayList<Issue>();
		for (Issue i : projectModel.getObject().getIssues())
			if(filter == null || i.filter(filter))
				issues.add(i);
		sort(issues);
		return issues;
	}
	
	private List<Issue> getAssignedIssues() {
		Filter filter = WicketSession.get().getFilter();
		List<Issue> issues = new ArrayList<Issue>();
		if (WicketSession.get().getUser().getIssues(projectModel.getObject()) == null)
			return new ArrayList<Issue>();
		for (Issue i : new ArrayList<Issue>(WicketSession.get().getUser().getIssues(projectModel.getObject())))
			if (i.getState().equals(Issue.State.Open) || i.getState().equals(Issue.State.Ongoing))
				if (filter == null || i.filter(filter))
					issues.add(i);
		sort(issues);
		return issues;
	}

	public Iterator<Issue> iterator(int first, int count) {
		return getIssues().subList(first, first + count).iterator();
	}

	public int size() {
		return getIssues().size();
	}

	public IModel<Issue> model(Issue issue) {
		return new EntityModel<Issue>(Issue.class, issue);
	}
	
	private void sort(List<Issue> issues){
		final boolean isAsc = getSort().isAscending();
		String property = getSort().getProperty();
		
		if (property.equals("title")){
			if (isAsc)
				Collections.sort(issues);
			else
				Collections.sort(issues, Collections.reverseOrder());
		} else if (property.equals("code")){
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					if (!isAsc)
						return i2.getCode().compareTo(i1.getCode());
					return i1.getCode().compareTo(i2.getCode());
				}				
			});
		} else if (property.equals("assignedUser")){
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					String name1 = "", name2 = "";
					if (i1.getAssignedUser() != null)
						name1 = i1.getAssignedUser().getName();
					if (i2.getAssignedUser() != null)
						name2 = i2.getAssignedUser().getName(); 
					if (!isAsc)
						return name2.compareTo(name1);
					return name1.compareTo(name2);
				}
			});
		} else if (property.equals("estimatedTime")){
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					int time1 = 0, time2 = 0;
					if (i1.getEstimatedTime() != null)
						time1 = i1.getEstimatedTime().getMinutes();
					if(i2.getEstimatedTime() != null)
						time2 = i2.getEstimatedTime().getMinutes();
					if (!isAsc)
						return time2 - time1;
					return time1 - time2;
				}				
			});
		} else if (property.equals("priority")){
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					if (!isAsc)
						return i2.getPriority().compareTo(i1.getPriority());
					return i1.getPriority().compareTo(i2.getPriority());
				}				
			});
		} else if (property.equals("type")){
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					if (!isAsc)
						return i2.getType().getCaption().compareTo(i1.getType().getCaption());
					return i1.getType().getCaption().compareTo(i2.getType().getCaption());
				}				
			});
		} else if (property.equals("state")) {
			Collections.sort(issues, new Comparator<Issue>(){
				public int compare(Issue i1, Issue i2) {
					if (!isAsc)
						return i2.getState().getCaption().compareTo(i1.getState().getCaption());
					return i1.getState().getCaption().compareTo(i2.getState().getCaption());
				}				
			});
		}
	}
}