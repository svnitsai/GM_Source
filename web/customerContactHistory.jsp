<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.svnitsai.gm.Util"%>
<%@ page import="com.svnitsai.gm.database.generated.*"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="com.svnitsai.gm.util.date.DateUtil"%>
<%@ page import="com.svnitsai.gm.database.provider.CustomerBankInfoCRUDProvider"%>
<%@ page import="com.svnitsai.gm.database.provider.SMSContactHistoryCRUDProvider" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.text.SimpleDateFormat"%>

<!-- 
 * customerContactHistory.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen to show SMS history for a customer
 *              Invoked from SMS history screen
 * 
-->
<jsp:include page="header.jsp?hideHeader=all" />

<%
	//Get CustId from SMS History - first time around; in Save it session for future use
	String custIdFromSMSHistoryScreen = (String) request.getParameter("custId");
	if (custIdFromSMSHistoryScreen == null) {
		custIdFromSMSHistoryScreen =  (String) session.getAttribute("custIdFromSMSHistoryScreen");
	} else {
		session.setAttribute("custIdFromSMSHistoryScreen", custIdFromSMSHistoryScreen);
	}
	
	
	String custNameFromSMSHistoryScreen = (String) request.getParameter("custName");
	if (custNameFromSMSHistoryScreen == null) {
		custNameFromSMSHistoryScreen =  (String) session.getAttribute("custNameFromSMSHistoryScreen");
	} else {
		session.setAttribute("custNameFromSMSHistoryScreen", custNameFromSMSHistoryScreen);
	}
//	System.out.println (" custNameFromSMSHistoryScreen " + custNameFromSMSHistoryScreen);
		
			
     SMSContactHistoryCRUDProvider contactHistoryCRUDProvider = new SMSContactHistoryCRUDProvider();
     List result = (List) contactHistoryCRUDProvider.getSMSContactHistoryByCustId(custIdFromSMSHistoryScreen);
			
%>
<style>
label.error {
	color: red;
}

input.error {
	border: 1px solid red;
}
</style>
<script>
	var rowChanged = "false"; //Global variable - whether the row data has been updated

	$(document).ready(
		function() {
			//Make use of DATATABLE jquery
			$('#scrollContactHistoryTable').dataTable({
				"order" : [ [ 0, "desc" ] ],
				"scrollY" : "300px", //Height of the table
				"scrollCollapse" : true,
				"paging" : true,
				"aoColumns": [
	                          {"iDataSort": 1},
	                          {"bVisible": false},
	                          {"bSortable": true},
	                          {"bSortable": true},
	                          {"bSortable": true}
				              ]
			});
			
	});
	//End of document ready

	//Put the curtain on Customer Screen
	if (window.opener != null) {
	/* Following reload option reloads parent screen aka Customer Screen */
 //     window.opener.location.reload(false);
		window.opener.loadModalDiv();
	}
	
	//Hide the curtain when CustomerBankInfo screen is closed
 	function OnClose()
 	{
 	    if(window.opener != null && !window.opener.closed)
 	    {
 	   	/* Following reload option reloads parent screen aka Customer Screen */
 //	       window.opener.location.reload(false);
 	       window.opener.hideModalDiv();
 	    }
 	}
 	window.onunload = OnClose;
	
</script>

<div id="one">
	<!--  contentDiv - begin -->
	<div class="item" id="contentDiv" name="contentDiv">
		<form>
			<div>
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width: 250px; text-align: right;">
							<h2>SMS History Information for: &nbsp;&nbsp;&nbsp;</h2></td>
						<td style="width: 350px; text-align: left;">
							<h2><%=custNameFromSMSHistoryScreen %></h2>
						</td>
						<td style="width: 350px; text-align: left;">&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</div>
			
			<!-- Tabular bank info - begin -->
			<table id="scrollContactHistoryTable" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>Sent Date</th>
						<th>Sort Date</th>
						<th>Contact Name</th>
						<th>Contact Number</th>
						<th>Message</th>
					</tr>
				</thead>
				<tbody>
					<%
				       if ((result != null) && (result.size() > 0)) { 
				     	  for (Object object : result) { 
				     		  Map row = (Map) object;
							   String sortRequestInitDate =  row.get("RequestInitiatedTS").toString();
							   String dispRequestInitDate =  DisplayUtil.getDisplayDate(sortRequestInitDate, new SimpleDateFormat("yyyy-MM-dd"));
//							   System.out.println(" sort date " + sortRequestInitDate + " disp date " + dispRequestInitDate);
							   boolean isSent = false;
							   if (row.get("Status").toString().equalsIgnoreCase("sent")) isSent = true;
				     		  
				    %>
				    <tr>
					    <td style="width: 110px" align="left"><%=dispRequestInitDate %></td>
						<td><%=sortRequestInitDate %></td>
					    <td><%=row.get("SMSMobileOwnerName") %></td>
					    <td <% if(isSent) { %>style="color:green;" <% } else { %> 
					    	style="color:red;" title="<%=row.get("FailedReason") %>" <% } %>><%=row.get("SMSMobileNumber")%></td>
					    <td><%=row.get("SMSMessage") %></td>
					 </tr>   
				    <%
				     		  } 
				     	  }

					%>
				</tbody>
			</table>
		</form>
	</div>
	<!--  contentDiv - end -->
</div>
</body>
</html>