<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

	<!DOCTYPE HTML>

	<html>
		<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Traveler &mdash; Free Website Template, Free HTML5 Template by FreeHTML5.co</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="Free HTML5 Website Template by FreeHTML5.co" />
		<meta name="keywords" content="free website templates, free html5, free template, free bootstrap, free website template, html5, css3, mobile first, responsive" />
		<meta name="author" content="FreeHTML5.co" />
	
		  <!-- Facebook and Twitter integration -->
		<meta property="og:title" content=""/>
		<meta property="og:image" content=""/>
		<meta property="og:url" content=""/>
		<meta property="og:site_name" content=""/>
		<meta property="og:description" content=""/>
		<meta name="twitter:title" content="" />
		<meta name="twitter:image" content="" />
		<meta name="twitter:url" content="" />
		<meta name="twitter:card" content="" />
	
		<link href="https://fonts.googleapis.com/css?family=Lato:300,400,700" rel="stylesheet">
		
		<!-- Animate.css -->
		<link rel="stylesheet" href="css/animate.css">
		<!-- Icomoon Icon Fonts-->
		<link rel="stylesheet" href="css/icomoon.css">
		<!-- Themify Icons-->
		<link rel="stylesheet" href="css/themify-icons.css">
		<!-- Bootstrap  -->
		<link rel="stylesheet" href="css/bootstrap.css">
	
		<!-- Magnific Popup -->
		<link rel="stylesheet" href="css/magnific-popup.css">
	
		<!-- Magnific Popup -->
		<link rel="stylesheet" href="css/bootstrap-datepicker.min.css">
	
		<!-- Owl Carousel  -->
		<link rel="stylesheet" href="css/owl.carousel.min.css">
		<link rel="stylesheet" href="css/owl.theme.default.min.css">
	
		<!-- Theme style  -->
		<link rel="stylesheet" href="css/style.css">
	
		<!-- Modernizr JS -->
		<script src="js/modernizr-2.6.2.min.js"></script>
		<!-- FOR IE9 below -->
		<!--[if lt IE 9]>
		<script src="js/respond.min.js"></script>
		<![endif]-->
	
		<script language = "javascript">
			function check(){
				var blank = 0;
				if(form1.USER_ID.value == "")
					blank=1;
				if(form1.USER_PWD.value=="")
					blank=1;
				if(blank==1){
					alert("공란이 있습니다. 채워주세요");
					return (false);
				}
				else{
					return (true);
				}
			}
		</script>
		</head>
		<body>
			
		<div class="gtco-loader"></div>
		
		<div id="page">
	
		
		<!-- <div class="page-inner"> -->
		<nav class="gtco-nav" role="navigation">
			<div class="gtco-container">
				
				<div class="row">
					<div class="col-sm-4 col-xs-12">
						<div id="gtco-logo"><a href="login.jsp"> 역사의 발자취 관리자모드 <em>.</em></a></div>
					</div>
				</div>
				
			</div>
		</nav>
		
		<header id="gtco-header" class="gtco-cover gtco-cover-md" role="banner" style="background-image: url(images/img_bg_2.jpg)">
			<div class="overlay"></div>
			<div class="gtco-container">
				<div class="row">
					<div class="col-md-12 col-md-offset-0 text-left">	
	
						<div class="row row-mt-15em">
							<div class="col-md-4 col-md-push-1 animate-box" data-animate-effect="fadeInRight">
								<div class="form-wrap">
									<div class="tab">
										
										<div class="tab-content">
											<div class="tab-content-inner active" data-content="signup">
												<h3>Login</h3>
												<form action=select_login.jsp method=POST name=form1 onSubmit="return check();">
													<div class="row form-group">
														<div class="col-md-12">
															<label for="fullname">Email</label>
															<input type="text" name="USER_ID" class="form-control">
														</div>
													</div>	
													<div class="row form-group">
														<div class="col-md-12">
															<label for="fullname">Password</label>
															<input type="password" name="USER_PWD" class="form-control">
														</div>
													</div>
													<div class="row form-group">
														<div class="col-md-12">
															<input type="submit" class="btn btn-primary btn-block" value="Submit">
														</div>
													</div>
												</form>	
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>					
					</div>
				</div>
			</div>
		</header>
	
		<!-- </div> -->
	
		</div>
	
		<div class="gototop js-top">
			<a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
		</div>
		
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- jQuery Easing -->
		<script src="js/jquery.easing.1.3.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Waypoints -->
		<script src="js/jquery.waypoints.min.js"></script>
		<!-- Carousel -->
		<script src="js/owl.carousel.min.js"></script>
		<!-- countTo -->
		<script src="js/jquery.countTo.js"></script>
	
		<!-- Stellar Parallax -->
		<script src="js/jquery.stellar.min.js"></script>
	
		<!-- Magnific Popup -->
		<script src="js/jquery.magnific-popup.min.js"></script>
		<script src="js/magnific-popup-options.js"></script>
		
		<!-- Datepicker -->
		<script src="js/bootstrap-datepicker.min.js"></script>
		
	
		<!-- Main -->
		<script src="js/main.js"></script>
	
		</body>
	</html>
	
	

