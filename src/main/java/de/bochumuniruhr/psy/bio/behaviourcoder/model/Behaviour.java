package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.awt.Color;

public abstract class Behaviour {
	private String name;
	private Color color;
	protected Trial trial;
	
	public Behaviour(String name, Color color, Trial trial){
		this.name = name;
		this.color = color;
		this.trial = trial;
	}
	
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
}
