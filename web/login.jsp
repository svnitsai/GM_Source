<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.svnitsai.gm.database.generated.Useraccess"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!-- 
 * login.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Initial user login screen;
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
</head>
<!-- head ends -->

<!-- body begins -->
<body>
	<!-- page-container begins -->
	<div class="page-container">
		<div id="masthead">
			<br class="clear"></br>
		</div>
		<div id="navigation">
			<div id="control-area">GARMENT MANTRA</div>
		</div>
		<div class="clear"></div>
		<div class="login-contentarea">
			<div id="column-container">
				<div class="login-screenlet" id="login-box">
					<div class="login-screenlet-title-bar">
						<h3>Registered User</h3>
					</div>
					<div class="login-item">
						<%
							Useraccess currentUser = (Useraccess) (request
									.getAttribute("currentUser"));
							String errorMessageDisplay = (String) request
									.getAttribute("errorMessage");
							String userName = (String) request.getAttribute("userName");
							String userPassword = (String) request.getAttribute("passWord");
						%>
						<form action="LoginServlet" method="post">
							<table class="login-table">
								<tr>
									<td nowrap align="right">User Name:</td>
									<%
										if ((userName != null) && (userName.length() > 0)) {
									%>
									<td><input type="text" name="jspUserName" size="25px"
										style="padding: 3px;" value="<%=userName%>"> </input></td>
									</td>
									<%
										} else {
									%>
									<td><input type="text" name="jspUserName" size="25px"
										style="padding: 3px;"></td>
									</td>
									<%
										}
									%>

									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<td nowrap>Password:</td>
									<td><input type="password" name="jspPassword" size="25px"
										style="padding: 3px;"></td>
									<td width="100%">&nbsp;</td>
								</tr>
								<tr>
									<%
										if ((errorMessageDisplay != null)
												&& (errorMessageDisplay.length() > 0)) {
									%>
									<td nowrap colspan="3" class="login-message"><%=errorMessageDisplay%></td>
									<%
										} else {
									%>
									<td nowrap colspan="3"></td>
									<%
										}
									%>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td align="left"><input type="submit" value="Login"></td>
									<td>&nbsp;</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- page-container begins -->

		<div id="login-footer">
			<ul style="list-style: none;">
				<li>&copy; 2014 Garment Mantra</li>
				<li></li>
				<li class="opposed">Designed by SVN Systems and Innovations</li>
			</ul>
		</div>
	</div>

</body>
<!-- body ends -->