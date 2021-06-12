package darktour;


/**
 *
 * Member에 해당하는 정보에 대한 저장소 역할
 *
 */
public class ReviewVO {
/**
 * 필요한 property 선언
 */
    private int REVIEW_NUM ;
    private String USER_ID;
    private String REVIEW_CONTENT;

   
    public ReviewVO(){}
    public ReviewVO(int REVIEW_NUM , String USER_ID, String REVIEW_CONTENT){
       
        this.REVIEW_NUM  = REVIEW_NUM ;
        this.USER_ID = USER_ID;
        this.REVIEW_CONTENT = REVIEW_CONTENT;   
    }
	public int getREVIEW_NUM() {
		return REVIEW_NUM;
	}
	public void setREVIEW_NUM(int rEVIEW_NUM) {
		REVIEW_NUM = rEVIEW_NUM;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getREVIEW_CONTENT() {
		return REVIEW_CONTENT;
	}
	public void setREVIEW_CONTENT(String rEVIEW_CONTENT) {
		REVIEW_CONTENT = rEVIEW_CONTENT;
	}
   
}
