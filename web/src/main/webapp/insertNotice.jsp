<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!--
    1. 넘어오은 값 한글 인코딩 처리한다.
    2. 사용할 객체 useBean한다
    3. setXxx를 호출하여 넘어오는 값들 저장한다.
    3. dao쪽의 insert하는 메소드 호출하여 성공 여부를 리턴한후
       성공하면 memberSelect.jsp 이동 실패하면 뒤로 이동 시킨다.
 -->
 
<%response.setContentType("text/html;charset=utf-8");%>
 
    <jsp:useBean id="vo" class="darktour.NoticeVO" />
    <jsp:setProperty property="*" name="vo"/>
    <jsp:useBean id="dao" class="darktour.JDBC_noticeDAO"/>
   
 <%
    if(dao.noticeInsert(vo)>0){
   
        out.print("<script>");
        out.print("alert('등록완료!');");  
        out.print("location.href='manage_notice.jsp';");        
        out.print("</script>");
     }else{
       
        out.print("<script>");
        out.print("alert('공지사항 등록이 정상적으로 완료되지 않았습니다.');");  
        out.print("history.back();");          
        out.print("</script>");
    }
 %>
 
</body>
</html>
