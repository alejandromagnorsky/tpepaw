package ar.edu.itba.it.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "issuerelation", uniqueConstraints={
		 @UniqueConstraint(columnNames={"type", "issuea", "issueb"})})
public class IssueRelation extends AbstractModel {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Type type;
	
	@ManyToOne
	@JoinColumn(name = "issuea", nullable = false)
	private Issue issueA;
	
	@ManyToOne
	@JoinColumn(name = "issueb", nullable = false)
	private Issue issueB;
	
	public enum Type {
		DependsOn("Depende de"),
		NecessaryFor("Es necesaria para"),
		RelatedTo("Está relacionada con"),
		DuplicatedWith("Está duplicada con");
		
		String caption;
		
		Type(String caption){
			this.caption = caption;
		}
		
		public String getCaption(){
			return caption;
		}
	}
	
	IssueRelation(){
		
	}
	
	public IssueRelation(Type type, Issue issueA, Issue issueB){
		if (type == null || issueA == null || issueB == null || issueA.equals(issueB))
			throw new IllegalArgumentException();
		this.type = type;
		this.issueA = issueA;
		this.issueB = issueB;
	}

	public Type getType() {
		return type;
	}

	public Issue getIssueA() {
		return issueA;
	}

	public Issue getIssueB() {
		return issueB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((issueA == null) ? 0 : issueA.hashCode());
		result = prime * result + ((issueB == null) ? 0 : issueB.hashCode());
		result = prime * result + ((type == null) ? 0 : type.getCaption().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IssueRelation other = (IssueRelation) obj;
		if (issueA == null) {
			if (other.issueA != null)
				return false;
		} else if (!issueA.equals(other.issueA))
			return false;
		if (issueB == null) {
			if (other.issueB != null)
				return false;
		} else if (!issueB.equals(other.issueB))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.getCaption().equals(other.type.getCaption()))
			return false;
		return true;
	}
	
	
}
