package entity;

public class Room {
    private String code;
    private String roomName;
    private Integer beds;
    private String bedType;
    private Integer maxOccupancy;
    private Integer basePrice;
    private String decor;

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

    @Override
    public String toString() {
        return "CODE: " + this.code + " Name: " + this.roomName + " Num_Beds: " + this.beds + " BedType: " + this.bedType +
                " MaxOcc: " + this.maxOccupancy + " BasePrice: " + this.basePrice + " Decor: " + this.decor;
    }
}