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
     * �ʿ��� property ����
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
     * �ε�� ������ ���� ������ �ۼ�
     */
    public JDBC_noticeDAO(){
       
        try {
            //�ε�
            Class.forName(driverName);
           
            //����
            con = DriverManager.getConnection(url,id,pwd);      
           
        } catch (ClassNotFoundException e) {
           
            System.out.println(e+"=> �ε� ����");
           
        } catch (SQLException e) {
           
            System.out.println(e+"=> ���� ����");
        }
    }//JDBC_memberDAO()
   
    /**
     * DB�ݱ� ��� �޼ҵ� �ۼ�
     */
    public void db_close(){
       
        try {
           
            if (rs != null ) rs.close();
            if (ps != null ) ps.close();      
            if (st != null ) st.close();
       
        } catch (SQLException e) {
            System.out.println(e+"=> �ݱ� ����");
        }      
       
    } //db_close
   
    /**
     * notice ���̺� insert�ϴ� �޼ҵ� �ۼ� NoticeVO
     */
    public int noticeInsert(NoticeVO vo){
        int result = 0;
    	Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
    	String datestr = format1.format(cal.getTime());
       
        try{
        //����
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
     * notice ���̺��� ��� ���ڵ� �˻���(Select)�� �޼��� �ۼ�
     */   
    public ArrayList<NoticeVO> getNoticelist(){
       
        ArrayList<NoticeVO> list = new ArrayList<NoticeVO>();
       
        try{//����
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
        try{//����
           
            ps = con.prepareStatement("delete from notice where NO_NUM=?");
            //?������ŭ �� ����
            ps.setString(1, id.trim());
            result = ps.executeUpdate(); //������������ ������ ���ڵ� �� ��ȯ       
               
        }catch(Exception e){           
            System.out.println(e+"=> delNotice fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }//delNoticelist  
}