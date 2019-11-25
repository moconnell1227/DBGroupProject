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
        this.rID = null;
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
        this.rID = rID;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rate = rate;
        this.numOcc = numOcc;
        this.roomCode = roomCode;
        this.customerId = customerId;
        this.cardNum = cardNum;
    }

    public Integer getRID() {
        return rID;
    }

    public void setRID(Integer rID) {
        this.rID = rID;
    }

    public String getCheckIn() {
        return this.checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return this.checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getNumOcc() {
        return numOcc;
    }

    public void setNumOcc(Integer numOcc) {
        this.numOcc = numOcc;
    }

    public String getRoomCode() {
        return this.roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    public String toString() {
        return "rID: " + rID.toString() + ", checkIn: " + checkIn +
         ", checkOut: " + checkOut + ", rate: " + rate.toString() + ", numOcc: "
         + numOcc + ", roomCode: " + roomCode + ", customerId: " +
         customerId.toString() + ", cardNum: " + cardNum.toString();
    }
}
