package com.example.umborno.model.current_weather_model;

import androidx.room.Embedded;

public class TemperatureBean {
    @Embedded
    private MetricBean Metric;
    @Embedded
    private ImperialBean Imperial;

    public MetricBean getMetric() {
        return Metric;
    }

    public void setMetric(MetricBean Metric) {
        this.Metric = Metric;
    }

    public ImperialBean getImperial() {
        return Imperial;
    }

    public void setImperial(ImperialBean Imperial) {
        this.Imperial = Imperial;
    }
}
