package com.travel.darktour_project;

public class MyLocationData { // locationdata 저장 클래스
    private String name;
    private String lon;
    private String lat;

    public MyLocationData(String name,String lon,String lat){
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }
}