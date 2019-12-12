package entity;

import java.util.HashMap;
import java.util.Map;
public class MonthlyRevenue {
    private int month;
    private String room;
    private int revenue;

    public MonthlyRevenue(int month, String room, int revenue) {
        this.month = month;
        this.room = room;
        this.revenue = revenue;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getRevenue() {
        return this.revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "month: " + month + ", room: " + room + ", revenue: " + revenue;
    }
}
