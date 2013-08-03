package ar.edu.itba.it.paw.validators;

public class EditIssueFormValidator extends IssueFormValidator {
	private static EditIssueFormValidator instance = new EditIssueFormValidator();
	
	private EditIssueFormValidator(){}

	public static EditIssueFormValidator getInstance(){
		return instance;
	}
}
