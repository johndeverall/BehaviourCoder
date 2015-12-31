package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;

/**
 * Represents a behaviour that a subject can demonstrate during a trial.
 */
public abstract class Behaviour {
	
	/**
	 * The name of the behaviour.
	 */
	private String name;
	
	/**
	 * The colour used to represent the behaviour.
	 */
	private Color color;
	
	/**
	 * The trial the behaviour is for.
	 */
	protected Trial trial;
	
	/**
	 * Whether the the behaviour should be use location to distinguish occurrences.
	 */
	protected boolean associateWithLocation;
	
	/**
	 * Creates a behaviour.
	 * 
	 * @param name - the name of the behaviour
	 * @param color - the colour of the behaviour
	 * @param trial - the trial the behaviour belongs to
	 * @param associateWithLocation - whether the behaviour should associate occurrences with locations
	 */
	public Behaviour(String name, Color color, Trial trial, boolean associateWithLocation){
		this.name = name;
		this.color = color;
		this.trial = trial;
		this.associateWithLocation = associateWithLocation;
	}
	
	/**
	 * Gets the name of the behaviour.
	 * 
	 * @return The name of the behaviour.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the colour used to represent the behaviour.
	 * 
	 * @return The colour of the behaviour.
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Gets whether this behaviour uses location to distinguish occurrences.
	 * 
	 * @return Whether occurrences are associated with locations.
	 */
	public boolean isAssociatedWithLocation(){
		return associateWithLocation;
	}
}
