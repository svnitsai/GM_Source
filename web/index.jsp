<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.svnitsai.gm.database.provider.LandingPageWindows"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
LandingPageWindows landingPageWindow = new LandingPageWindows();
List result = (List) landingPageWindow.getBalancePayableWindow();
String balancePaymentDue = "";
if ((result != null) && (result.size() > 0)) {
	Map row = (Map) result.get(0);
	balancePaymentDue = "Balance Payables (on " + DisplayUtil.getDisplayDate(row.get("BalanceDueDate").toString(), new SimpleDateFormat("yyyy/MM/dd")) + " )";
}
else balancePaymentDue = "Balance Payables - None to display";
%>

<%@include file="web/header.jsp" %>
<script>
	function loadPage(page)
	{
		window.location = "/gm/web/" + page;
	}
	$(document).ready(function() {
	    $('#scrollTable').dataTable( {
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
	<table id="scrollTable" class="display" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th style="width:120px">Supplier Name</th>
			<th style="width:75px">Payable Amount</th>
			<th style="width:90px">Balance Due</th>
		</tr>
	</thead>
	<tbody style="height:125px">
	<%if ((result != null) && (result.size() > 0)) { %>
		<%for (Object object : result) {
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
	<h2>Invoices Due Today</h2>
	<table id="borderTable">
		<tr>
			<th>Merchant Name</th>
			<th>Due Date</th>
			<th>Deferred Date</th>
			<th>Amount Due</th>
		</tr>
		<tr>
			<td>AtoZ Garments</td>
			<td>Nov 24, 2014</td>
			<td>Dec 6, 2016</td>
			<td>1,00,000</td>
		</tr>
		<tr>
			<td>Chandra Textiles</td>
			<td>Dec 6, 2014</td>
			<td>&nbsp;</td>
			<td>50,000</td>
		</tr>
		<tr>
			<td>ExportDesi Inc</td>
			<td>Dec 6, 2014</td>
			<td>&nbsp;</td>
			<td>45,000</td>
		</tr>
	</table>
    </div>
  </div>
 <%@include file="web/footer.jsp" %>