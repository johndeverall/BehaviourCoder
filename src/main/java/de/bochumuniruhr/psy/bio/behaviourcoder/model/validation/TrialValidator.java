package de.bochumuniruhr.psy.bio.behaviourcoder.model.validation;

import java.util.ArrayList;
import java.util.List;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails;

public class TrialValidator {

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
