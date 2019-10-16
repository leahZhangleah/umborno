package com.example.umborno.model.current_weather_model;

import androidx.room.ColumnInfo;

public class ImperialBean {
    @ColumnInfo(name = "imperial_value")
    private int Value;
    @ColumnInfo(name = "imperial_unit")
    private String Unit;
    @ColumnInfo(name = "imperial_unit_type")
    private int UnitType;

    public int getValue() {
        return Value;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getUnitType() {
        return UnitType;
    }

    public void setUnitType(int UnitType) {
        this.UnitType = UnitType;
    }
}
