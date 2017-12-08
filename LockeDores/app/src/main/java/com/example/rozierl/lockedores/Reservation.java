package com.example.rozierl.lockedores;

/**
 * Created by rozierl on 12/7/17.
 */

public class Reservation {
    private String building;
    private String date;
    private String time;

    public Reservation() {}

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
