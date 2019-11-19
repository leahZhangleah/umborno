package com.example.umborno.model.reminder_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Reminder implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    @Embedded
    private ReminderDate dateTime;
    private String locationKey;
    private String repeat;
    private String alert;

    public Reminder() {
    }

    protected Reminder(Parcel in) {
        id = in.readInt();
        description = in.readString();
        dateTime = in.readParcelable(ReminderDate.class.getClassLoader());
        locationKey = in.readString();
        repeat = in.readString();
        alert = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeParcelable(dateTime,flags);
        dest.writeString(locationKey);
        dest.writeString(repeat);
        dest.writeString(alert);
    }

}
