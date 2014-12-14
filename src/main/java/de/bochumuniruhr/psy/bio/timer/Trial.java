package de.bochumuniruhr.psy.bio.timer;

import java.util.Date;

public class Trial { 

	// unique identifying information
	private String subjectNumber;
	private String cohort;
	private Date date;
	private String taskName;

	private double[] zones;
	
	private int movements;
	
	private int socialCalls;
	
	private int alarmCalls;
	
	private int headBobs;
	private int zoneMovements;
	
	public Trial() { 
		zones = new double[9];
	}

	public String getSubjectNumber() {
		return subjectNumber;
	}

	public void setSubjectNumber(String subjectNumber) {
		this.subjectNumber = subjectNumber;
	}

	public String getCohort() {
		return cohort;
	}

	public void setCohort(String cohort) {
		this.cohort = cohort;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public void setZone(double value, int boxNumber) { 
		zones[boxNumber - 1] = value;
	}
	
	public double getZone(int boxNumber) { 
		return zones[boxNumber -1];
	}

	public int getMovements() {
		return movements;
	}

	public void setMovements(int movements) {
		this.movements = movements;
	}

	public int getSocialCalls() {
		return socialCalls;
	}

	public void setSocialCalls(int socialCalls) {
		this.socialCalls = socialCalls;
	}

	public int getAlarmCalls() {
		return alarmCalls;
	}

	public void setAlarmCalls(int alarmCalls) {
		this.alarmCalls = alarmCalls;
	}

	public int getHeadBobs() {
		return headBobs;
	}

	public void setHeadBobs(int headBobs) {
		this.headBobs = headBobs;
	}

	public void setZoneMovements(int zoneMovements) {
		this.zoneMovements = zoneMovements; 
	}
	
	public int getZoneMovements() { 
		return this.zoneMovements;
	}
	
	public double getTotalTime() { 
		double totalTime = 0;
		for (double zoneTime : zones) { 
			totalTime = totalTime + zoneTime;
		}
		return totalTime;
	}
	
}
