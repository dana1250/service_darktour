package darktour;


/**
 *
 * Member�� �ش��ϴ� ������ ���� ����� ����
 *
 */
public class CourseVO {
/**
 * �ʿ��� property ����
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
