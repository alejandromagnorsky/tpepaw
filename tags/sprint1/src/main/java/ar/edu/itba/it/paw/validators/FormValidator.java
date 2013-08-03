package ar.edu.itba.it.paw.validators;

import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public abstract class FormValidator implements Validator {
	private static final int MAX_DESCRIPTION_LENGTH = 255;
	protected static final ProjectServiceImpl PS = ProjectServiceImpl.getInstance();
	protected static final UserServiceImpl US = UserServiceImpl.getInstance();
	
	protected boolean validateRequiredField(String value){
		return value.length() != 0;
	}
	
	protected boolean validateExistingUser(String username){
		return US.getUser(username) != null;
	}
	
	/* For now, we are not using this due to javascript textarea validation. */
	protected boolean validateDescriptionLength(String description){
		return description.length() <= MAX_DESCRIPTION_LENGTH;
	}
	
}
