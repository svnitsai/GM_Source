<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.SMSRecipientProvider"%>
<%@ page import="com.svnitsai.gm.database.provider.SMSTemplateCRUDProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<%@ page import="com.svnitsai.gm.Util"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<!-- 
 * smsSend.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen helps in picking up recipient list, content and in sending SMS
 * 
-->

<%
//Default (on first entry), screen will show recipient list of PAST_DUE
//Second time around, SMSSendServlet will pass option selected in session variable
String recipientList = "Past-due";
if (session.getAttribute("recipientList") != null) {
	recipientList = (String) session.getAttribute("recipientList");
}

SMSRecipientProvider sMSRecipientProvider = new SMSRecipientProvider();

List recipientResult = null;
boolean pastDueRecipients = false;
boolean futureDueRecipients = false;
boolean allDueRecipients = false;
if (recipientList.equalsIgnoreCase("Past-due")) {
	recipientResult = (List) sMSRecipientProvider.getSMSRecipientPastDue();
	pastDueRecipients = true;
} else if (recipientList.equalsIgnoreCase("Immediate-due")) {
	recipientResult = (List) sMSRecipientProvider.getSMSRecipientFutureDue();
	futureDueRecipients = true;
} else {
	recipientResult = (List) sMSRecipientProvider.getSMSRecipientDue();
	allDueRecipients = true;
}

//Message tab begins
	String templateName = "";
	if (session.getAttribute("templateName") != null) {
		templateName = (String) session.getAttribute("templateName");
	}
	session.removeAttribute("templateName"); //So that page will refresh next time around
	List messageResult = null;
	SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();
	  
 	messageResult = (List) SMSTemplateInfoCrud.getAllSMSTemplateList(); //Get all templates


%>
<script type="text/javascript">
//Global variables
var CustIdArray = [];
var ReferenceNumberArray = [];
var CustNameArray = [];

var SMSMobileOwnerNameArray = [];
var SMSMobileNumberArray = [];
var DueDateArray = [];

var DueAmountArray = [];
var InvoiceNumberArray = [];
var SMSMessage;

var CustCodeArray = [];
var smsVendor;

$(document).ready(
	function() {

		$('#breadCrumps').text("SMS >> Send");
		$('#breadCrumps').show();

		$('#scrollSMSRecipientTable').dataTable({
			"order" : [ [ 4, "asc" ] ],
			"scrollY" : "120px", //Height of the table
			"scrollCollapse" : true,
			"paging" : true,
			 "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
             "iDisplayLength": -1,
			"aoColumns" : [ {
				"bSortable" : false
			}, {
				"bSortable" : true
			}, {
				"bSortable" : true
			}, {
				"bSortable" : true
			}, {
				"iDataSort" : 5
			}, {
				"bVisible" : false
			}, {
				"bSortable" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}, {
				"bVisible" : true
			}]
		});

		var selectTable =
			$('#scrollSMSSelectedRecipientTable').dataTable({
			"order" : [ [ 2, "asc" ] ],
			"scrollY" : "100px", //Height of the table
			//"scrollX": "600px",
			//"bAutoWidth": false,
			"scrollCollapse" : true,
			"bFilter" : false,
			"paging" : false,
			"aoColumns" : [ {
				"bSortable" : true
			}, {
				"bSortable" : true
			}, {
				"bSortable" : true
			} ]
		});

		//Jquery Tab
		$(function() {
			$("#tabs").tabs( {
				activate: function (event, ui) {
					var $activeTab = $('#tabs').tabs('option', 'active');

// 					if ($activeTab == 0) {//RECIPIENTS tab clicked
// 						jQuery('#dueDateHeader').trigger('click');
// 					}

					if ($activeTab == 2) {//SEND tab clicked
						selectTable.fnClearTable(); //Clear RECIPIENT selected table & rebuild
						CustIdArray = []; //Clear All array elements
						ReferenceNumberArray  = [];
						CustNameArray = [];
						SMSMobileOwnerNameArray = [];
						SMSMobileNumberArray = [];
						DueDateArray = [];
						DueAmountArray = [];
						InvoiceNumberArray = [];
						CustCodeArray = [];
						smsVendor = $('#sendOptionSelect').val();
						$('#Recipient #scrollSMSRecipientTable tbody tr').each(function() {
							if ($(this).hasClass("includeRow")) {
							    var customerId = $(this).find(".customerIDCell").html();
							    var customerName = $(this).find(".customerNameCell").html();
							    var smsMobileOwnerName = $(this).find(".smsMobileOwnerNameCell").html();
							    var smsMobileNumber = $(this).find(".smsMobileNumberCell").html();
							    var dueDate = $(this).find(".dueDateCell").html();
							    var dueAmount = $(this).find(".dueAmountCell").html();
							    var invoiceNumber = $(this).find(".invoiceNumberCell").html();
							    var custCode = $(this).find(".custCodeCell").html();
							    var customerName = $(this).find(".customerNameCell").html();
							    var ReferencNumber = $(this).find(".ReferencNumberCell").html();
								if ($(this).find('#includeCheckBox').is(":checked")) {
									selectTable.fnAddData( [
							 								'<td align="left">' + customerName +'</td>',
							 								'<td align="left">' + smsMobileOwnerName + '</td>' ,
							 								'<td align="left">' + smsMobileNumber + '</td>' ]);
									//Save the selected recipients info
									CustIdArray.push(customerId);
									ReferenceNumberArray.push(ReferencNumber);
									CustNameArray.push(customerName);
									SMSMobileOwnerNameArray.push(smsMobileOwnerName);
									SMSMobileNumberArray.push(smsMobileNumber);
									DueDateArray.push(dueDate);
									DueAmountArray.push(dueAmount);
									InvoiceNumberArray.push(invoiceNumber);
									CustCodeArray.push(custCode);

								} 
							}
						});

						jQuery('#selectedCustomerName').trigger('click'); //Datatable head redraw
						
						//Copy selected message to SEND tab
					    $("#Message .templateMessage").each(function() {
							if (!$(this).hasClass('hide-label')) { //Find SMS message visible to copy
						    	$("#Send #selectedMessageBox").val($(this).val());
								SMSMessageArray = $(this).val();
							}
						});
						
					    $("#Send #errorMesg").addClass('hide-label');
						$("#sendSMSButton").prop('title', '');
						$("#Send #selectedMessageBox").removeClass('error');
						$("#sendSMSButton").attr("disabled", false);
					    
						//Validate recipients
						if (selectTable.fnSettings().fnRecordsTotal() == 0) {
							$("#Send #errorMesg").text('Please select recipient(s) using Recipient tab.');
							$("#Send #errorMesg").removeClass('hide-label');
							$("#sendSMSButton").attr("disabled", "disabled");
							$("#sendSMSButton").prop('title', 'Please correct the above shown error.');
						} else {
							if ($("#Send #selectedMessageBox").val().length == 0) {
								//Validate Message
								$("#Send #errorMesg").text('Please select message to send using Message tab.');
								$("#Send #errorMesg").removeClass('hide-label');
								$("#sendSMSButton").attr("disabled", "disabled");
								$("#sendSMSButton").prop('title', 'Please correct the above shown error.');
								$("#Send #selectedMessageBox").addClass('error');
							}
						}
					}//SEND ends
				}
			});
		});

		//Implement CLEAR ALL / INCLUDE ALL
		$("#actionTable input:radio").click(
			function() {
				if ($(this).val() == "includeAll") {
					// include all customers
					$("#scrollSMSRecipientTable .includeQualified")
							.each(function() {
								$(this).prop('checked', true);
							});
				}
				if ($(this).val() == "clearAll") {
					// include all customers
					$("#scrollSMSRecipientTable .includeQualified")
							.each(function() {
								$(this).prop('checked', false);
							});
				}
			})

		//jQuery('#dueDateHeader').trigger('click'); //TODO fix header positioning of recipient table in RECIPIENT tab
		
		$("#Message #customerCriteria").addClass("inputBoxBorder");
		$('#messageBox').attr("disabled", "disabled");
		$('#selectedMessageBox').text($('#messageBox').text());
		
	    $("#Message .templateMessage").each(function() {
			if (!$(this).hasClass('hide-label')) {
				//Load initial sms message's length
				$("#smsLengthLabel").text("SMS Length: " + $(this).val().length);
			}
		});
	    
		//Check if input elements were changed by the user	
		$(".inputElements").each(
			function() {
				var elem = $(this);
				if (!elem.hasClass('hide-label')) { //Length of visible sms message
					$("#smsLengthLabel").text("SMS Length: " + elem.val().length); //Show SMS length as soon as loaded
				}
				// Look for changes in the value
				elem.bind(
					"propertychange change click keyup input paste",
					function(event) {
						$("#smsLengthLabel").text("SMS Length: " + elem.val().length);
				});
		});



	});

	//When recipient dropdown is changed
	$(document).on('change',"#Recipient #customerCriteria",function () 
	 	{ 
		    var optionSelected = $(this).find("option:selected");
	 	    var valueSelected  = optionSelected.val();
			$.ajax({
				type : "POST",
				url : '/gm/web/SMSSendServlet',
				data : {
					smsAction : 'getRecipients',
					jspRecipientList : valueSelected
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
									//Reload page
									location.reload(true);
								}
							} ]
						});
					}
				}
			})
	 	})
	 	
	//Handles when drop down TEMPLATE selection changes
 	$(document).on('change',"#Message #customerCriteria",function () 
 	{
	    var optionSelected = $(this).find("option:selected");
 	    var valueSelected  = optionSelected.val();
	    $(".templateMessage").each(function() {
	     	if ($(this).attr('name') == valueSelected) {
	     		 $(this).removeClass("hide-label");
 				//Length of visible sms message
				$("#smsLengthLabel").text("SMS Length: " + $(this).val().length);
	     	 } else $(this).addClass("hide-label");
		})
 	})
 	
 	//When SEND SMS button pressed - following function handles it
	function sendSMS() {
		$("#sendSMSButton").removeClass("ui-state-focus ui-state-hover");
		$("#send-message").dialog({
			modal : true,
			buttons : [ {
				id : "yesButton",
				text : "Yes",
				tabIndex : -1,
				click : function() {
					$.ajax({
						type : "POST",
					    beforeSend: function() {
					           //call loading image indicator here
					           $("#spinner-info-message").dialog({
					        	   modal : true
					           });
					      },
						url : '/gm/web/SMSSendServlet',
						data : {
							smsAction : 'sendSMS',
							jspsmsVendor : smsVendor,
							jspCustIdArray : CustIdArray,
							jspReferenceNumberArray :  ReferenceNumberArray,
							jspCustNameArray : CustNameArray ,
							jspSMSMobileOwnerNameArray : SMSMobileOwnerNameArray ,
							jspSMSMobileNumberArray : SMSMobileNumberArray ,
							jspDueDateArray : DueDateArray ,
							jspDueAmountArray : DueAmountArray ,
							jspInvoiceNumberArray : InvoiceNumberArray ,
							jspSMSMessageArray : SMSMessageArray ,
							jspCustCodeArray : CustCodeArray  
						},
						success : function(returnData) {
							var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
							if (returnCode == '0') { //Successful return
								$("#dynamic-info-message .textMsg").text(returnData.substring(13, returnData.length));
								$("#dynamic-info-message").dialog({
									modal : true,
									buttons : [ {
										id : "okButton",
										text : "Ok",
										click : function() {
											//Reload page
											location.reload(true);
										}
									} ]
								});
								//location.reload(true);
							} else { //Unsuccessful return - return code is 4 digit
									$("#dynamic-error-message .textMsg").text(returnData.substring(16,returnData.length));
									$("#dynamic-error-message").dialog({
									modal : true,
									buttons : [ {
										id : "okButton",
										text : "Ok",
										click : function() {
											//Reload page
											location.reload(true);
										}
									} ]
								});
							}
						}
					})
					//Reload page after cancel
					//location.reload(true);
				}
			}, {
				id : "noButton",
				text : "No",
				click : function() {
					$(this).dialog("close");
					//Just Close
				}
			} ]

		});
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

<div id="dynamic-error-message" title="Error!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;" class="textMsg"></span>
	</p>
</div>

<div id="dynamic-info-message" title="Information!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;" class="textMsg"></span>
	</p>
</div>

<div id="spinner-info-message" title="Information!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;" class="textMsg">SMS is being sent!  Please wait...</span>
		<img id="imgAjaxLoader" class="ajaxLoader" src="css/images/ajax-loader.gif" />
	</p>
</div>

<div id="send-message" title="Confirmation!" class="hide-label">
	<p>
		<span class="ui-icon  ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
		<span style="color: red;">About to send SMS as requested!  Do you want to proceed?</span>
	</p>
</div>
<!-- <div id="cancel-message" title="Warning!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon  ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> -->
<!-- 		<span style="color: red;">Changes made will be lost! Do you want to cancel?</span> -->
<!-- 	</p> -->
<!-- </div> -->

<div id="error-message" title="Error!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Request could not be completed at this time. Please try after some time. If this continues, contact System Administrator!</span>
	</p>
</div>

<!-- <div id="update-message" title="Success!" class="hide-label"> -->
<!-- 	<p> -->
<!-- 		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span>  -->
<!-- 		<span style="color: green;">Template saved to system successfully!</span> -->
<!-- 	</p> -->
<!-- </div> -->

<div id="oneSMS">
	<div class="item" id="contentDiv" name="contentDiv">
		<div id="tabs">
			<ul>
				<li><a href="#Recipient">Recipients</a></li>
				<li><a href="#Message">Message</a></li>
				<li><a href="#Send">Send</a></li>
			</ul>
			<div id="Recipient">
				<table id="actionTable"
					style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width: 250px; text-align: right;">Customers&nbsp;&nbsp;</td>
						<td style="width: 250px; text-align: left;"><select
							id="customerCriteria" class="inputCustomerCriteria inputBoxBorder"
							style="width: 180px;">
								<option value="Past-due" <% if (pastDueRecipients) { %>selected="selected" <% } %>>Past Due</option>
								<option value="Immediate-due" <% if (futureDueRecipients) { %>selected="selected" <% } %>>Immediate Due</option>
								<option value="All-due" <% if (allDueRecipients) { %>selected="selected" <% } %>>All Due</option>
						</select></td>
						<td style="width: 250px; text-align: right;"><input
							type="radio" name="filterSelection" id="filterSelection"
							value="includeAll" title="Send SMS to all">&nbsp;&nbsp;&nbsp;Include
							All</td>
						<td style="width: 220px; text-align: center;"><input
							type="radio" name="filterSelection" id="filterSelection"
							value="clearAll" title="Clear All Selection"  checked>&nbsp;&nbsp;&nbsp;Clear
							All</td>
					</tr>
				</table>
				<table id="scrollSMSRecipientTable" class="display" cellspacing="0" style="margin-left: 0px; clear: both; width: 855px;">
					<thead>
						<tr>
							<th>Include</th>
							<th>Customer Name</th>
							<th>Contact Name</th>
							<th>Contact Number</th>
							<th id="dueDateHeader">Due Date</th>
							<th>Sortable Due Date</th>
							<th>Due Amount</th>
							
							<th style="display: none;">Due Date</th>
							<th style="display: none;">Due Amount</th>
							<!-- Following columns are invisible -->				
							<th style="display: none;">Invoice Number</th>
							<th style="display: none;">Customer Code</th>
							<th style="display: none;">Customer Name</th>
							<th style="display: none;">Customer Id</th>
							<th style="display: none;">ReferenceNumber</th>
						</tr>
					</thead>
					<tbody>
						<%
							if ((recipientResult != null) && (recipientResult.size() > 0)) 
							{ for (Object object : recipientResult) { 
								  Map row = (Map) object;
								  boolean smsQualified = false;
								  if (Util.convertToLong(row.get("SMSMobileNumber").toString().trim()) != 0) {
									  smsQualified = true;
								  }
									String dispReceivableBalanceDue = " "
											+ DisplayUtil.getDisplayAmount(row.get("BalanceDue").toString());
									String dispAdjustedDueDate = DisplayUtil.getDisplayDate(row
											.get("AdjustedDueDate").toString(),
											new SimpleDateFormat("yyyy/MM/dd"));
									String sortAdjustedDueDate = row.get("AdjustedDueDate").toString(); //Use yyyy/mm/dd date for sorting
						%>
							<tr <% if (smsQualified) { %> class="includeRow" <% } %>>
								<td><input id="includeCheckBox" type="checkbox" <% if (smsQualified) { %>class="includeQualified" <% } else { %>
									disabled title="Incomplete SMS Contact Number"<% } %> 
									name="checkbox" value="value" style="margin-left: 25px;"
									></td> <!-- TODO: Checked only on first time! -->
								<td class="customerNameCell" align="left"><%=row.get("CustomerName") %></td>
								<td class="smsMobileOwnerNameCell" align="left"><%=row.get("SMSMobileOwnerName")%></td>
								<td class="smsMobileNumberCell" align="right"><%=row.get("SMSMobileNumber")%></td>
								<td class="dueDate" align="left"><%=dispAdjustedDueDate%></td>
								<td><%=sortAdjustedDueDate%></td>
								<td class="dueAmount" align="right"><span
									style="font-family: DejaVu Sans;">&#x20b9; </span><%=dispReceivableBalanceDue%></td>
								
								<td class="dueDateCell" style="display: none;"><%=sortAdjustedDueDate %></td>
								<td class="dueAmountCell" style="display: none;"><%=row.get("BalanceDue")%></td>

								<td class="invoiceNumberCell" style="display: none;"><%=row.get("InvoiceNumber")%></td>
								<td class="custCodeCell" style="display: none;"><%=row.get("CustCode")%></td>
								<td class="customerNameCell" style="display: none;"><%=row.get("CustomerName")%></td>
								<td class="customerIDCell" style="display: none;"><%=row.get("CustId")%></td>
								<td class="ReferencNumberCell" style="display: none;"><%=row.get("ReferenceNumber")%></td>
							</tr>
						<%
								} 
							}
						%>
					</tbody>
				</table>

			</div>
			<!-- Recipients tab ends -->

			<div id="Message">
				<table id="messageActionTable"
					style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width: 250px; text-align: right;">Choose your
							message&nbsp;&nbsp;</td>
						<td style="width: 250px; text-align: left;"><select
							id="customerCriteria" class="inputCustomerCriteria"
							style="width: 180px;">
							<%if ((messageResult != null) && (messageResult.size() > 0)) { 
							  int templateLoop = 0;
							  for (Object object : messageResult) { 
								  templateLoop ++;
								  Map row = (Map) object; 
							%>
								<option value="<%=row.get("TemplateName")%>" <% if (templateLoop == 1) { %>selected="selected" <% } %>>Template:
									<%=row.get("TemplateName")%></option>
							<% }
							}
							%>
								<option value="GenericSMS">Generic SMS</option>
						</select></td>
						<td style="width: 250px; text-align: right;"></td>
						<td style="width: 220px; text-align: center;"></td>
					</tr>
				</table>
					 <%if ((messageResult != null) && (messageResult.size() > 0)) { 
						  int templateLoop = 0;
						  for (Object object : messageResult) { 
							  templateLoop ++;
							  Map row = (Map) object; 
							  String templatesmsMessage = row.get("Template").toString().trim();
						%>
							<textarea id="messageBox" name="<%=row.get("TemplateName")%>" maxlength="160"
							placeholder="Please enter your individual SMS message here.  Please do not use any variables. Max 160 chars."
							style="size: 500px; width: 550px; height: 150px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;"
							<% if (templateLoop != 1) {%> class="inputBoxBorder templateMessage hide-label" <% } else { %> class="inputBoxBorder templateMessage" <% } %> 
							disabled><%=templatesmsMessage%></textarea>
					<% }
					}
					%>
					<textarea id="messageBox" name="GenericSMS" maxlength="160"
					placeholder="Please enter your generic SMS message here.  Message will be sent as it is.  Please do not use any variables. Max 160 chars."
					style="size: 500px; width: 550px; height: 150px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;"
					class="hide-label templateMessage inputBoxBorder inputElements"
					></textarea>
					<label id="smsLengthLabel" style="font-size: 9px; margin-left: 30px; font-style: italic;">SMS Length: 123</label>
			</div>
			<!-- Message tab ends -->

			<div id="Send" style="height: 335px;">
				<div id="topWindow" style="height: 298px;">
					<div id="errorDiv" style="width: 100%; margin-left: 200px; padding-bottom: 5px;">
						<label id="errorMesg" class="err-mesg-font hide-label">Invalid variable used in template.</label>
					</div>
 					<table id="titleTable" class="display" cellspacing="0">
 						<tbody>
 							<tr> <!-- Recipients -->
 								<td style="width: 200px; text-align: right; font-weight: bold;"> Recipient(s) :
 								</td>
 								<td> 
									<table id="scrollSMSSelectedRecipientTable" class="display" style="width: 100%;"
										cellspacing="0">
										<thead>
											<tr>
												<th id="selectedCustomerName">Customer Name</th>
												<th>Contact Name</th>
												<th>Contact Number</th>
											</tr>
										</thead> <!-- Dynamically built table -->
									</table>
 								</td>
 							</tr>
 							<tr> <!-- Recipients -->
 								<td style="width: 200px; text-align: right; font-weight: bold;"> Message :
 								</td>
 								<td>
				 					<textarea id="selectedMessageBox" class="inputBoxBorder"
										name="selectedSMSMessage"
										style="width: 520px; margin-top: 25px; margin-left: 30px; size: 500px; resize: none; text-align: left; line-height: 1.8; 
				 						       padding-left: 20px; padding-right: 20px;" 
										disabled></textarea> <!-- Dynamically copied SMS message -->
 								</td>
 							</tr>
 						</tbody>
					</table>
				</div>
				<div id="sendOption"
					style="width: 100%; position: absolute; margin-top: 12px; font-weight: bold;">
					<table id="sendActionTable"
						style="border-collapse: collapse; height: 35px;">
						<tr style="background-color: rgba(241, 245, 235, 0.61);">
							<td style="width: 210px; text-align: right;">Send Option
								:&nbsp;&nbsp;</td>
							<td style="width: 250px;padding-left: 20px;">
								<select id="sendOptionSelect" style="width: 200px;"
								class="inputBoxBorder">
									<option value="Apex" selected="selected">SMS Vendor: Jyothi Agencies</option>
							</select>
							</td>
							<td style="width: 115px;"></td>
							<td style="width: 195px; text-align: right;"><button id="sendSMSButton"
									type="button" style="margin-right: 100px; font-weight: bold;"
									value="smsSend" onclick="sendSMS(); return false;">
									<img src="css/images/sms.ico" alt="" height=20 width=20>&nbsp;&nbsp;&nbsp;Send
								</button></td>
						</tr>
					</table>
				</div>
			</div> <!--  Send ends -->
		</div>
		<!-- Tabs end -->
	</div>
</div>
<!-- Overlay transparent screen to implement modal window -->
<div id="overlay" class="ui-widget-overlay ui-front"
	style="z-index: 100; display: none"></div>
<!--  TODO: delme -->
<div id="waterMark" class="hide-label"> SAMPLE ONLY </div>

<jsp:include page="footer.jsp" />

