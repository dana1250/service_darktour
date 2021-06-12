package darktour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
import darktour.HistoricVO;
 
public class JDBC_historicDAO {
 
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
    public JDBC_historicDAO(){
       
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
    }//JDBC_historicDAO()
   
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
     * historic테이블의 모든 레코드 검색하(Select)는 메서드 작성
     */   
    public ArrayList<HistoricVO> getHistoriclist(){
       
        ArrayList<HistoricVO> list = new ArrayList<HistoricVO>();
       
        try{//실행
            st = con.createStatement();
            rs = st.executeQuery("select * from historic_info");
           
            while(rs.next()){
            	HistoricVO vo = new HistoricVO();
               
                vo.setHISTORIC_NUM(rs.getInt(1));
                vo.setNAME(rs.getString(4));
                vo.setEXPLAIN_HIS(rs.getString(6));
                vo.setADDRESS(rs.getString(7));
               
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getHistoriclist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getHistoriclist
    
    public int delHistoriclist(int HISTORIC_NUM){
        int result = 0;
        try{//실행
           
            ps = con.prepareStatement("delete from historic_info where HISTORIC_NUM = ?");
            //?개수만큼 값 지정
            ps.setInt(1, HISTORIC_NUM);
            result = ps.executeUpdate(); //쿼리실행으로 삭제된 레코드 수 반환       
               
        }catch(Exception e){           
            System.out.println(e+"=> delHistoriclist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  //delHistoriclist
}