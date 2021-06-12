package darktour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
import darktour.CourseVO;
 
public class JDBC_coursesDAO {
 
    /**
     * 필요한 property 선언
     */
    Connection con;
    Statement st;
    PreparedStatement ps;
    ResultSet rs;
   
    //MySQL
    String driverName="com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/darktour";
 

    String id = "root";
    String pwd ="0797";
   
    /**
     * 로드와 연결을 위한 생성자 작성
     */
    public JDBC_coursesDAO(){
       
        try {
            //로드
            Class.forName(driverName);
           
            //연결
            con = DriverManager.getConnection(url,id,pwd);      
           
        } catch (ClassNotFoundException e) {
           
            System.out.println(e+"=> 로드 실패");
           
        } catch (SQLException e) {
           
            System.out.println(e+"=> 연결 실패");
        }
    }//JDBC_courseDAO()
   
    /**
     * DB닫기 기능 메소드 작성
     */
    public void db_close(){
       
        try {
           
            if (rs != null ) rs.close();
            if (ps != null ) ps.close();      
            if (st != null ) st.close();
       
        } catch (SQLException e) {
            System.out.println(e+"=> 닫기 오류");
        }      
       
    } //db_close
   
    /**
     * course테이블의 모든 레코드 검색하(Select)는 메서드 작성
     */   
    public ArrayList<CourseVO> getCourselist(){
       
        ArrayList<CourseVO> list = new ArrayList<CourseVO>();
       
        try{//실행
            st = con.createStatement();
            rs = st.executeQuery("select * from course");
           
            while(rs.next()){
            	CourseVO vo = new CourseVO();
               
                vo.setCOURSE_CODE(rs.getInt(1));
                vo.setCONTENTS(rs.getString(2));
               
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getCourselist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getCourselist
    
    public int delCourselist(int COURSE_CODE){
        int result = 0;
        try{//실행
           
            ps = con.prepareStatement("delete from course where COURSE_CODE = ?");
            //?개수만큼 값 지정
            ps.setInt(1, COURSE_CODE);
            result = ps.executeUpdate(); //쿼리실행으로 삭제된 레코드 수 반환       
               
        }catch(Exception e){           
            System.out.println(e+"=> delCourselist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  
}