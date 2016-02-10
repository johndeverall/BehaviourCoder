package de.bochumuniruhr.psy.bio.behaviourcoder.model.validation;

import java.util.ArrayList;
import java.util.List;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;

/**
 * Validator for trials to ensure that the trial is prepared to be saved.
 */
public class TrialValidator {

	/**
	 * Checks whether the trial is valid by returning a list of all errors. 
	 * When there are no errors it is valid.
	 * 
	 * @param trial - the trial to check. The trial will be invalid if it is still running,
	 * 		doesn't have a date, or there is a detail that does not hold to its constraint
	 * @return The list of errors.
	 */
	public static List<ValidationError> validate(Trial trial){
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		//Trial errors
		if (trial.isRunning()) { 
			errors.add(new ValidationError("Trial is still running."));
		}
		
		//Detail errors
		TrialDetails details = trial.getDetails();
		
		if (details.getDate() == null) { 
			errors.add(new ValidationError("Date cannot be empty."));
		}
		
		for (String detail : details.getDetailNames()){
			if (!details.isValid(detail)) { 
				String message = "Invalid " + detail + ".";
				if (details.getConstraint(detail).holdsForAny()){
					message = detail + " cannot be empty.";
				}
				errors.add(new ValidationError(message));
			}
		}
		
		return errors;
	}
}
