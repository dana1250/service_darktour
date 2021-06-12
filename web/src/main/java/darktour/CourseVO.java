package darktour;


/**
 *
 * Member에 해당하는 정보에 대한 저장소 역할
 *
 */
public class CourseVO {
/**
 * 필요한 property 선언
 */
    private int COURSE_CODE ;
    private String CONTENTS;

   
    public CourseVO(){}
    public CourseVO(int COURSE_CODE, String CONTENTS){
       
        this.COURSE_CODE = COURSE_CODE;
        this.CONTENTS = CONTENTS;    
    }
	public int getCOURSE_CODE() {
		return COURSE_CODE;
	}
	public void setCOURSE_CODE(int cOURSE_CODE) {
		COURSE_CODE = cOURSE_CODE;
	}
	public String getCONTENTS() {
		return CONTENTS;
	}
	public void setCONTENTS(String cONTENTS) {
		CONTENTS = cONTENTS;
	}
   
}
