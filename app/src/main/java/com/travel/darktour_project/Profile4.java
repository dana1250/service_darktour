package com.travel.darktour_project;

public class Profile4 {
    private String date;
    private String historic_name;
    private String contents;

    public Profile4(String date, String historic_name, String contents){
        this.date=date;
        this.historic_name=historic_name;
        this.contents=contents;
    }

    public String getDate(){
        return date;
    }
    public String getHistoricName(){
        return historic_name;
    }
    public String getContents(){
        return contents;
    }
}
