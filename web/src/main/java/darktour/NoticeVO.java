package darktour;


public class NoticeVO {
/**
 * 필요한 property 선언
 */
    private String CONTENTS;
    private String DATE;
    private int NUM;
   
    public NoticeVO(){}
    public NoticeVO(Integer NUM, String CONTENTS, String DATE){
       
    	this.NUM = NUM;
        this.CONTENTS = CONTENTS;
        this.DATE = DATE; 
    }
   
   
    public Integer getNUM() {
        return NUM;
    }
    public void setNUM(Integer NUM) {
        this.NUM = NUM;
    }
    
    public String getCONTENTS() {
        return CONTENTS;
    }
    public void setCONTENTS(String CONTENTS) {
        this.CONTENTS = CONTENTS;
    }
    public String getDATE() {
        return DATE;
    }
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
