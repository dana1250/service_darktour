<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC
"-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <jsp:useBean id="dao" class="darktour.JDBC_historicDAO" />
   
    <%     
        //넘어온 ID값 받아서 삭제하는 메소드 호출
  
        int result = dao.delHistoriclist(Integer.parseInt(request.getParameter("HISTORIC_NUM")));
       
        String message="삭제되지 않았습니다.";
        if(result > 0 ){
                       
            message = "삭제되었습니다.";
        }
    %> 
   
    <script>   
        alert("<%=message%>");
        location.href="manage_historic.jsp";  
    </script>
</body>
</html>
