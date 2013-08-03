package ar.edu.itba.it.paw.web.command;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueRelation;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.IssueRelation.Type;

public class IssueRelationForm {
	private Issue issue;
	private List<Issue> dependsOn;
	private List<Issue> necessaryFor;
	private List<Issue> relatedTo;
	private List<Issue> duplicatedWith;
	
	public Issue getIssue() {
		return issue;
	}
	
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public List<Issue> getDependsOn() {
		return dependsOn;
	}
	
	public void setDependsOn(List<Issue> dependsOn) {
		this.dependsOn = dependsOn;
	}
	
	public List<Issue> getNecessaryFor() {
		return necessaryFor;
	}
	
	public void setNecessaryFor(List<Issue> necessaryFor) {
		this.necessaryFor = necessaryFor;
	}
	
	public List<Issue> getRelatedTo() {
		return relatedTo;
	}
	
	public void setRelatedTo(List<Issue> relatedTo) {
		this.relatedTo = relatedTo;
	}
	
	public List<Issue> getDuplicatedWith() {
		return duplicatedWith;
	}
	
	public void setDuplicatedWith(List<Issue> duplicatedWith) {
		this.duplicatedWith = duplicatedWith;
	}
	
	public void update(User user){
		addRelations(user);
		deleteRelations(user);
	}
	
	private void addRelations(User user){
		if (dependsOn != null)
			for (Issue i : dependsOn){
				issue.addRelation(user, new IssueRelation(Type.DependsOn, issue, i));
				i.addRelation(user, new IssueRelation(Type.NecessaryFor, i, issue));
			}
		
		if (necessaryFor != null)
			for (Issue i : necessaryFor){
				issue.addRelation(user, new IssueRelation(Type.NecessaryFor, issue, i));
				i.addRelation(user, new IssueRelation(Type.DependsOn, i, issue));
			}
		
		if (relatedTo != null)
			for (Issue i : relatedTo){
				issue.addRelation(user, new IssueRelation(Type.RelatedTo, issue, i));
				i.addRelation(user, new IssueRelation(Type.RelatedTo, i, issue));
			}
		
		if (duplicatedWith != null)
			for (Issue i : duplicatedWith){
				issue.addRelation(user, new IssueRelation(Type.DuplicatedWith, issue, i));
				i.addRelation(user, new IssueRelation(Type.DuplicatedWith, i, issue));
			}
	}
	
	private void deleteRelations(User user){
		List<Issue> toRemoveDependsOn = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveNecessaryFor = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveRelatedTo = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveDuplicatedWith = new ArrayList<Issue>(issue.getProject().getIssues());
		
		toRemoveDependsOn.remove(issue);
		toRemoveNecessaryFor.remove(issue);
		toRemoveRelatedTo.remove(issue);
		toRemoveDuplicatedWith.remove(issue);

		if (dependsOn != null)
			toRemoveDependsOn.removeAll(dependsOn);
			for (Issue i : toRemoveDependsOn){
				issue.deleteRelation(user, new IssueRelation(Type.DependsOn, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.NecessaryFor, i, issue));
			}
		
		if (necessaryFor != null)
			toRemoveNecessaryFor.removeAll(necessaryFor);
			for (Issue i : toRemoveNecessaryFor){
				issue.deleteRelation(user, new IssueRelation(Type.NecessaryFor, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.DependsOn, i, issue));
			}
		
		if (relatedTo != null)
			toRemoveRelatedTo.removeAll(relatedTo);
			for (Issue i : toRemoveRelatedTo){
				issue.deleteRelation(user, new IssueRelation(Type.RelatedTo, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.RelatedTo, i, issue));
			}
		
		if (duplicatedWith != null)
			toRemoveDuplicatedWith.removeAll(duplicatedWith);
			for (Issue i : toRemoveDuplicatedWith){
				issue.deleteRelation(user, new IssueRelation(Type.DuplicatedWith, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.DuplicatedWith, i, issue));
			}
	}
}
