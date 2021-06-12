package darktour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
import darktour.ReviewVO;
 
public class JDBC_reviewDAO {
 
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
    public JDBC_reviewDAO(){
       
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
    }//JDBC_reviewDAO()
   
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
     * member테이블의 모든 레코드 검색하(Select)는 메서드 작성
     */   
    public ArrayList<ReviewVO> getReviewlist(){
       
        ArrayList<ReviewVO> list = new ArrayList<ReviewVO>();
       
        try{//실행
            st = con.createStatement();
            rs = st.executeQuery("select * from all_review");
           
            while(rs.next()){
            	ReviewVO vo = new ReviewVO();
               
                vo.setREVIEW_NUM(rs.getInt(1));
                vo.setUSER_ID(rs.getString(2));
                vo.setREVIEW_CONTENT(rs.getString(6));

                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getReviewlist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getReviewlist
    
    public int delReviewlist(int REVIEW_NUM){
        int result = 0;
        try{//실행
           
            ps = con.prepareStatement("delete from all_review where REVIEW_NUM = ?");
            //?개수만큼 값 지정
            ps.setInt(1, REVIEW_NUM);
            result = ps.executeUpdate(); //쿼리실행으로 삭제된 레코드 수 반환       
               
        }catch(Exception e){           
            System.out.println(e+"=> delReviewlist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  //delReviewlist
}