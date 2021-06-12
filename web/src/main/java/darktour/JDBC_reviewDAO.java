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
    public JDBC_reviewDAO(){
       
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
    }//JDBC_reviewDAO()
   
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
     * member���̺��� ��� ���ڵ� �˻���(Select)�� �޼��� �ۼ�
     */   
    public ArrayList<ReviewVO> getReviewlist(){
       
        ArrayList<ReviewVO> list = new ArrayList<ReviewVO>();
       
        try{//����
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
        try{//����
           
            ps = con.prepareStatement("delete from all_review where REVIEW_NUM = ?");
            //?������ŭ �� ����
            ps.setInt(1, REVIEW_NUM);
            result = ps.executeUpdate(); //������������ ������ ���ڵ� �� ��ȯ       
               
        }catch(Exception e){           
            System.out.println(e+"=> delReviewlist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  //delReviewlist
}