package edu.calpoly.csc365.group01.entity;

public class Room{
	private String roomID;
	private String roomName;
	private Integer beds;
	private String bedType;
	private Integer maxOccupancy;
	private Float basePrice;
	private String decor;

	public Room() {
		this.roomID = null;
		this.roomName = null;
		this.beds = null;
		this.bedType = null;
		this.maxOccupancy = null;
		this.basePrice = null;
		this.decor = null;
	}

	public Room(String roomID, String roomName, Integer beds, String bedType, Integer maxOccupancy, Float basePrice, String decor) {
		this.roomID = roomID;
		this.roomName = roomName;
		this.beds = beds;
		this.bedType = bedType;
		this.maxOccupancy = maxOccupancy;
		this.basePrice = basePrice;
		this.decor = decor;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getBeds() {
		return beds;
	}

	public void setBeds(Integer beds) {
		this.beds = beds;
	}

	public String getBedType() {
		return bedType;
	}

	public void setBedType(String bedType) {
		this.bedType = bedType;
	}

	public Integer getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public Float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Float basePrice) {
		this.basePrice = basePrice;
	}

	public String getDecor() {
		return decor;
	}

	public void setDecor(String decor){
		this.decor = decor;
	}

	

}