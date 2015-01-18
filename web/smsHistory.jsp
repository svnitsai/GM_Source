<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.SMSContactHistoryCRUDProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="com.svnitsai.gm.util.date.DateUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>


<!-- 
 * smsHistory.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen lists SMS history for a given date range.
 * 
-->

<%
System.out.println("Inside smsHistory.jsp...");

String chFromDate = ""; //TS
String chToDate = "";

if (session.getAttribute("jspCHFromDate") != null) { //First time around this will be null
	chFromDate = (String) session.getAttribute("jspCHFromDate");
}
session.removeAttribute("jspCHFromDate"); //So that page will refresh next time around with default

if (session.getAttribute("jspCHToDate") != null) {
	chToDate = (String) session.getAttribute("jspCHToDate");
}
session.removeAttribute("jspCHToDate"); //So that page will refresh next time around

SMSContactHistoryCRUDProvider contactHistoryCRUDProvider = new SMSContactHistoryCRUDProvider();
List result = null;
String chFromDateddmmyyyy = null;
String chToDateddmmyyyy = null;
if (chFromDate == "") { // get default - last one week history
	result = (List) contactHistoryCRUDProvider.getSMSContactHistoryList_OneWeek();
	chFromDateddmmyyyy = DateUtil.getDateWeeksBefore(1);
	chToDateddmmyyyy = DateUtil.getTodayDate();
} else {
	chFromDateddmmyyyy = chFromDate.substring(8,10) + "/" + chFromDate.substring(5, 7) + "/" + chFromDate.substring(0, 4);
	chToDateddmmyyyy = chToDate.substring(8,10) + "/" + chToDate.substring(5, 7) + "/" + chToDate.substring(0, 4);
	result = (List) contactHistoryCRUDProvider.getSMSContactHistoryList
			(chFromDate + " 00:00:00.000", chToDate + " 23:59:00.000"); //Get records for the date range
}

//   if ((result != null) && (result.size() > 0)) { 
// 	  for (Object object : result) { 
// 		  Map row = (Map) object; System.out.println(" ");
// 		  System.out.println(" HistoryId: " + row.get("HistoryId")
// 				  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
// 				  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
// 				  + " CustId: " + row.get("CustId")
// 				  + " CustCode: " + row.get("CustCode")
// 				  + " CustName: " + row.get("CustName")
// 				  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
// 				  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
// 				  + " SMSMessage: " + row.get("SMSMessage")
// 				  + " SMSVendor: " + row.get("SMSVendor")
// 				  + " SentTimeStamp: " + row.get("SentTimeStamp")
// 				  + " FailedReason: " + row.get("FailedReason")
// 				  + " PayCReferenceNumber: " + row.get("PayCReferenceNumber")
// 				  + " Status: " + row.get("Status")
// 				  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
// 				  ); 
// 		  } 
// 	  }
%>
<script type="text/javascript">
	$(document).ready(function() {

		$('#breadCrumps').text("SMS >> History");
		$('#breadCrumps').show();
		
		$('#scrollSMSHistoryTable').dataTable({
			"order" : [ [ 2, "desc" ] ],
			"scrollY" : "250px", //Height of the table
			"scrollCollapse" : true,
			"paging" : true,
			"aoColumns": [
                          {"bSortable": true},
                          {"bSortable": true},
                          {"iDataSort": 3},
                          {"bVisible": false},
                          {"bSortable": true},
                          {"bSortable": false}
			              ]
		});
		
		$.datepicker.setDefaults({dateFormat:"dd/mm/yy", maxDate:0});//Prevent future dates
	    
	});

	var popUpObj;
	function getContactHistory(custId, custName) 
	{
		popUpObj=window.open("/gm/web/customerContactHistory.jsp?custId=" + custId + "&custName=" + custName,
		    "Customer SMS Contact History",
		    "toolbar=no, scrollbars=no,location=no,statusbar=no,menubar=no,status=no,resizable=no,width=950,height=500,left=350,top=150" );
	    	popUpObj.focus(); 
	}
	
	function loadModalDiv()
    	{
	        var bcgDiv = document.getElementById("overlay");
	        bcgDiv.style.display="block";
	}

	function hideModalDiv()
	{
	        var bcgDiv = document.getElementById("overlay");
	        bcgDiv.style.display="none";
	}
	
	//Handle adding a new template CLICK
 	function getContactHistoryDateRange() {
		var chFromDate = $("#selectedFromDate").val(); //20/01/2015
		var chToDate = $("#selectedtoDate").val();
		var editError = false;
		$("#dateErrorMessage").hide();
		$("#selectedFromDate").removeClass('error');
		//from date has to be <= to date
		if (chFromDate.substring(6,10) > chToDate.substring(6,10)) {
			editError = true;
		} else if (chFromDate.substring(6,10) == chToDate.substring(6,10)) {
			if (chFromDate.substring(3,5) > chToDate.substring(3,5)) {
				editError = true;
			} else if (chFromDate.substring(3,5) == chToDate.substring(3,5)) {
				if (chFromDate.substring(0,2) > chToDate.substring(0,2)) {
					editError = true;
				}
			}
		}
		var chFromDateYYYYMMDD = chFromDate.substring(6,10) + '-' + chFromDate.substring(3,5) + '-' +chFromDate.substring(0,2)
		var chToDateYYYYMMDD = chToDate.substring(6,10) + '-' + chToDate.substring(3,5) + '-' + chToDate.substring(0,2)
		if (editError) {
			$("#dateErrorMessage").show();
			$("#selectedFromDate").addClass('error');
		} else {
			$.ajax({
				type : "POST",
				url : '/gm/web/SMSHistoryServlet',
				data : {
					historyAction : 'getHistory',
					jspCHFromDate : chFromDateYYYYMMDD,
					jspCHToDate   : chToDateYYYYMMDD
				},
				success : function(returnData) {
					var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
					if (returnCode == '0') { //Successful return
						location.reload(true);
					} else { //Unsuccessful return
							$("#error-message").dialog({
							modal : true,
							buttons : [ {
								id : "okButton",
								text : "Ok",
								click : function() {
									//Reload page with DB updates
									location.reload(true);
								}
							} ]
						});
					}
				}
			})
		}
 	}

		
</script>


<style>
label.error {
	display: block;
	color: red;
}

input.error {
	border: 1px solid red;
}
</style>

<div id="error-message" title="Error!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Request could not be completed at this time. Please try after some time. If this continues, contact System Administrator!</span>
	</p>
</div>

<div id="oneSMS">
	<div class="item" id="contentDiv" name="contentDiv">
		<!-- Customer Filter by Supplier / Merchant -->
		<div>
			<form action="/gm/web/smsHistoryServlet" method="post">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: right; ">SMS History from&nbsp;&nbsp;</td>
						<td style="width:150px; text-align: left;">
							<input type="text" name="selectedFromDate" id="selectedFromDate" class="datepicker inputBoxBorder" value="<%=chFromDateddmmyyyy %>"
							  style="text-align: center;">
						</td>
						<td style="width:250px; text-align: center;">&nbsp;&nbsp;to&nbsp;&nbsp;
							<input type="text" name="selectedtoDate" id="selectedtoDate" class="datepicker inputBoxBorder" value="<%=chToDateddmmyyyy%>"
							  style="text-align: center;">
						</td>
						<td style="width:320px; text-align: left;">&nbsp;&nbsp;&nbsp;
						  	<button type="button" title="Get History"
								class="refreshButton" onClick="getContactHistoryDateRange();">&nbsp;</button>
						</td>
					</tr>
				</table>
				<div id="dateErrorMessage" style="width: 100%; margin-left: 200px;" class="hide-label err-mesg-font">
					<label>From date should be before to date</label>
				</div>
			</form>
		</div>

		<!--  SMS History table - begins -->
		<form>
			<input type="hidden" name="action" id="action" value="save">
			<table id="scrollSMSHistoryTable" class="display" cellspacing="0"
				width="100%">
				<thead>
					<tr>
						<th>Contact Number</th>
						<th>Customer Name</th>
						<th>Sent Date</th>
						<th>Sortable Date</th>
						<th>Message</th>
						<th>&nbsp;&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<%
					  if ((result != null) && (result.size() > 0)) { 
					  for (Object object : result) { 
						  Map row = (Map) object; 
//						  System.out.println(" ");
// 						  System.out.println(" HistoryId: " + row.get("HistoryId")
// 								  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
// 								  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
// 								  + " CustId: " + row.get("CustId")
// 								  + " CustCode: " + row.get("CustCode")
// 								  + " CustName: " + row.get("CustName")
// 								  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
// 								  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
// 								  + " SMSMessage: " + row.get("SMSMessage")
// 								  + " SMSVendor: " + row.get("SMSVendor")
// 								  + " SentTimeStamp: " + row.get("SentTimeStamp")
// 								  + " FailedReason: " + row.get("FailedReason")
// 								  + " PayCReferenceNumber: " + row.get("PayCReferenceNumber")
// 								  + " Status: " + row.get("Status")
// 								  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
// 								  ); 
						   String sortRequestInitDate =  row.get("RequestInitiatedTS").toString().substring(0,10);
						   String dispRequestInitDate =  DisplayUtil.getDisplayDate(sortRequestInitDate, new SimpleDateFormat("yyyy-MM-dd"));
// 						   System.out.println(" sort date " + sortRequestInitDate + " disp date " + dispRequestInitDate);
						   boolean isSent = false;
						   if (row.get("Status").toString().equalsIgnoreCase("sent")) isSent = true;
					%>
					<tr>
						<td <% if(isSent) { %>style="width: 160px; color:green;" <% } else { %>style="width: 160px; color:red;" 
						       title="<%=row.get("FailedReason")%>" <% } %>align="left"><%=row.get("SMSMobileNumber")%></td>
						<td style="width: 160px" align="left"><%=row.get("CustName") %></td>
						<td style="width: 110px" align="left"><%=dispRequestInitDate %></td>
						<td><%=sortRequestInitDate %></td>
						<td style="width: 300px" align="left"><%=row.get("SMSMessage") %></td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="getContactHistory('<%=row.get("CustId")%>', '<%=row.get("CustName")%>');">&nbsp;</button></td>
					</tr>
					<%
						  } 
					  }
					%>
				</tbody>
			</table>
			<!--  Contact History table ends -->
		</form>
	</div>
	<!-- Overlay transparent screen to implement modal window -->
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
	
</div>
<!--  TODO: delme -->
<div id="waterMark" class="hide-label"> SAMPLE ONLY </div>
<jsp:include page="footer.jsp" />