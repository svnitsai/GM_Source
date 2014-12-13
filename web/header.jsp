<%@ page import="com.svnitsai.gm.database.generated.UserAccess" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css" href="/gm/web/css/style.css" />
  <link rel="stylesheet" type="text/css" href="/gm/web/css/jquery-ui.css" />
  <link rel="stylesheet" type="text/css" href="/gm/web/css/jquery.datatables.css" />
  
  <script src="/gm/web/javascript/jquery-1.11.1.min.js"></script>
  <script src="/gm/web/javascript/jquery-ui.min.js"></script>
  <script src="/gm/web/javascript/chosen.jquery.min.js"></script>
  <script src="/gm/web/javascript/jquery.dataTables.min.js"></script>
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
				primary: "ui-icon-plus"
			}
		});
		$( ".saveButton" ).button({
			icons: {
				primary: "ui-icon-disk"
			}
		});
		$( ".printButton" ).button({
			icons: {
				primary: "ui-icon-print"
			}
		});
		$( ".refreshButton" ).button({
			icons: {
				primary: "ui-icon-refresh"
			}
		});
		$( ".anyButton" ).button();
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
String userCamelCase = DisplayUtil.getDisplayCamelCase(userName);
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
	   <li> Welcome <%=userCamelCase%>! </li>
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
	      <li><a href="/gm/web/suppliers.jsp">Payables</a></li>
	      <li><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
	      <li><a href="/gm/web/reports.jsp">Reports</a></li>
	      <% } else  { %>
	      <li><a href="/gm">Home</a></li>
	      <li><a href="/gm/web/collectionDisplay.jsp">Collection Report</a></li>
	      <li><a href="/gm/web/invoiceReport.jsp">Invoice Report</a></li>
	      <% } %>
	    </ul>
	  </div>
	  <!-- End Navigation -->
<% } %>