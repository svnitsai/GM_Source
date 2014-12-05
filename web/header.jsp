<%@ page import="com.svnitsai.gm.database.generated.Useraccess" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="/gm/web/css/style.css" />
  <link rel="stylesheet" type="text/css" href="/gm/web/css/jquery-ui.css" />
  
  <script src="/gm/web/javascript/jquery-1.11.1.min.js"></script>
  <script src="/gm/web/javascript/jquery-ui.min.js"></script>
  <script src="/gm/web/javascript/chosen.jquery.min.js"></script>
  <script src="/gm/web/javascript/utils.js"></script>
    
  <title>Garment Mantra</title>

   <script>
	$(function() 
	{
		 $( ".datepicker" ).datepicker({
			dateFormat: "dd/mm/yy",
			showAnim: "slideDown"
		});

		$( ".editButton" ).button({
			icons: {
				primary: "ui-icon-pencil"
			},
			label: "&nbsp;&nbsp;Edit"
		});

		$( ".deleteButton" ).button({
			icons: {
				primary: "ui-icon-closethick"
			}
		});
		
		$( ".addButton" ).button({
			icons: {
				primary: "ui-icon-plusthick"
			}
		});
	});
</script>
<% 
//Force user to login if 'not already logged in'
String userName = (String) session.getAttribute("User");
if (null == userName) {
   request.setAttribute("Error", "Session has ended.  Please login.");
   RequestDispatcher rd = request.getRequestDispatcher("/web/login.jsp");
   rd.forward(request, response);
}
String userRole = (String) session.getAttribute("Role");

//If requested URL is restricted by role (eg. supplier by ADMIN only), redirect to login
//TODO: Add all pages restricted by ROLE
if ((request.getRequestURL().indexOf("supplier") > 0) && (userRole.compareToIgnoreCase("USER") == 0)) {
	   RequestDispatcher rd = request.getRequestDispatcher("/web/login.jsp");
	   rd.forward(request, response);
}
%>
</head>

<body>

<% if(!"all".equals(request.getParameter("hideHeader"))) {%>
	<!-- Begin Wrapper -->
	<div id="wrapper">
		<div id="company-title"> Garment Mantra </div>
	<div id="login-menu">	   
	 <ul style="list-style: none;">
	   <li> </li>
	   <li> Welcome <%=userName %>! </li>
	   <li class="opposed"> <a href="/gm/web/logout.jsp">Logout</a> </li>
	 </ul>
</div>	  
	  <% if(request.getParameter("hideHeader") == null) { %>
	  <div id="header">		  
	  </div>
	  <% } %>
	  
	  <!-- Begin Navigation -->
	  <div id="navigation">
	    <ul>
	     <% if ((userRole != null) && (userRole.equalsIgnoreCase("ADMIN"))) { %>
	      <li><a href="/gm">Home</a></li>
	      <li><a href="/gm/web/suppliers.jsp">Suppliers</a></li>
	      <li><a href="/gm/web/collectionDisplay.jsp">Collection Report</a></li>
	      <li><a href="/gm/web/invoiceReport.jsp">Invoice Report</a></li>
	      <% } else  { %>
	      <li><a href="/gm">Home</a></li>
	      <li><a href="/gm/web/collectionDisplay.jsp">Collection Report</a></li>
	      <li><a href="/gm/web/invoiceReport.jsp">Invoice Report</a></li>
	      <% } %>
	    </ul>
	  </div>
	  <!-- End Navigation -->
<% } %>