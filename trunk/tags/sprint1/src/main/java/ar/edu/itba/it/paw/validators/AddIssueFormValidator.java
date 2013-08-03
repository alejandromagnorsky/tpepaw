package ar.edu.itba.it.paw.validators;

public class AddIssueFormValidator extends IssueFormValidator {
	private static AddIssueFormValidator instance = new AddIssueFormValidator();
	
	private AddIssueFormValidator(){}

	public static AddIssueFormValidator getInstance(){
		return instance;
	}
}
