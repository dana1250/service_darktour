package com.travel.darktour_project;
// 리사이클러 유적지 data


public class SiteData{


    private String desc;
    private String title;
    private String image; // bitmap
    private String like; // 따봉숫자
    private String accident_text; // 사건
    private boolean isSelected = false;
    private int layout_;
    private double latitude;
    private double longitude;

    public SiteData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public int getLayout_() {
        return layout_;
    }

    public void setLayout_(int layout_) {
        this.layout_ = layout_;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAccident_text() {
        return accident_text;
    }

    public void setAccident_text(String accident_text) {
        this.accident_text = accident_text;
    }
}