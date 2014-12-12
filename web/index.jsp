<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.svnitsai.gm.database.provider.LandingPageWindows"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<%@ page import="com.svnitsai.gm.util.date.DateUtil"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
LandingPageWindows landingPageWindow = new LandingPageWindows();

/* Get BalancePayable Window*/
List resultBalancePayable = (List) landingPageWindow.getBalancePayableWindow();
String balancePaymentDue = "";
if ((resultBalancePayable != null) && (resultBalancePayable.size() > 0)) {
	Map row = (Map) resultBalancePayable.get(0);
	balancePaymentDue = "Balance Payables (on " + DisplayUtil.getDisplayDate(row.get("BalanceDueDate").toString(), new SimpleDateFormat("yyyy/MM/dd")) + " )";
}
else balancePaymentDue = "Balance Payables - None to display";

/* Get CreditSale Window*/
List resultCreditSales = (List) landingPageWindow.getCreditSalesWindow();
String creditSalesPaymentDue = "";
if ((resultCreditSales != null) && (resultCreditSales.size() > 0)) {
	Map row = (Map) resultCreditSales.get(0);
	creditSalesPaymentDue = "Credit Sales Due Today (" + DisplayUtil.getDisplayDate(DateUtil.getCurrentDate(new SimpleDateFormat("yyyy/MM/dd")), new SimpleDateFormat("yyyy/MM/dd")) + " )";
}
else creditSalesPaymentDue = "Credit Sales Due Today - None to display";
%>

<%@include file="web/header.jsp" %>
<script>
	function loadPage(page)
	{
		window.location = "/gm/web/" + page;
	}

	$(document).ready(function() {
	    $('#scrollBalanceDueTable').dataTable( {
	        "scrollY":        "125px",
	        "scrollCollapse": true,
	        "paging":         false
	    } );

	    $('#scrollCreditSalesTable').dataTable( {
	        "scrollY":        "200px",
	        "scrollCollapse": true,
	        "paging":         false
	    } );
	} );
	
</script>
  <!-- Begin Column One-->
  <div id="one" style="width: 460px">
    <div class="item" style="height: 350px"> 
	<h2><%=balancePaymentDue%></h2>
	<table id="scrollBalanceDueTable" class="display" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th style="width:120px">Supplier Name</th>
			<th style="width:75px">Payable Amount</th>
			<th style="width:90px">Balance Due</th>
		</tr>
	</thead>
	<tbody style="height:125px">
	<%if ((resultBalancePayable != null) && (resultBalancePayable.size() > 0)) { %>
		<%for (Object object : resultBalancePayable) {
			Map row = (Map) object; 	
			String dispPayableAmount = " " + DisplayUtil.getDisplayAmount(row.get("PayableAmount").toString());
			String dispBalanceDue = " " + DisplayUtil.getDisplayAmount(row.get("BalanceDue").toString());
			%>
		<tr>
			<td style="width:160px" align="left"><%=row.get("SupplierName")%> </td>
			<td style="width:110px" align="right">&#8377;<%=dispPayableAmount%> </td>
			<td style="width:110px" align="right">&#8377;<%=dispBalanceDue%> </td>
		</tr>	
		<% }
	} else { /* If no BalanceDue Record Found */
 		%>
 		<% }
 		%>
 	</tbody>
	</table>
	<br>
	<button class="addButton" onclick="loadPage('suppliers.jsp');">&nbsp;&nbsp;Add Payables</button>
    </div>
  </div>
  <div id="two" style="width: 450px;">
    <div class="item" style="height: 350px">
	<h2><%=creditSalesPaymentDue%></h2>
	<table id="scrollCreditSalesTable" class="display" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th>Merchant Name</th>
			<th>Due Date</th>
			<th>Pending Amount</th>
		</tr>
	</thead>
	<tbody>
	<%if ((resultCreditSales != null) && (resultCreditSales.size() > 0)) { %>
		<%for (Object object : resultCreditSales) {
			Map row = (Map) object; 	
			String dispReceivableBalanceDue = " " + DisplayUtil.getDisplayAmount(row.get("BalanceDue").toString());
			String dispAdjustedDueDate = DisplayUtil.getDisplayDate(row.get("AdjustedDueDate").toString(), new SimpleDateFormat("yyyy/MM/dd"));
			%>
		<tr>
			<td style="width:160px" align="left"><%=row.get("CustomerName")%> </td>
			<td style="width:160px" align="left"><%=dispAdjustedDueDate%></td>
			<td style="width:110px" align="right">&#8377;<%=dispReceivableBalanceDue%></td>
		</tr>
		<% }
	} else { /* If no CreditSales Due Record Found */
 		%>
 		<% }
 		%>
	<!--  		
		<tr>
			<td style="width:160px" align="left">AtoZ Garments, Erode</td>
			<td style="width:160px" align="left">Nov 24, 2014</td>
			<td style="width:110px" align="right">&#8377; 9,99,99,999</td>
		</tr>
		<tr>
			<td style="width:160px" align="left">Chandra Textiles, Chennai</td>
			<td style="width:160px" align="left">Nov 25, 2014</td>
			<td style="width:110px" align="right">&#8377; 19,99,999</td>
		</tr>
		<tr>
			<td style="width:160px" align="left">Gomathi Textiles, Tiruppur</td>
			<td style="width:160px" align="left">Nov 26, 2014</td>
			<td style="width:110px" align="right">&#8377; 29,99,999</td>
		</tr>
		<tr>
			<td style="width:160px" align="left">AtoZ Garments, Bangalore</td>
			<td style="width:160px" align="left">Nov 24, 2014</td>
			<td style="width:110px" align="right">&#8377; 9,99,999</td>
		</tr>
		<tr>
			<td style="width:160px" align="left">Chandra Textiles, Delhi</td>
			<td style="width:160px" align="left">Nov 25, 2014</td>
			<td style="width:110px" align="right">&#8377; 19,99,999</td>
		</tr>
		-->
	 </tbody>
	</table>
    </div>
  </div>
 <%@include file="web/footer.jsp" %>