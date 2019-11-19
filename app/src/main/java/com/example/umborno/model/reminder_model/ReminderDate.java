package com.example.umborno.model.reminder_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ReminderDate implements Parcelable {
    private int year=-1,month=-1,day=-1,dayOfWeek=-1,hour=-1,minute=-1;
    @Ignore
    private String date="";
    @Ignore
    private String time="";
    @Ignore
    private Calendar mCalendar;

    public ReminderDate() {
        mCalendar = Calendar.getInstance();
    }

    protected ReminderDate(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        dayOfWeek = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        date = in.readString();
        time = in.readString();
    }

    public static final Creator<ReminderDate> CREATOR = new Creator<ReminderDate>() {
        @Override
        public ReminderDate createFromParcel(Parcel in) {
            return new ReminderDate(in);
        }

        @Override
        public ReminderDate[] newArray(int size) {
            return new ReminderDate[size];
        }
    };

    public int getYear() {
        if(year==-1){
           year = mCalendar.get(Calendar.YEAR);
        }
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        if(month==-1){
            month = mCalendar.get(Calendar.MONTH);
        }
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        if(day==-1){
            day = mCalendar.get(Calendar.DAY_OF_MONTH);
        }
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayOfWeek() {
        if(dayOfWeek==-1){
            dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        }
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() {
        if(hour==-1){
            hour = mCalendar.get(Calendar.HOUR);
        }
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        if(minute==-1){
            minute = mCalendar.get(Calendar.MINUTE);
        }
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDate() {
        date = getYear() + " "+getMonthNameForInt(getMonth())+" "+getDay();
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        time = getDayOfWeekNameForInt(getDayOfWeek())+" "+getHour()+":"+getMinute();
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String getMonthNameForInt(int m){
        String mMonth = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortMonths();
        if(m>=0&&m<=11){
            mMonth = months[m];
        }
        return mMonth;
    }

    private String getDayOfWeekNameForInt(int d){
        String mDay = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] daysOfWeek = dfs.getWeekdays();
        if(d>=1&&d<=7){
            mDay = daysOfWeek[d];
        }
        return mDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(dayOfWeek);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeString(date);
        dest.writeString(time);
    }
}
