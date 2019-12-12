package entity;

public class Reservation {
    private Integer rID;
    private String checkIn;
    private String checkOut;
    private Float rate;
    private Integer numOcc;
    private String roomCode;
    private Integer customerId;
    private Integer cardNum;

    public Reservation(String checkIn, String checkOut,
                       Float rate, Integer numOcc, String roomCode, Integer customerId,
                       Integer cardNum) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rate = rate;
        this.numOcc = numOcc;
        this.roomCode = roomCode;
        this.customerId = customerId;
        this.cardNum = cardNum;
    }

    public Reservation(Integer rId, String checkIn, String checkOut,
                       Float rate, Integer numOcc, String roomCode, Integer customerId,
                       Integer cardNum) {
        this.rID = rId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rate = rate;
        this.numOcc = numOcc;
        this.roomCode = roomCode;
        this.customerId = customerId;
        this.cardNum = cardNum;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public Float getRate() {
        return rate;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getNumOcc() {
        return numOcc;
    }

    public Integer getrID() {
        return rID;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setNumOcc(Integer numOcc) {
        this.numOcc = numOcc;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "rID: " + rID + ", checkIn: " + checkIn +
                ", checkOut: " + checkOut + ", rate: " + rate + ", numOcc: "
                + numOcc + ", roomCode: " + roomCode + ", customerId: " +
                customerId + ", cardNum: " + cardNum;
    }
}
