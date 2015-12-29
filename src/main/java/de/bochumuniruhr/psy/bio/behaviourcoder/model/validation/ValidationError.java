package de.bochumuniruhr.psy.bio.behaviourcoder.model.validation;

public class ValidationError {

	private String message;
	
	ValidationError(String message) { 
		this.message = message;
	}
	
	public String getMessage() { 
		return this.message;
	}
	
}
