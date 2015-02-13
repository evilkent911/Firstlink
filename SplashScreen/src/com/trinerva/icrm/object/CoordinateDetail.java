package com.trinerva.icrm.object;

public class CoordinateDetail {
	private String _id;
	private String internal_num;
	private String server_id;
	private String active;
	private String first_name;
	private String last_name;
	private String company_name;
	private String city;
	private String country;
	private String state;
	private String street;
	private String zip;
	private double latitude;
	private double longitude;
	private String type;
	private String photo;
	private double distance = -1.0;
	
	public void setId(String strId) {
		this._id = strId;
	}
	
	public void setInternalNum(String strInternalNum) {
		this.internal_num = strInternalNum;
	}
	
	public void setServerId(String strServerId) {
		this.server_id = strServerId;
	}
	
	public void setActive(String strActive) {
		this.active = strActive;
	}
	
	public void setFirstName(String strFirstName) {
		this.first_name = strFirstName;
	}
	
	public void setLastName(String strLastName) {
		this.last_name = strLastName;
	}
	
	public void setCompanyName(String strCompanyName) {
		this.company_name = strCompanyName;
	}
	
	public void setCity(String strCity) {
		this.city = strCity;
	}
	
	public void setCountry(String strCountry) {
		this.country = strCountry;
	}
	
	public void setState(String strState) {
		this.state = strState;
	}
	
	public void setStreet(String strStreet) {
		this.street = strStreet;
	}
	
	public void setZip(String strZip) {
		this.zip = strZip;
	}
	
	public void setLatitude(double dLatitude) {
		this.latitude = dLatitude;
	}
	
	public void setLongitude(double dLongitude) {
		this.longitude = dLongitude;
	}
	
	public void setType(String strType) {
		this.type = strType;
	}
	
	public void setPhoto(String strPhoto) {
		this.photo = strPhoto;
	}
	
	public void setDistance(double dDistance) {
		this.distance = dDistance;
	}
	
	public String getId() {
		return this._id;
	}
	
	public String getInternalNum() {
		return this.internal_num;
	}
	
	public String getServerId() {
		return this.server_id;
	}
	
	public String getActive() {
		return this.active;
	}
	
	public String getFirstName() {
		return this.first_name;
	}
	
	public String getLastName() {
		return this.last_name;
	}
	
	public String getCompanyName() {
		return this.company_name;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getPhoto() {
		return this.photo;
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	public String toString() {
		return "_id: " + _id +
				" internal_num: " + internal_num +
				" server_id: " + server_id +
				" active: " + active +
				" first_name: " + first_name +
				" last_name: " + last_name +
				" company_name: " + company_name +
				" city: " + city +
				" country: " + country +
				" state: " + state +
				" street: " + street +
				" zip: " + zip +
				" latitude: " + latitude +
				" longitude: " + longitude +
				" type: " + type +
				" photo: " + photo +
				" distance: " + distance;
	}
}
