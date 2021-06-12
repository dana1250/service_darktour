package com.travel.darktour_project;
// 리사이클러 리뷰 data



public class ReviewData {

    private String id;
    private String review;
    private String title;
    private String image; // image url 리뷰
    private String like;
    private int tag_color; // 카테고리 색상
    private String review_num; // 리뷰 고유 num
    private String category; // 카테고리 이름
    private boolean press = false ; // 버튼 눌려졌는가
    private int thumb_image; // 따봉 이미지

    
    public ReviewData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }


    public int getTag_color() {
        return tag_color;
    }

    public void setTag_color(int tag_color) {
        this.tag_color = tag_color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    public int getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(int thumb_image) {
        this.thumb_image = thumb_image;
    }


    public String getReview_num() {
        return review_num;
    }

    public void setReview_num(String review_num) {
        this.review_num = review_num;
    }
}