<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.svnitsai.gm.database.generated.UserAccess"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!-- 
 * logout.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Logout screen which clears sessions;
 * 
-->

<html xmlns="http://www.w3.org/1999/xhtml">
<!-- head begins -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/style.css" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/jquery-ui.css" />

<script src="/gm/web/javascript/jquery-1.11.1.min.js"></script>
<script src="/gm/web/javascript/jquery-ui.min.js"></script>
<script src="/gm/web/javascript/chosen.jquery.min.js"></script>
<script src="/gm/web/javascript/utils.js"></script>
<title>Garment Mantra</title>
<% 
String userName = (String) session.getAttribute("User");
if (null != userName) {
	//Clear session
	session.removeAttribute("User");
	session.removeAttribute("Role");
	session.invalidate();
}
RequestDispatcher rd = request.getRequestDispatcher("/web/login.jsp");
rd.forward(request, response);
%>

</head>
<!-- head ends -->

<!-- body begins -->
<body>
	<!-- page-container begins -->
	<div class="page-container"></div>
	<!-- page-container begins -->

	<div id="login-footer">
		<ul style="list-style: none;">
			<li>&copy; 2014 Garment Mantra</li>
			<li></li>
			<li class="opposed">Designed by SVN Systems and Innovations</li>
		</ul>
	</div>
</body>
<!-- body ends -->