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
    public JDBC_historicDAO(){
       
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
    }//JDBC_historicDAO()
   
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
     * historic���̺��� ��� ���ڵ� �˻���(Select)�� �޼��� �ۼ�
     */   
    public ArrayList<HistoricVO> getHistoriclist(){
       
        ArrayList<HistoricVO> list = new ArrayList<HistoricVO>();
       
        try{//����
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
        try{//����
           
            ps = con.prepareStatement("delete from historic_info where HISTORIC_NUM = ?");
            //?������ŭ �� ����
            ps.setInt(1, HISTORIC_NUM);
            result = ps.executeUpdate(); //������������ ������ ���ڵ� �� ��ȯ       
               
        }catch(Exception e){           
            System.out.println(e+"=> delHistoriclist fail");         
        }finally{          
            db_close();
        }      
       
        return result;
    }  //delHistoriclist
}