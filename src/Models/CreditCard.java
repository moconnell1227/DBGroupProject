package edu.calpoly.csc365.group01.entity;

public class CreditCard {
    private int cardNum;
    private float balance;
    private float cardLimit;
    private int ownerId;

    public CreditCard(int cardNum, float balance, float cardLimit, int ownerId) {
        this.cardNum = cardNum;
        this.balance = balance;
        this.cardLimit = cardLimit;
        this.ownerId = ownerId;
    }

    public CreditCard(float balance, float cardLimit, int ownerId) {
        this.balance = balance;
        this.cardLimit = cardLimit;
        this.ownerId = ownerId;
    }

    public int getCardNum() {
        return cardNum;
    }

    public float getBalance() {
        return balance;
    }

    public float getCardLimit() {
        return cardLimit;
    }

    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return "Credit Card:" +
                "\nCard Number: " + cardNum +
                "\nBalance: " + balance +
                "\nCard Limit: " + cardLimit +
                "\nOwner ID: " + ownerId;
    }
}
