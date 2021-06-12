package darktour;


/**
 *
 * Member에 해당하는 정보에 대한 저장소 역할
 *
 */
public class MemberVO {
/**
 * 필요한 property 선언
 */
    private String NAME;
    private String USER_ID;
    private String USER_PWD;
    private String FAVORITE_HIS;

   
    public MemberVO(){}
    public MemberVO(String NAME, String USER_ID, String USER_PWD, String FAVORITE_HIS){
       
        this.NAME = NAME;
        this.USER_ID = USER_ID;
        this.USER_PWD = USER_PWD;
        this.FAVORITE_HIS = FAVORITE_HIS;       
    }
   
   
    public String getNAME() {
        return NAME;
    }
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    public String getUSER_ID() {
        return USER_ID;
    }
    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
    public String getUSER_PWD() {
        return USER_PWD;
    }
    public void setUSER_PWD(String USER_PWD) {
        this.USER_PWD = USER_PWD;
    }
    public String getFAVORITE_HIS() {
        return FAVORITE_HIS;
    }
    public void setFAVORITE_HIS(String FAVORITE_HIS) {
        this.FAVORITE_HIS = FAVORITE_HIS;
    }
}
