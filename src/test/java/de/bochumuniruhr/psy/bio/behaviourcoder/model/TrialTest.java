package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;

public class TrialTest {
	
	private Trial trial;
	
	@Before
	public void initializeTrial() { 
		List<Constraint> constraints = new ArrayList<>();
		List<Location> locations = new ArrayList<Location>();
		List<String> names = new ArrayList<String>();
		int duration = 120;
		trial = new Trial(duration, locations, names, constraints);
	}
	
	@Test
	public void testOnVideoFinished() throws InterruptedException { 
		trial.onVideoLoaded(10.00); // marks the video as ready
		trial.start();
		Thread.sleep(1000);
		assertTrue(trial.getCurrentTime() >=  990);
		trial.onVideoFinished();
		Thread.sleep(1000);
		assertTrue(trial.getCurrentTime() < 1100);
	}

}
