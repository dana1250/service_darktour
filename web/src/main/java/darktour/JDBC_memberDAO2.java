package darktour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
import darktour.MemberVO;
 
public class JDBC_memberDAO2 {
 
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
    public JDBC_memberDAO2(){
       
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
     * member테이블에 insert하는 메소드 작성
     */
    public int memberInsert(MemberVO vo){
        int result = 0;
       
        try{
        //실행
            String sql = "INSERT INTO users VALUES(?,?,?,?)";
           
            ps = con.prepareStatement(sql);
            ps.setString(1, vo.getNAME());
            ps.setString(2, vo.getUSER_ID());
            ps.setString(3, vo.getUSER_PWD());
            ps.setString(4, vo.getFAVORITE_HIS());

            result = ps.executeUpdate();
           
        }catch (Exception e){
           
            System.out.println(e + "=> memberInsert fail");
           
        }finally{
            db_close();
        }
       
        return result;
    }//memberInsert
   
    /**
     * member테이블의 모든 레코드 검색하(Select)는 메서드 작성
     */   
    public ArrayList<MemberVO> getMemberlist(){
       
        ArrayList<MemberVO> list = new ArrayList<MemberVO>();
       
        try{//실행
            st = con.createStatement();
            rs = st.executeQuery("select * from users");
           
            while(rs.next()){
                MemberVO vo = new MemberVO();
               
                vo.setNAME(rs.getString(2));
                vo.setUSER_ID(rs.getString(1));
                vo.setUSER_PWD(rs.getString(3));
                vo.setFAVORITE_HIS(rs.getString(4));
               
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getMemberlist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getMemberlist
    
    public int delMemberlist(String id){
        int result = 0;
        try{//실행
           
            ps = con.prepareStatement("delete from users where USER_ID = ?");
            //?개수만큼 값 지정
            ps.setString(1, id.trim());
            result = ps.executeUpdate(); //쿼리실행으로 삭제된 레코드 수 반환       
               
        }catch(Exception e){           
            System.out.println(e+"=> delMemberlist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  
}