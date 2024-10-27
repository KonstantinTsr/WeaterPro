package com.example.weaterpro;

public class WeatherData {
    private String date;
    private String temperature;
    private String apparentTemperature;
    private String rain;
    private String surfacePressure;

    public WeatherData(String date, String temperature, String apparentTemperature, String rain, String surfacePressure) {
        this.date = date;
        this.temperature = temperature;
        this.apparentTemperature = apparentTemperature;
        this.rain = rain;
        this.surfacePressure = surfacePressure;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getApparentTemperature() {
        return apparentTemperature;
    }

    public String getRain() {
        return rain;
    }

    public String getSurfacePressure() {
        return surfacePressure;
    }
}
