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
</head>

<body>
<% if(!"all".equals(request.getParameter("hideHeader"))) {%>
	<!-- Begin Wrapper -->
	<div id="wrapper">  
	
	  <h1><a href="#" title="">Garment Mantra</a></h1>
	  
	  <% if(request.getParameter("hideHeader") == null) { %>
	  <div id="header">		  
	  </div>
	  <% } %>
	  
	  <!-- Begin Navigation -->
	  <div id="navigation">
	    <ul>
	      <li><a href="/gm">Home</a></li>
	      <li><a href="/gm/web/suppliers.jsp">Suppliers</a></li>
	      <li><a href="/gm/web/collectionDisplay.jsp">Collection Report</a></li>
	      <li><a href="/gm/web/invoiceReport.jsp">Invoice Report</a></li>
	    </ul>
	  </div>
	  <!-- End Navigation -->
<% } %>