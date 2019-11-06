package com.example.umborno.model.reminder_model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reminder{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    @Embedded
    private ReminderDate dateTime;
    private String locationKey;
    private String repeat;
    private String alert;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReminderDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(ReminderDate dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }


}
