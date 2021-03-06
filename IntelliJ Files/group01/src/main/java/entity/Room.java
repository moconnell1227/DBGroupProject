package entity;

import java.math.BigDecimal;

public class Room {
    private String code;
    private String roomName;
    private Integer beds;
    private String bedType;
    private Integer maxOccupancy;
    private Integer basePrice;
    private String decor;
    private BigDecimal popScore;

    public Room() {
        this.code = null;
        this.roomName = null;
        this.beds = null;
        this.bedType = null;
        this.maxOccupancy = null;
        this.basePrice = null;
        this.decor = null;
    }

    public Room(String code, String roomName, Integer beds, String bedType, Integer maxOccupancy, Integer basePrice, String decor) {
        this.code = code;
        this.roomName = roomName;
        this.beds = beds;
        this.bedType = bedType;
        this.maxOccupancy = maxOccupancy;
        this.basePrice = basePrice;
        this.decor = decor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public String getDecor() {
        return decor;
    }

    public void setDecor(String decor) {
        this.decor = decor;
    }

    public BigDecimal getPopScore() {return popScore;}

    public void setPopScore(BigDecimal popScore) {this.popScore = popScore;}

    @Override
    public String toString() {
        String roomFormat = "%7.2f\t%7s\t\t%-30s %-10s %-1d\t\t%-1d\t\t%-1.2f\t\t%-15s";
        return String.format(roomFormat, this.popScore, this.code, this.roomName, this.bedType, this.beds, this.maxOccupancy, (float)this.basePrice, this.decor);

    }


}