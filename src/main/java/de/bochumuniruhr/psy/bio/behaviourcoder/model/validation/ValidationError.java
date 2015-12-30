package de.bochumuniruhr.psy.bio.behaviourcoder.model.validation;

/**
 * The error that represents a reason that a trial is not valid.
 */
public class ValidationError {

	/**
	 * The message specifying the error.
	 */
	private String message;
	
	/**
	 * Creates a validation error.
	 * 
	 * @param message - describes the error.
	 */
	ValidationError(String message) { 
		this.message = message;
	}
	
	/**
	 * Gets the message that describes the error.
	 * 
	 * @return The description of the error.
	 */
	public String getMessage() { 
		return this.message;
	}
	
}
