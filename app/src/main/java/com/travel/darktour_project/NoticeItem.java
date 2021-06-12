package com.travel.darktour_project;

public class NoticeItem {
    private String numstr ;
    private String datestr ;
    private String contentsstr ;

    public void setNum(String num) {
        numstr = num ;
    }
    public void setDate(String date) {
        datestr = date ;
    }
    public void setContents(String contents) { contentsstr = contents ; }

    public String getNum() {
        return this.numstr ;
    }
    public String getDate() {
        return this.datestr ;
    }
    public String getContents() {
        return this.contentsstr ;
    }
}
