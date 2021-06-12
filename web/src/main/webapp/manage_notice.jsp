<%@page import="darktour.NoticeVO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>darktour &mdash; DT</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="https://fonts.googleapis.com/css?family=Lato:300,400,700" rel="stylesheet">
		<!-- Animate.css --><link rel="stylesheet" href="css/animate.css">
		<!-- Icomoon Icon Fonts--><link rel="stylesheet" href="css/icomoon.css">
		<!-- Themify Icons--><link rel="stylesheet" href="css/themify-icons.css">
		<!-- Bootstrap  --><link rel="stylesheet" href="css/bootstrap.css">
		<!-- Magnific Popup --><link rel="stylesheet" href="css/magnific-popup.css">
		<!-- Magnific Popup --><link rel="stylesheet" href="css/bootstrap-datepicker.min.css">
		<!-- Theme style  --><link rel="stylesheet" href="css/style.css">
		<!-- Modernizr JS --><script src="js/modernizr-2.6.2.min.js"></script>
				
		<script>
	    function noticeDelete(delID){
	       location.href = "deleteNotice.jsp?NO_NUM=" + encodeURI(delID,"UTF-8");   //get방식으로 삭제할아이디를 넘김     
	    }
	    function notionCreate(){
            response.sendRedirect("http://www.daum.net");    //외부 사이트로 이동

		    }
		</script>
	</head>
	<body>	
		<div id="page">
		 <!--
	        1. dao객체 선언한다.
	        2. dao쪽의 select하는 메소드를 호출하여 그 결과를 리턴하여 테이블에 예쁘게 출력한다.
	    -->
		    <nav class="gtco-nav" role="navigation">
				<div class="gtco-container">
					<div class="row">
	                    <div class="col-sm-4 col-xs-12">
							<div id="gtco-logo"><a href="main.jsp"> 역사의 발자취 관리자모드 </a></div>
	                    </div>
	                    <div class="col-xs-8 text-right menu-1">
	                        <ul>
	                            <li><a href="manage_user.jsp">사용자</a></li>
	                            <li><a href="manage_historic.jsp">유적지</a></li>
	                            <li><a href="manage_course.jsp">코스</a></li>	                            
	                            <li><a href="manage_review.jsp">리뷰</a></li>
	                            <li><a href="manage_notice.jsp">공지사항</a></li>
	                        </ul>	
	                    </div>
	                </div>                
	            </div>
	        </nav>              
               
			<body>   
				<header id="gtco-header" class="gtco-cover gtco-cover-md" role="banner" style="background-image: url(images/img_bg_2.jpg)">
					<div class="overlay"></div>
					<div class="gtco-container">
						<div class="row">
							<div class="col-md-12 col-md-offset-0 text-left">	
								<div class="row row-mt-15em">
									<div class="col-md-7 mt-text animate-box" data-animate-effect="fadeInUp">
										<h1>Notice Management</h1>	
									</div>
								</div>
							</div>
						</div>
					</div>
				</header>
	
	<div class="gtco-section">
		<div class="gtco-container">
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center gtco-heading">				
					<jsp:useBean id="dao" class="darktour.JDBC_noticeDAO" />
				    <%
				    ArrayList<NoticeVO> list = dao.getNoticelist();
				    %>
					<h2>NOTICE</h2>
				</div>
			</div>
			<div class="row">

			</div>
		</div>
		<div class="row">
			<form method="get" name="f">	
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">공지번호</th>
						<th style="background-color: #eeeeee; text-align: center;">공지내용</th>
						<th style="background-color: #eeeeee; text-align: center;">공지날짜</th>
						<th style="background-color: #eeeeee; text-align: center;"> </th>
					</tr>
				</thead>
				<tbody>
					 <%
				    for(NoticeVO vo : list){
				    %> 
			        <tr>
			            <td><%=vo.getNUM() %></td>
			            <td><%=vo.getCONTENTS() %></td>
			            <td><%=vo.getDATE() %></td>
						<td><input type="button" value="삭제" onclick="noticeDelete('<%=vo.getNUM()%>');"></td>
			        </tr>				               
				    <%
				    }
				     %>
				</tbody>
			</table>
			<input type="button" value="공지사항 등록" onclick="location.href='make_notice.jsp'">
			</form>
		</div>
		
    <footer id="gtco-footer" role="contentinfo">
		<div class="gtco-container">
			<div class="row row-p	b-md">
				<div class="col-md-4">
					<div class="gtco-widget">
						<h3>About Us</h3>
						<p>동의대학교 컴퓨터소프트웨어공학과 4학년 시크릿 혜쥬</p>
					</div>
				</div>
				<div class="col-md-2 col-md-push-1">
					<div class="gtco-widget">
						<h3>Developers</h3>
						<ul class="gtco-footer-links">
							<li><a href="#">김선희</a></li>
							<li><a href="#">백다은</a></li>
							<li><a href="#">양윤지</a></li>
							<li><a href="#">이혜주</a></li>
							<li><a href="#">황현지</a></li>
						</ul>
					</div>
				</div>
				<div class="col-md-3 col-md-push-1">
					<div class="gtco-widget">
						<h3>Get In Touch</h3>
						<ul class="gtco-quick-contact">
							<li><a href="#"><i class="icon-phone"></i> +1 234 567 890</a></li>
							<li><a href="#"><i class="icon-mail2"></i> info@freehtml5.co</a></li>
							<li><a href="#"><i class="icon-chat"></i> Live Chat</a></li>
						</ul>
					</div>
				</div>

			</div>

			<div class="row copyright">
				<div class="col-md-12">
					<p class="pull-left">
						<small class="block">&copy; 2016 Free HTML5. All Rights Reserved.</small> 
						<small class="block">Designed by <a href="https://freehtml5.co/" target="_blank">FreeHTML5.co</a> Demo Images: <a href="http://unsplash.com/" target="_blank">Unsplash</a></small>
					</p>
					<p class="pull-right">
						<ul class="gtco-social-icons pull-right">
							<li><a href="#"><i class="icon-twitter"></i></a></li>
							<li><a href="#"><i class="icon-facebook"></i></a></li>
							<li><a href="#"><i class="icon-linkedin"></i></a></li>
							<li><a href="#"><i class="icon-dribbble"></i></a></li>
						</ul>
					</p>
				</div>
			</div>

		</div>
	</footer>
	<!-- </div> -->

	</div>
	<div class="gototop js-top">
		<a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
	</div>	
	<!-- jQuery --><script src="js/jquery.min.js"></script>
	<!-- jQuery Easing --><script src="js/jquery.easing.1.3.js"></script>
	<!-- Bootstrap --><script src="js/bootstrap.min.js"></script>
	<!-- Waypoints --><script src="js/jquery.waypoints.min.js"></script>
	<!-- Carousel --><script src="js/owl.carousel.min.js"></script>
	<!-- countTo --><script src="js/jquery.countTo.js"></script>
	<!-- Stellar Parallax --><script src="js/jquery.stellar.min.js"></script>
	
	<!-- Magnific Popup -->
	<script src="js/jquery.magnific-popup.min.js"></script>
	<script src="js/magnific-popup-options.js"></script>
	
	<!-- Main -->
	<script src="js/main.js"></script>
   
</body>
</html>