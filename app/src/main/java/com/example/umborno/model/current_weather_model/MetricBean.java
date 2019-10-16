package com.example.umborno.model.current_weather_model;

import androidx.room.ColumnInfo;

public class MetricBean {
    @ColumnInfo(name = "metric_value")
    private double Value;
    @ColumnInfo(name = "metric_unit")
    private String Unit;
    @ColumnInfo(name = "metric_unit_type")
    private int UnitType;

    public double getValue() {
        return Value;
    }

    public void setValue(double Value) {
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
