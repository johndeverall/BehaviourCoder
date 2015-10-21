package de.bochumuniruhr.psy.bio.behaviourcoder.details;

public class ValidationError {

	private String message;
	
	public ValidationError(String message) { 
		this.message = message;
	}
	
	public String getMessage() { 
		return this.message;
	}
	
}
