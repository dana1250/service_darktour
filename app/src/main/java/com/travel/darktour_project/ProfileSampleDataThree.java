package com.travel.darktour_project;

import java.util.ArrayList;

public class ProfileSampleDataThree {
    private String startlocal; // 출발지
    private String finishlocal; // 도착지
    private String time; // 총 시간

    public ProfileSampleDataThree(String startlocal, String finishlocal, String time) {
        this.startlocal = startlocal;
        this.finishlocal = finishlocal;
        this.time = time;
    }

    public String getStart() { return this.startlocal; }

    public String getFinish() { return this.finishlocal; }

    public String getTime() { return this.time; }
}
