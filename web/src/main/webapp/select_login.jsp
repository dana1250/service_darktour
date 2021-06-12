<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.sql.*" %>
<%
request.setCharacterEncoding("UTF-8");
String USER_ID = request.getParameter("USER_ID");
String USER_PWD = request.getParameter("USER_PWD").trim();
String sql = "select * from users where USER_ID=?";

Connection con=null;
PreparedStatement pst = null;
ResultSet rs = null;
try{
    Class.forName("com.mysql.jdbc.Driver");
} catch(ClassNotFoundException e){
    System.out.println(e.getMessage());
}
try{
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/darktour", "root", "0797");
} catch(SQLException e){
    System.out.println(e);
}
try{
	String msg;
	if(USER_ID.equals("admin00")){
	    pst = con.prepareStatement(sql);
	    pst.setString(1,USER_ID);
	    rs = pst.executeQuery();
	    if(!(rs.next())){ 
	        out.println("<script>");
	        out.println("alert('존재하지 않는 아이디');");
	        out.println("</script>");
	        msg = "login.jsp";
	        response.sendRedirect(msg);
	    }else{
	        if(USER_PWD.equals(rs.getString("USER_PWD"))){
	            msg = "main.jsp";
	            response.sendRedirect(msg);
	        } else{
	            msg = "login.jsp";
	            response.sendRedirect(msg);
	        }
	    }
	    rs.close();
	    con.close();
	    pst.close();
	}
	else{
		msg = "login.jsp";
        response.sendRedirect(msg);
	}
} catch(SQLException e){
    System.out.println(e);
}

%>