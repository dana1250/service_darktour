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
    public JDBC_coursesDAO(){
       
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
    }//JDBC_courseDAO()
   
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
     * course���̺��� ��� ���ڵ� �˻���(Select)�� �޼��� �ۼ�
     */   
    public ArrayList<CourseVO> getCourselist(){
       
        ArrayList<CourseVO> list = new ArrayList<CourseVO>();
       
        try{//����
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
        try{//����
           
            ps = con.prepareStatement("delete from course where COURSE_CODE = ?");
            //?������ŭ �� ����
            ps.setInt(1, COURSE_CODE);
            result = ps.executeUpdate(); //������������ ������ ���ڵ� �� ��ȯ       
               
        }catch(Exception e){           
            System.out.println(e+"=> delCourselist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  
}