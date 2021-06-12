package darktour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
import darktour.NoticeVO;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class JDBC_noticeDAO {
 
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
    public JDBC_noticeDAO(){
       
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
    }//JDBC_memberDAO()
   
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
     * notice 테이블에 insert하는 메소드 작성 NoticeVO
     */
    public int noticeInsert(NoticeVO vo){
        int result = 0;
    	Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
    	String datestr = format1.format(cal.getTime());
       
        try{
        //실행
            String sql = "INSERT INTO notice(NO_CONTENTS, NO_DATE) VALUES(?,?)";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, vo.getCONTENTS());
            ps.setString(2, datestr);

            result = ps.executeUpdate();
           
        }catch (Exception e){
           
            System.out.println(e + "=> noticeInsert fail");
           
        }finally{
            db_close();
        }
       
        return result;
    }//noticeInsert
   
    /**
     * notice 테이블의 모든 레코드 검색하(Select)는 메서드 작성
     */   
    public ArrayList<NoticeVO> getNoticelist(){
       
        ArrayList<NoticeVO> list = new ArrayList<NoticeVO>();
       
        try{//실행
            st = con.createStatement();
            rs = st.executeQuery("select * from notice");
           
            while(rs.next()){
                NoticeVO vo = new NoticeVO();
                
               vo.setNUM(rs.getInt(1));
                vo.setCONTENTS(rs.getString(2));
                vo.setDATE(rs.getString(3));
               
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getNotice fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getNoticelist
    
    public int delNoticelist(String id){
        int result = 0;
        try{//실행
           
            ps = con.prepareStatement("delete from notice where NO_NUM=?");
            //?개수만큼 값 지정
            ps.setString(1, id.trim());
            result = ps.executeUpdate(); //쿼리실행으로 삭제된 레코드 수 반환       
               
        }catch(Exception e){           
            System.out.println(e+"=> delNotice fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }//delNoticelist  
}