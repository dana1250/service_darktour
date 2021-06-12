package com.travel.darktour_project;

// 리사이클러 코스선택 data
public class FavoriteData {
    private String title;
    private String desc;
    private boolean press_start;
    private boolean press_finish;
    private int start_back;
    private int finish_back;
    private int start_text;
    private int finish_text;


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


    public boolean isPress_start() {
        return press_start;
    }

    public void setPress_start(boolean press_start) {
        this.press_start = press_start;
    }



    public int getStart_back() {
        return start_back;
    }

    public void setStart_back(int start_back) {
        this.start_back = start_back;
    }

    public boolean isPress_finish() {
        return press_finish;
    }

    public void setPress_finish(boolean press_finish) {
        this.press_finish = press_finish;
    }

    public int getFinish_back() {
        return finish_back;
    }

    public void setFinish_back(int finish_back) {
        this.finish_back = finish_back;
    }

    public int getStart_text() {
        return start_text;
    }

    public void setStart_text(int start_text) {
        this.start_text = start_text;
    }

    public int getFinish_text() {
        return finish_text;
    }

    public void setFinish_text(int finish_text) {
        this.finish_text = finish_text;
    }

}
