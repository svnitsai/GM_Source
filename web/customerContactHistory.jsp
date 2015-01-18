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
// 	//customerBankInfoId is passed from CustomerBankInfoServlet
// 	String customerBankInfoId = "";
// 	boolean addOptionClicked = false;
// 	boolean rowBeingEdited = false;
	
// 	if (session.getAttribute("customerBankInfoId") != null) {
// 		customerBankInfoId = (String) session.getAttribute("customerBankInfoId");
// 		if (customerBankInfoId.length() > 0) {
// 			rowBeingEdited = true;
// 		}
// 	}
// 	//System.out.println (" JSP - customerBankInfoId " + customerBankInfoId);
// 	session.removeAttribute("customerBankInfoId"); //So that page will refresh next time around

// 	//Add option has 0 in customerBankInfoId
// 	if (customerBankInfoId.equals("0")) {
// 		addOptionClicked=true;
// 	}
// 	//System.out.println (" JSP - addOptionClicked " + addOptionClicked);
	
// 	String custNameFromCustomerScreen = (String) request.getParameter("custName");
// 	if (custNameFromCustomerScreen == null) {
// 		custNameFromCustomerScreen =  (String) session.getAttribute("custNameFromCustomerScreen");
// 	} else {
// 		session.setAttribute("custNameFromCustomerScreen", custNameFromCustomerScreen);
// 	}

// 	String custCityFromCustomerScreen = (String) request.getParameter("custCity");
// 	if (custCityFromCustomerScreen == null) {
// 		custCityFromCustomerScreen =  (String) session.getAttribute("custCityFromCustomerScreen");
// 	} else {
// 		session.setAttribute("custCityFromCustomerScreen", custCityFromCustomerScreen);
// 	}

// 	String custProcTypeFromCustomerScreen = (String) request.getParameter("custProcType");
// 	if (custProcTypeFromCustomerScreen == null) {
// 		custProcTypeFromCustomerScreen =  (String) session.getAttribute("custProcTypeFromCustomerScreen");
// 	} else {
// 		session.setAttribute("custProcTypeFromCustomerScreen", custProcTypeFromCustomerScreen);
// 	}
	
// 	//Get Customer Banks for a given customer 
//     // CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
//      //List result = (List) customerBankInfoCrud.getCustomerBankList(Util.convertToLong(custIdFromCustomerScreen));

			
			
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
       if ((result != null) && (result.size() > 0)) { 
     	  for (Object object : result) { 
     		  Map row = (Map) object; System.out.println(" ");
//      		  System.out.println(" HistoryId: " + row.get("HistoryId")
//      				  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
//      				  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
//      				  + " CustId: " + row.get("CustId")
//      				  + " CustCode: " + row.get("CustCode")
//      				  + " CustName: " + row.get("CustName")
//      				  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
//      				  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
//      				  + " SMSMessage: " + row.get("SMSMessage")
//      				  + " SMSVendor: " + row.get("SMSVendor")
//      				  + " SentTimeStamp: " + row.get("SentTimeStamp")
//      				  + " FailedReason: " + row.get("FailedReason")
//      				  + " PayCReferenceNumber: " + row.get("PayCReferenceNumber")
//      				  + " Status: " + row.get("Status")
//      				  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
//      				  ); 
     		  } 
     	  }
			
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
			
// 			//Check if input elements were changed by the user	
// 			$(".inputElements").each(
// 				function() {
// 					var elem = $(this);

// 					// Save current value of element to compare later
// 					if (elem.data('oldVal') != "")
// 						elem.data('oldVal', elem.val());

// 					// Look for changes in the value
// 					elem.bind(
// 						"propertychange change click keyup input paste",
// 						function(event) {
// 							if (elem.data('oldVal') != elem.val()) {
// 								rowChanged = "true";
// 							}
// 					});
// 			});
			
	});
	//End of document ready

// 	//When EDIT button is pressed first
// 	function startUpdate(rowActionIn) {
// 		$.ajax({
// 			type : "POST",
// 			url : '/gm/web/CustomerBankInfoServlet',
// 			data : {
// 				rowAction : rowActionIn
// 			},
// 			success : function(returnData) {
// 				var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
// 				if (returnCode == '0') { //Successful return
// 					location.reload(true);
// 				} else { //Unsuccessful return
// 						$("#error-message").dialog({
// 						modal : true,
// 						buttons : [ {
// 							id : "okButton",
// 							text : "Ok",
// 							click : function() {
// 								//Reload page with DB updates
// 								location.reload(true);
// 							}
// 						} ]
// 					});
// 				}
// 			}
// 		})
// 	}
	
// 	//When SAVE button pressed on update; following function handles it
// 	function saveUpdateChanges(currentDispKey) {
// 		form_valid = "true";
// 		if ($("#custBankName").val().length == 0) {
// 			$("#custBankName-mesg").show();
// 			form_valid = "false";
// 			$("#custBankName").addClass("error")
// 		} else {
// 			$("#custBankName-mesg").hide();
// 			$("#custBankName").removeClass("error")
// 		}

// 		if (form_valid == "true") {
// 			//Call database update
// 			$.ajax({
// 						type : "POST",
// 						url : '/gm/web/CustomerBankInfoServlet',
// 						data : {
// 							rowAction : 'update',
// 							jspcustBankBranch : $("#custBankBranch").val(),
// 							jspcustAccountName : $("#custAccountName").val(),
// 							jspcustAccountType : $("#custAccountType").val(),
// 							jspcustAccountNumber : $("#custAccountNumber").val(),
// 							jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
// 							jspcustBankId : $("#custBankId").val(),
// 							jspCustBankName : $("#custBankName").val()
// 						},
// 						success : function(returnData) {
// 							var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
// 							if (returnCode == '0') { //Successful return
// 								$("#update-message").dialog({
// 									modal : true,
// 									buttons : [ {
// 										id : "okButton",
// 										text : "Ok",
// 										click : function() {
// 											//Reload page with DB updates
// 											location.reload(true);
// 										}
// 									} ]
// 								});
// 							} else { //Unsuccessful return
// 	 							$("#error-message").dialog({
// 									modal : true,
// 									buttons : [ {
// 										id : "okButton",
// 										text : "Ok",
// 										click : function() {
// 											//Reload page with DB updates
// 											location.reload(true);
// 										}
// 									} ]
// 								});
// 							}
// 						}
// 					})

// 		} else {
// 			//remove pressed state on the button
// 			$("#saveButton").removeClass("ui-state-focus ui-state-hover");
// 		}
// 	}

// 	//When CANCEL button pressed on update; following function handles it
// 	function cancelUpdateChanges(currentDispKey) {
// 		//remove pressed state on the button
// 		//TODO: not working!
// 		$("#cancelButton").removeClass("ui-state-focus ui-state-hover");
// 		if (rowChanged == "true") {
// 			$("#cancel-message").dialog({
// 				modal : true,
// 				buttons : [ {
// 					id : "yesButton",
// 					text : "Yes",
// 					tabIndex : -1,
// 					click : function() {
// 						rowChanged = "false";
// 						$(this).dialog("close");
// 						//Reload page after cancel
// 						location.reload(true);
// 					}
// 				}, {
// 					id : "noButton",
// 					text : "No",
// 					click : function() {
// 						$(this).dialog("close");
// 						//Just Close
// 					}
// 				} ]

// 			});
// 		} else {
// 			//Reload page 
// 			location.reload(true);
// 		}
// 	}

// 	//When ADD button is pressed first
// 	function startAdd(rowActionIn) {
// 		$.ajax({
// 			type : "POST",
// 			url : '/gm/web/CustomerBankInfoServlet',
// 			data : {
// 				rowAction : rowActionIn
// 			},
// 			success : function(returnData) {
// 				var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
// 				if (returnCode == '0') { //Successful return
// 					location.reload(true);
// 				} else { //Unsuccessful return
// 						$("#error-message").dialog({
// 						modal : true,
// 						buttons : [ {
// 							id : "okButton",
// 							text : "Ok",
// 							click : function() {
// 								//Reload page with DB updates
// 								location.reload(true);
// 							}
// 						} ]
// 					});
// 				}
// 			}
// 		})
//	}
	
// 	//When SAVE button pressed on ADD; following function handles it
// 	function saveAddChanges(currentDispKey) {
// 		form_valid = "true";
// 		if ($("#custBankName").val().length == 0) {
// 			$("#custBankName-mesg").show();
// 			form_valid = "false";
// 			$("#custBankName").addClass("error")
// 		} else {
// 			$("#custBankName-mesg").hide();
// 			$("#custBankName").removeClass("error")
// 		}

// 		if (form_valid == "true") {
// 			//Call database update
// 			$.ajax({
// 						type : "POST",
// 						url : '/gm/web/CustomerBankInfoServlet',
// 						data : {
// 							rowAction : 'add',
// 							jspcustBankBranch : $("#custBankBranch").val(),
// 							jspcustAccountName : $("#custAccountName").val(),
// 							jspcustAccountType : $("#custAccountType").val(),
// 							jspcustAccountNumber : $("#custAccountNumber").val(),
// 							jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
// 							jspCustBankName : $("#custBankName").val()
// 						},
// 						success : function(returnData) {
// 							var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
// 							if (returnCode == '0') { //Successful return
// 								$("#add-message").dialog({
// 									modal : true,
// 									buttons : [ {
// 										id : "okButton",
// 										text : "Ok",
// 										click : function() {
// 											//Reload page with DB updates
// 											location.reload(true);
// 										}
// 									} ]
// 								});
// 							} else { //Unsuccessful return
// 	 							$("#error-message").dialog({
// 									modal : true,
// 									buttons : [ {
// 										id : "okButton",
// 										text : "Ok",
// 										click : function() {
// 											//Reload page with DB updates
// 											location.reload(true);
// 										}
// 									} ]
// 								});
// 							}
// 						}
// 					});

// 		} else {
// 			//remove pressed state on the button
// 			$("#addSaveButton").removeClass("ui-state-focus ui-state-hover");
// 		}
// 	}

// 	//When CANCEL button pressed on ADD; following function handles it
// 	function cancelAddChanges(currentDispKey) {
// 		$("#addCancelButton").removeClass("ui-state-focus ui-state-hover");

// 		if (rowChanged == "true") {
// 			$("#cancel-message").dialog(
// 				{
// 					modal : true,
// 					buttons : [
// 							{
// 								id : "yesButton",
// 								text : "Yes",
// 								tabIndex : -1,
// 								click : function() {
// 									rowChanged = "false";
// 									$(this).dialog("close");
// 									//Reload Page
// 									location.reload(true);
// 								}
// 							},
// 							{
// 								id : "noButton",
// 								text : "No",
// 								click : function() {
// 									//remove pressed state on the button
// 									$("#addCancelButton").removeClass("ui-state-focus ui-state-hover");
// 									$(this).dialog("close");
// 									//Just Close
// 								}
// 							} ]
// 				});
// 		} else {
// 			//Reload page
// 			location.reload(true);
// 		}
// 	}

// 	//When DELETE button pressed; following function handles it
// 	function deleteChanges(currentDispKey) {
// 		$("#delete-warning").dialog(
// 			{
// 				modal : true,
// 				buttons : [
// 						{
// 							id : "yesButton",
// 							text : "Yes",
// 							tabIndex : -1,
// 							click : function() {
// 								rowChanged = "false";
// 								$.ajax({
// 										type : "POST",
// 										url : '/gm/web/CustomerBankInfoServlet',
// 										data : {
// 											rowAction : 'delete',
// 											jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
// 											jspcustBankId : $("#custBankId").val()
// 										},
// 										success : function(returnData) {
// 											var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
// 											if (returnCode == '0') { //Successful return
// 												$("#delete-message").dialog({
// 													modal : true,
// 													buttons : [ {
// 														id : "okButton",
// 														text : "Ok",
// 														click : function() {
// 															//Reload page with DB updates
// 															location.reload(true);
// 														}
// 													} ]
// 												});
// 											} else { //Unsuccessful return
// 					 							$("#error-message").dialog({
// 													modal : true,
// 													buttons : [ {
// 														id : "okButton",
// 														text : "Ok",
// 														click : function() {
// 															//Reload page with DB updates
// 															location.reload(true);
// 														}
// 													} ]
// 												});
// 											}
// 										}
// 									})
// 							}
// 						},
// 						{
// 							id : "noButton",
// 							text : "No",
// 							click : function() {
// 								//remove pressed state on the button
// 								$("#deleteButton").removeClass("ui-state-focus ui-state-hover");
// 								//Just Close
// 								$(this).dialog("close");
// 							}
// 						} ]
// 			});
// 	}

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

<!-- <div id="cancel-message" title="Warning!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon  ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> -->
<!-- 		<span style="color: red;">Changes made will be lost! Do you want to cancel?</span> -->
<!-- 	</p> -->
<!-- </div> -->

<!-- <div id="add-message" title="Success!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: green;">Bank Information Successfully added!</span> -->
<!-- 	</p> -->
<!-- </div> -->

<!-- <div id="update-message" title="Success!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-info " style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: green;">Bank Information Successfully updated!</span> -->
<!-- 	</p> -->
<!-- </div> -->

<!-- <div id="delete-message" title="Success!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-info " style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: green;">Bank Information Successfully deleted!</span> -->
<!-- 	</p> -->
<!-- </div> -->

<!-- <div id="delete-warning" title="Warning!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: red;">Bank Information will be permanently deleted.  Do you want to continue deleting?</span> -->
<!-- 	</p> -->
<!-- </div> -->

<!-- <div id="error-message" title="Error!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: red;">Request could not be completed at this time. Please try after some time. If this continues, contact System Administrator!</span> -->
<!-- 	</p> -->
<!-- </div> -->

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
<%-- 						<% --%>
<%-- 						%> --%>
<!-- 							<td style="width: 220px; text-align: right;"> -->
<!-- 							<button type="submit" class="addButton" name="rowAction" value="customerBankInfoAdd" disabled>&nbsp; -->
<!-- 							</button>&nbsp;&nbsp;&nbsp;Add Bank Information</td> -->
<%-- 						<% --%>
<%-- 						%> --%>
<!-- 							<td style="width: 220px; text-align: right;"> -->
<!-- 							<button type="submit" class="addButton" name="rowAction" -->
<!-- 									value="customerBankInfoAdd" onClick="startAdd('customerBankInfoAdd'); return false;">&nbsp; -->
<!-- 							</button>&nbsp;&nbsp;&nbsp;Add Bank Information</td> -->
<%-- 						<% --%>
<%-- 						%> --%>
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
				     		  Map row = (Map) object; System.out.println(" ");
// 				     		  System.out.println(" HistoryId: " + row.get("HistoryId")
// 				     				  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
// 				     				  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
// 				     				  + " CustId: " + row.get("CustId")
// 				     				  + " CustCode: " + row.get("CustCode")
// 				     				  + " CustName: " + row.get("CustName")
// 				     				  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
// 				     				  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
// 				     				  + " SMSMessage: " + row.get("SMSMessage")
// 				     				  + " SMSVendor: " + row.get("SMSVendor")
// 				     				  + " SentTimeStamp: " + row.get("SentTimeStamp")
// 				     				  + " FailedReason: " + row.get("FailedReason")
// 				     				  + " PayCReferenceNumber: " + row.get("PayCReferenceNumber")
// 				     				  + " Status: " + row.get("Status")
// 				     				  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
// 				     				  ); 
							   String sortRequestInitDate =  row.get("RequestInitiatedTS").toString().substring(0,10);
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