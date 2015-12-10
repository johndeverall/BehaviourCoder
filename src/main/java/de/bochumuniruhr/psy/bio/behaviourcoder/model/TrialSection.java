package de.bochumuniruhr.psy.bio.behaviourcoder.model;

import java.util.Date;

public class TrialSection { 

	private Date date;
	private String roosterId;
	private String trialType;
	private String sessionNumber;
	private String trialNumber;
	private String sectionNumber;
	private String mirror;
	private Integer pecksClose;
	private Integer pecksFar;
	private Integer attacksClose;
	private Integer attacksFar;
	private Integer hacklesClose;
	private Integer hacklesFar;
	private Integer crouchesClose;
	private Integer crouchesFar;
	private Double followingClose;
	private Double followingFar;
	private Double facingAwayClose;
	private Double facingAwayFar;
	private Double groomMarkClose;
	private Double groomMarkFar;
	private Double groomOtherClose;
	private Double groomOtherFar;
	private Double close;
	private Double far;
	private Integer locationChanges;
	private double trialSectionStart;

//	header.add("Rooster_ID");
//	header.add("Trial_Type");
//	header.add("Session_Num");
//	header.add("Trial_Num");
//	header.add("Section_Num");
//	header.add("Mirror");
//	header.add("Pecks");
//	header.add("Attacks");
//	header.add("Hackles");
//	header.add("Crouch");
//	header.add("Following");
//	header.add("Facing_Away");
//	header.add("Groom_Mark");
//	header.add("Groom_Other");
//	header.add("Close");
//	header.add("Far");
//	header.add("Location_Change");
	
	public TrialSection() { 
	}

	public void setFollowing(double parseDouble, int i) {
		
	}

	public Date getDate() {
		return date;
	}

	public String getRoosterId() {
		return roosterId;
	}

	public String getTrialType() {
		return trialType;
	}

	public String getSessionNumber() {
		return sessionNumber;
	}

	public String getTrialNumber() {
		return trialNumber;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public String getMirror() {
		return mirror;
	}

	public Integer getPecksClose() {
		return pecksClose;
	}

	public Integer getPecksFar() {
		return pecksFar;
	}

	public Integer getAttacksClose() {
		return attacksClose;
	}

	public Integer getAttacksFar() {
		return attacksFar;
	}

	public Integer getHacklesClose() {
		return hacklesClose;
	}

	public Integer getHacklesFar() {
		return hacklesFar;
	}

	public Integer getCrouchClose() {
		return crouchesClose;
	}

	public Integer getCouchFar() {
		return crouchesFar;
	}

	public Double getFollowingClose() {
		return followingClose;
	}

	public Double getFollowingFar() {
		return followingFar;
	}

	public Double getFacingAwayClose() {
		return facingAwayClose;
	}

	public Double getFacingAwayFar() {
		return facingAwayFar;
	}

	public Double getGroomMarkClose() {
		return groomMarkClose;
	}

	public Double getGroomMarkFar() {
		return groomMarkFar;
	}

	public Double getOtherClose() {
		return groomOtherClose;
	}

	public Double getOtherFar() {
		return groomOtherFar;
	}

	public Double getClose() {
		return close;
	}

	public Double getFar() {
		return far;
	}

	public Integer getLocationChanges() {
		return locationChanges;
	}
	
	public Double getTrialSectionStart() { 
		return trialSectionStart;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTrialType(String trialType) {
		this.trialType = trialType;
	}

	public void setRoosterId(String roosterId) {
		this.roosterId = roosterId;
	}

	public void setSessionNumber(String sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public void setMirror(String mirror) {
		this.mirror = mirror;
	}

	public void setPecksClose(Integer pecksClose) {
		this.pecksClose = pecksClose;
	}
	
	public void setPecksFar(Integer pecksFar) { 
		this.pecksFar = pecksFar;
	}

	public void setHacklesClose(Integer hacklesClose) {
		this.hacklesClose = hacklesClose;
	}

	public void setHacklesFar(Integer hacklesFar) {
		this.hacklesFar = hacklesFar;
	}

	public void setAttacksClose(Integer attacksClose) {
		this.attacksClose = attacksClose;
	}

	public void setAttacksFar(Integer attacksFar) {
		this.attacksFar = attacksFar;
	}

	public void setCrouchesClose(Integer crouchesClose) {
		this.crouchesClose = crouchesClose;
	}

	public void setCrouchesFar(Integer crouchesFar) {
		this.crouchesFar = crouchesFar;
	}

	public void setClose(Double close) {
		this.close = close;
	}
	
	public void setFar(Double far) { 
		this.far = far;
	}

	public void setFollowingClose(Double followingClose) {
		this.followingClose = followingClose;
	}

	public void setFollowingFar(Double followingFar) {
		this.followingFar = followingFar;
	}

	public void setFacingAwayClose(Double facingAwayClose) {
		this.facingAwayClose = facingAwayClose;
	}

	public void setFacingAwayFar(Double facingAwayFar) {
		this.facingAwayFar = facingAwayFar;
	}

	public void setGroomMarkClose(Double groomMarkClose) {
		this.groomMarkClose = groomMarkClose;
	}

	public void setGroomMarkFar(Double groomMarkFar) {
		this.groomMarkFar = groomMarkFar;
	}

	public void setOtherClose(Double groomOtherClose) {
		this.groomOtherClose = groomOtherClose;
	}

	public void setOtherFar(Double groomOtherFar) {
		this.groomOtherFar = groomOtherFar;
	}

	public void setLocationChanges(int locationChanges) {
		this.locationChanges = locationChanges;
	}

	public void setTrialSectionStart(double trialSectionStart) {
		this.trialSectionStart = trialSectionStart;
	}

}
