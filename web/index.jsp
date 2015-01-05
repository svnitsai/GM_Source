<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!-- 
 * index.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Landing page followed by successful user login;
 * 
-->

<%@ page import="com.svnitsai.gm.database.provider.LandingPageWindows"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<%@ page import="com.svnitsai.gm.util.date.DateUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
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

/* Get DataRefresh Window*/
List resultDataRefresh = (List) landingPageWindow.getDataRefreshWindow();
String partyDataRefreshMesgDue = "Customer Data is not loaded";
String creditSalesDataRefreshMesgDue = "Credit Sales Data is not loaded";
boolean partyDataRefresh = true, creditSalesDataRefresh = true;
if ((resultDataRefresh != null) && (resultDataRefresh.size() > 0)) {
	for (Object object : resultDataRefresh) {
		Map row = (Map) object;
		if (row.get("ExtractDataType").toString().equals("P")) {
			partyDataRefreshMesgDue = "Customer Data last refreshed on " + DisplayUtil.getDisplayDate(row.get("ExtractedDate").toString(), new SimpleDateFormat("yyyy/MM/dd"));
			if (row.get("Extracted").toString().equals("I")) partyDataRefresh = false;
		} else {
			creditSalesDataRefreshMesgDue = "Credit Sales Data last refreshed on " + DisplayUtil.getDisplayDate(row.get("ExtractedDate").toString(), new SimpleDateFormat("yyyy/MM/dd"));
			if (row.get("Extracted").toString().equals("I")) creditSalesDataRefresh = false;
		}
	}
}
/* If display strings are empty, populate appropriate message */
%>

<%@include file="web/header.jsp"%>
<script>
	function loadPage(page) {
		window.location = "/gm/web/" + page;
	}

	$(document).ready(function() {
		$('#scrollBalanceDueTable').dataTable({
			"scrollY" : "125px",
			"scrollCollapse" : true,
			"paging" : false
		});

		$('#scrollCreditSalesTable').dataTable({
			"scrollY" : "200px",
			"scrollCollapse" : true,
			"paging" : false,
			"aoColumns": [
                          {"bSortable": true},
                          {"iDataSort": 2},
                          {"bVisible": false},
                          {"bSortable": true}
			              ]
		});
	});

</script>
<!-- Begin Column One-->
<!-- Balance Payable Window - Begin -->
<div id="one" style="width: 460px">
	<div class="item" style="height: 360px">
		<h2><%=balancePaymentDue%></h2>
		<table id="scrollBalanceDueTable" class="display" cellspacing="0"
			width="100%">
			<thead>
				<tr>
					<th style="width: 120px">Supplier Name</th>
					<th style="width: 75px">Payable Amount</th>
					<th style="width: 90px">Balance Due</th>
				</tr>
			</thead>
			<tbody style="height: 125px">
				<%
					if ((resultBalancePayable != null)
							&& (resultBalancePayable.size() > 0)) {
				%>
				<%
					for (Object object : resultBalancePayable) {
							Map row = (Map) object;
							String dispPayableAmount = " "
									+ DisplayUtil.getDisplayAmount(row.get(
											"PayableAmount").toString());
							String dispBalanceDue = " "
									+ DisplayUtil.getDisplayAmount(row
											.get("BalanceDue").toString());
				%>
				<tr>
					<td style="width: 160px" align="left"><%=row.get("SupplierName")%>
					</td>
					<!--
					<td style="width: 110px" align="right">&#8377;<%=dispPayableAmount%>
					</td> 
					<td style="width: 110px" align="right">&#8377;<%=dispBalanceDue%>
					</td> -->
					<td style="width: 110px" align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span><%=dispPayableAmount%>
					</td> 
					<td style="width: 110px" align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span><%=dispBalanceDue%>
				</tr>
				<%
					}
					} else { /* If no BalanceDue Record Found */
				%>
				<%
					}
				%>
			</tbody>
		</table>
		<br>
		<!-- <button class="addButton" onclick="loadPage('suppliers.jsp');">&nbsp;&nbsp;Add Payables</button> -->

		<!-- Customer aka Party information refresh -->
		<div>
			<!-- TODO: Cater action button -->
			<form action="/gm/web/DataRefreshServlet" method="post">
				<table style="border-collapse: collapse;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width: 375px;"><%=partyDataRefreshMesgDue %></td>
						<% if (partyDataRefresh) { %>
						<td><button type="submit" class="refreshButton" name="action"
								value="customerDataRefresh">&nbsp;&nbsp;</button></td>
						<% } else { %>
						<td title="Data being loaded"><button type="button"
								class="refreshButton" onClick="dummy();" disabled>&nbsp;&nbsp;</button></td>
						<% } %>
					</tr>

					<tr>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<!-- Receivable information refresh -->
					<tr>
						<td><%=creditSalesDataRefreshMesgDue %></td>
						<% if (creditSalesDataRefresh) { %>
						<td><button type="submit" class="refreshButton" name="action"
								value="creditSalesDataRefresh">&nbsp;&nbsp;</button></td>
						<% } else { %>
						<td title="Data being loaded"><button type="button"
								class="refreshButton" onClick="dummy();" disabled>&nbsp;&nbsp;</button></td>
						<% } %>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>
<!-- Balance Payable Window - End -->

<!-- Credit Sales Window - Begin -->
<div id="two" style="width: 450px;">
	<div class="item" style="height: 360px">
		<h2><%=creditSalesPaymentDue%></h2>
		<table id="scrollCreditSalesTable" class="display" cellspacing="0"
			width="100%">
			<thead>
				<tr>
					<th>Merchant Name</th>
					<th>Due Date</th>
					<th>Sortable Due Date</th>
					<th>Pending Amount</th>
				</tr>
			</thead>
			<tbody>
				<%
					if ((resultCreditSales != null) && (resultCreditSales.size() > 0)) {
				%>
				<%
					for (Object object : resultCreditSales) {
							Map row = (Map) object;
							String dispReceivableBalanceDue = " "
									+ DisplayUtil.getDisplayAmount(row
											.get("BalanceDue").toString());
							String dispAdjustedDueDate = DisplayUtil.getDisplayDate(row
									.get("AdjustedDueDate").toString(),
									new SimpleDateFormat("yyyy/MM/dd"));
							String sortAdjustedDueDate = row.get("AdjustedDueDate").toString(); //Use yyyy/mm/dd date for sorting
				%>
				<tr>
					<td style="width: 160px" align="left"><%=row.get("CustomerName")%>
					</td>
					<td style="width: 160px" align="left"><%=dispAdjustedDueDate%></td>
					<td style="width: 160px" align="left"><%=sortAdjustedDueDate%></td>
					<td style="width: 110px" align="right"><span style="font-family: DejaVu Sans;">&#x20b9;</span><%=dispReceivableBalanceDue%></td>
				</tr>
				<%
					}
					} else { /* If no CreditSales Due Record Found */
				%>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
</div>
<!-- Credit Sales Window - End -->
<%@include file="web/footer.jsp"%>