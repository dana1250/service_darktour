package com.travel.darktour_project;

public class Profile3 {
    private String startlocal;
    private String finishlocal;
    private String traffictime;

    public Profile3(String startlocal, String finishlocal, String traffictime){
        this.startlocal=startlocal;
        this.finishlocal=finishlocal;
        this.traffictime=traffictime;
    }
    public String getStartlocal(){
        return startlocal;
    }
    public String getFinishlocal(){
        return finishlocal;
    }
    public String getTraffictime(){
        return traffictime;
    }
}
