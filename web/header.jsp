<%@ page import="com.svnitsai.gm.database.generated.UserAccess"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/style.css" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/jquery.datatables.css" />
<link rel="stylesheet" type="text/css" href="/gm/web/css/select2.css" />

<script src="/gm/web/javascript/jquery-1.11.1.min.js"></script>
<script src="/gm/web/javascript/jquery-ui.min.js"></script>
<script src="/gm/web/javascript/jquery.validate.min.js"></script>
<script src="/gm/web/javascript/additional-methods.min.js"></script>
<script src="/gm/web/javascript/chosen.jquery.min.js"></script>
<!-- Table Scroll formatting -->
<script src="/gm/web/javascript/jquery.dataTables.min.js"></script>
<!-- Select drop down formatting -->
<script src="/gm/web/javascript/select2.min.js"></script>
<script src="/gm/web/javascript/utils.js"></script>
<script src="/gm/web/javascript/jquery.validate.fixes.js"></script>

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
		
		$( ".saveButton" ).button({
			icons: {
				primary: "ui-icon-disk"
			}
		});
		$( ".cancelButton" ).button({
			icons: {
				primary: "ui-icon-cancel"
			}
		});
		$( ".addButton" ).button({
			icons: {
				primary: "ui-icon-plus"
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
		$( ".infoButton" ).button({
			icons: {
				primary: "ui-icon-info"
			}
		});
		$( ".cancelButton" ).button({
			icons: {
				primary: "ui-icon-cancel"
			}
		});
		$( ".editButtonNolabel" ).button({
			icons: {
				primary: "ui-icon-pencil"
			}
		});
		$( ".clockButton" ).button({
			icons: {
				primary: "ui-icon-clock"
			}
		});
		$( ".anyButton" ).button();
	});
	
	//Hide breadCrumps by default
	$(document).ready(function() {
		$('#breadCrumps').hide();
	});
	
	// SMS submenu handling - begins
 	$(document).on('mouseover',".navMenu",function () {
 	        if ($(this).hasClass("dropdown")) {
 	 	        $('#smsSubMenuDiv').show();
 				$(".smsSubMenuItem").each(
					function() {
 							$(this).removeClass('hover');
 						}
 					);
 	        } else {
 	 	        $('#smsSubMenuDiv').delay(900).hide();
 	        }
 	        $(this).addClass('hover');
 	 }); //mouseover main horizontal navigation menu
	
 	 $(document).on('mouseout',".navMenu",function () {
	        $('#smsSubMenuDiv').delay(900).hide();
	        $(this).removeClass('hover');
	 }); //mouseout of horizontal navigation menu 
 	 
 	 $(document).on('mouseover',".smsSubMenuItem",function () {
	        $('#smsSubMenuDiv').show();
	        $('.dropdown').addClass('hover');
			$(".smsSubMenuItem").each(
					function() {
						$(this).removeClass('hover');
					}
				);
	        $(this).addClass('hover');
	 });//mouseover vertical SMS submenu items
 	
	 $(document).on('mouseout',"#smsSubMenu",function () {
	        $('#smsSubMenuDiv').delay(900).hide();
	        $('.dropdown').removeClass('hover');
	 }); //mouseout of ALL SMS submenu item
	// SMS submenu handling - ends
	 
	function isPageValid()
	{
		$.validator.messages.required = 'Please specify value';
		var form = $( "#inputForm" );
	   	form.validate();
     		if(form.valid() == false)
      		{
       			return false;
      		}

		return true;
	}
</script>

<style>
label.error {
	color: red;
}

input.error {
	border: 1px solid red;
}
</style>

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

//Get active page
String activePage = "Home";
if (request.getRequestURL().indexOf("supplier") > 0) activePage = "Supplier";
else if (request.getRequestURL().indexOf("collection") > 0) activePage = "Collections";
else if (request.getRequestURL().indexOf("report") > 0) activePage = "Reports";
else if (request.getRequestURL().indexOf("customer") > 0) activePage = "Customer";
else if (request.getRequestURL().indexOf("sms") > 0) activePage = "SMS";
else if (request.getRequestURL().indexOf("help") > 0) activePage = "Help";

%>

</head>

<body>

	<% if(!"all".equals(request.getParameter("hideHeader"))) {%>
	<!-- Begin Wrapper -->
	<div id="wrapper">
		<div id="company-title">Garment Mantra</div>
		<div id="login-menu">
			<ul style="list-style: none;">
				<li>Welcome <%=userCamelCase%>!
				</li>
				<li class="opposed"><a href="/gm/web/logout.jsp">Logout</a></li>
			</ul>
		</div>
		<% if(request.getParameter("hideHeader") == null) { %>
		<div id="header"></div>
		<% } %>

		<!-- Begin Navigation -->
		<div id="navigation">
			<ul class="menu">
				<% // Set Admin specific menu options
	      if ((userRole != null) && (userRole.equalsIgnoreCase("ADMIN"))) { %>
				<% if (activePage.equalsIgnoreCase("Home")) { %>
				<li class="active"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="navMenu dropdown"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Supplier")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="active"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="navMenu dropdown"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Collections")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="active"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="navMenu dropdown"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Customer")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="active"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="dropdown navMenu"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Reports")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="dropdown navMenu"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="active"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("SMS")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="active navMenu dropdown"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Help")) {%>
				<li><a href="/gm">Home</a></li>
				<li><a href="/gm/web/suppliers.jsp">Payables</a></li>
				<li><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li><a href="/gm/web/customer.jsp">Customer</a></li>
				<li class="dropdown"><a>&nbsp;&nbsp;&nbsp;SMS&nbsp;&nbsp;&nbsp;</a></li>
				<li><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="active"><a href="/gm/web/help.jsp">Help</a></li>
				<% } %>
				<% } else  { %>
				<%  if (activePage.equalsIgnoreCase("Home")) {  %>
				<li class="active"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/invoiceReport.jsp">Reports</a></li>
				<li><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Collections")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="active"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Reports")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="active"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="navMenu"><a href="/gm/web/Files/GM_Help.pdf"
					onClick="window.open('/gm/web/Files/GM_Help.pdf', 'newwindow', 'width=700, height=500').focus(); return false;")>Help</a></li>
				<% } else if (activePage.equalsIgnoreCase("Help")) {%>
				<li class="navMenu"><a href="/gm">Home</a></li>
				<li class="navMenu"><a href="/gm/web/collectionDisplay.jsp">Collections</a></li>
				<li class="navMenu"><a href="/gm/web/reports.jsp">Reports</a></li>
				<li class="active"><a href="/gm/web/help.jsp">Help</a></li>
				<% } %>
				<% } %>
			</ul>
		</div>
		<!-- SMS sub menu - begins -->
		<div id="smsSubMenuDiv" class="smsSubMenuDiv hide-label"
			style="position: absolute; z-index: 99;">
			<ul id="smsSubMenu" class="dropdown-menu vertical-nav">
				<li class="smsSubMenuItem"><a href="/gm/web/smsSend.jsp">Send</a>
				</li>
				<li class="smsSubMenuItem"><a href="/gm/web/smsHistory.jsp">History</a>
				</li>
				<li class="smsSubMenuItem"><a href="/gm/web/smsTemplates.jsp">Templates</a>
				</li>
			</ul>
		</div>
		<!-- SMS sub menu - ends -->
		<div id="breadCrumps" class="hide-label"
			style="position: absolute; z-index: 90; margin-top: 34px; background-color: rgb(115, 143, 17); opacity: 0.8; height: 20px; color: white; font-weight: bold; padding-left: 30px; width: 920px;">
			SMS >></div>
		<!-- End Navigation -->
		<% } %>