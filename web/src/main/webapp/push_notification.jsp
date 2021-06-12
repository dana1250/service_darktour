<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.android.gcm.server.*"%>
 
<%
    ArrayList<String> token = new ArrayList<String>();    //token값을 ArrayList에 저장
    String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
    boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지
    int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간
    int RETRY = 2;    //메시지 전송실패시 재시도 횟수
 
    
    String simpleApiKey = "AAAA_8a-iYM:APA91bHlb7iRuyqKpS2DU8XXyFRnbMKq6pwJL_plpdzpzLjq4WGm2JFgiCArDKIhF1wF4f7cGU5jNCL17JIa7tk2f_hL3RFiZIZnnJb77wKK64wTZt0iYfUPO7PHbrC18DZ50f_4z_Kj";
    String gcmURL = "https://android.googleapis.com/fcm/send";    
    Connection conn = null; 
    Statement stmt = null; 
    ResultSet rs = null;
    
    String msg = request.getParameter("message");;
    
    if(msg==null || msg.equals("")){
        msg="";
    }
    
    msg = new String(msg.getBytes("UTF-8"), "UTF-8");   //메시지 한글깨짐 처리
    
    try {
    	 //MySQL
        String driverName="com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/darktour";
        String id = "root";
        String pwd ="0797";
        conn = DriverManager.getConnection(url,id,pwd);
        stmt = conn.createStatement();            
        String sql = "select token from users";
        rs = stmt.executeQuery(sql);
        
        //모든 등록ID를 리스트로 묶음
        while(rs.next()){
            token.add(rs.getString("Token"));
        }
        conn.close();
        
        Sender sender = new Sender(simpleApiKey);
        Message message = new Message.Builder()
        .collapseKey(MESSAGE_ID)
        .delayWhileIdle(SHOW_ON_IDLE)
        .timeToLive(LIVE_TIME)
        .addData("message",msg)
        .build();
        MulticastResult result1 = sender.send(message,token,RETRY);
        if (result1 != null) {
            List<Result> resultList = result1.getResults();
            for (Result result : resultList) {
                System.out.println(result.getErrorCodeName()); 
            }
        }
    }catch (Exception e) {
        e.printStackTrace();
    }
%>