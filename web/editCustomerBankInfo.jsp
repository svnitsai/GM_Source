<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.svnitsai.gm.Util"%>
<%@ page import="com.svnitsai.gm.database.generated.*"%>
<%@ page import="com.svnitsai.gm.util.date.DateUtil"%>
<%@ page import="com.svnitsai.gm.database.provider.CustomerBankInfoCRUDProvider"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<!-- 
 * editCustomerBankInfo.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen to do CRUD operation on Customer Bank details;
 *              Invoked from Customer Screen
 * 
-->
<jsp:include page="header.jsp?hideHeader=all" />

<%
	//customerBankInfoId is passed from CustomerBankInfoServlet
	String customerBankInfoId = "";
	boolean addOptionClicked = false;
	boolean rowBeingEdited = false;
	
	if (session.getAttribute("customerBankInfoId") != null) {
		customerBankInfoId = (String) session.getAttribute("customerBankInfoId");
		if (customerBankInfoId.length() > 0) {
			rowBeingEdited = true;
		}
	}
	System.out.println (" JSP - customerBankInfoId " + customerBankInfoId);
	session.removeAttribute("customerBankInfoId"); //So that page will refresh next time around

	//Add option has 0 in customerBankInfoId
	if (customerBankInfoId.equals("0")) {
		addOptionClicked=true;
	}
	System.out.println (" JSP - addOptionClicked " + addOptionClicked);
	
	//Get CustId from request - first time around; in Save it session for future use
	String custIdFromCustomerScreen = (String) request.getParameter("custId");
	if (custIdFromCustomerScreen == null) {
		custIdFromCustomerScreen =  (String) session.getAttribute("custIdFromCustomerScreen");
	} else {
		session.setAttribute("custIdFromCustomerScreen", custIdFromCustomerScreen);
	}
	
	String custNameFromCustomerScreen = (String) request.getParameter("custName");
	if (custNameFromCustomerScreen == null) {
		custNameFromCustomerScreen =  (String) session.getAttribute("custNameFromCustomerScreen");
	} else {
		session.setAttribute("custNameFromCustomerScreen", custNameFromCustomerScreen);
	}

	String custCityFromCustomerScreen = (String) request.getParameter("custCity");
	if (custCityFromCustomerScreen == null) {
		custCityFromCustomerScreen =  (String) session.getAttribute("custCityFromCustomerScreen");
	} else {
		session.setAttribute("custCityFromCustomerScreen", custCityFromCustomerScreen);
	}

	String custProcTypeFromCustomerScreen = (String) request.getParameter("custProcType");
	if (custProcTypeFromCustomerScreen == null) {
		custProcTypeFromCustomerScreen =  (String) session.getAttribute("custProcTypeFromCustomerScreen");
	} else {
		session.setAttribute("custProcTypeFromCustomerScreen", custProcTypeFromCustomerScreen);
	}
	
	//TODO: Delete me
	System.out.println (" from edit cust bank jsp; id : " + custIdFromCustomerScreen  + "name: " + custNameFromCustomerScreen + " city: " + custCityFromCustomerScreen
	 + " Customer Type:  " + custProcTypeFromCustomerScreen);

	//Get Customer Banks for a given customer 
     CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
     List result = (List) customerBankInfoCrud.getCustomerBankList(Util.convertToLong(custIdFromCustomerScreen));
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
			$('#scrollCustomerBankInfoTable').dataTable({
				"scrollY" : "250px", //Height of the table
				"scrollCollapse" : true,
				"paging" : false
			});
			
			//Check if input elements were changed by the user	
			$(".inputElements").each(
				function() {
					var elem = $(this);

					// Save current value of element to compare later
					if (elem.data('oldVal') != "")
						elem.data('oldVal', elem.val());

					// Look for changes in the value
					elem.bind(
						"propertychange change click keyup input paste",
						function(event) {
							if (elem.data('oldVal') != elem.val()) {
								rowChanged = "true";
							}
					});
			});
			
			// remove sorting for ICON columns in DATATABLE
			$(".nosorting").each(function() {
				$(this).removeClass("sorting");
			})
	});
	//End of document ready

	//When SAVE button pressed on update; following function handles it
	function saveUpdateChanges(currentDispKey) {
		form_valid = "true";
		if ($("#custBankName").val().length == 0) {
			$("#custBankName-mesg").show();
			form_valid = "false";
			$("#custBankName").addClass("error")
		} else {
			$("#custBankName-mesg").hide();
			$("#custBankName").removeClass("error")
		}

		if (form_valid == "true") {
			//Call database update
			$.ajax({
						type : "POST",
						url : '/gm/web/CustomerBankInfoServlet',
						data : {
							rowAction : 'update',
							jspcustBankBranch : $("#custBankBranch").val(),
							jspcustAccountName : $("#custAccountName").val(),
							jspcustAccountType : $("#custAccountType").val(),
							jspcustAccountNumber : $("#custAccountNumber").val(),
							jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
							jspcustBankId : $("#custBankId").val(),
							jspCustBankName : $("#custBankName").val()
						},
						success : function(returnData) {
							var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
							if (returnCode == '0') { //Successful return
								$("#update-message").dialog({
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

		} else {
			//remove pressed state on the button
			$("#saveButton").removeClass("ui-state-focus ui-state-hover");
		}
	}

	//When CANCEL button pressed on update; following function handles it
	function cancelUpdateChanges(currentDispKey) {
		//remove pressed state on the button
		//TODO: not working!
		$("#cancelButton").removeClass("ui-state-focus ui-state-hover");
		if (rowChanged == "true") {
			$("#cancel-message").dialog({
				modal : true,
				buttons : [ {
					id : "yesButton",
					text : "Yes",
					tabIndex : -1,
					click : function() {
						rowChanged = "false";
						$(this).dialog("close");
						//Reload page after cancel
						location.reload(true);
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
		} else {
			//Reload page 
			location.reload(true);
		}
	}

	//When SAVE button pressed on ADD; following function handles it
	function saveAddChanges(currentDispKey) {
		form_valid = "true";
		if ($("#custBankName").val().length == 0) {
			$("#custBankName-mesg").show();
			form_valid = "false";
			$("#custBankName").addClass("error")
		} else {
			$("#custBankName-mesg").hide();
			$("#custBankName").removeClass("error")
		}

		if (form_valid == "true") {
			//Call database update
			$.ajax({
						type : "POST",
						url : '/gm/web/CustomerBankInfoServlet',
						data : {
							rowAction : 'add',
							jspcustBankBranch : $("#custBankBranch").val(),
							jspcustAccountName : $("#custAccountName").val(),
							jspcustAccountType : $("#custAccountType").val(),
							jspcustAccountNumber : $("#custAccountNumber").val(),
							jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
							jspCustBankName : $("#custBankName").val()
						},
						success : function(returnData) {
							var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
							if (returnCode == '0') { //Successful return
								$("#add-message").dialog({
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
					});

		} else {
			//remove pressed state on the button
			$("#addSaveButton").removeClass("ui-state-focus ui-state-hover");
		}
	}

	//When CANCEL button pressed on ADD; following function handles it
	function cancelAddChanges(currentDispKey) {
		$("#addCancelButton").removeClass("ui-state-focus ui-state-hover");

		if (rowChanged == "true") {
			$("#cancel-message").dialog(
				{
					modal : true,
					buttons : [
							{
								id : "yesButton",
								text : "Yes",
								tabIndex : -1,
								click : function() {
									rowChanged = "false";
									$(this).dialog("close");
									//Reload Page
									location.reload(true);
								}
							},
							{
								id : "noButton",
								text : "No",
								click : function() {
									//remove pressed state on the button
									$("#addCancelButton").removeClass("ui-state-focus ui-state-hover");
									$(this).dialog("close");
									//Just Close
								}
							} ]
				});
		} else {
			//Reload page
			location.reload(true);
		}
	}

	//When DELETE button pressed; following function handles it
	function deleteChanges(currentDispKey) {
		$("#delete-warning").dialog(
			{
				modal : true,
				buttons : [
						{
							id : "yesButton",
							text : "Yes",
							tabIndex : -1,
							click : function() {
								rowChanged = "false";
								$.ajax({
										type : "POST",
										url : '/gm/web/CustomerBankInfoServlet',
										data : {
											rowAction : 'delete',
											jspcustIdFromCustomerScreen : $("#custIdFromCustomerScreen").val(),
											jspcustBankId : $("#custBankId").val()
										},
										success : function(returnData) {
											var returnCode = returnData.charAt(11); //servlet data "ReturnCode_" + returnCode
											if (returnCode == '0') { //Successful return
												$("#delete-message").dialog({
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
						},
						{
							id : "noButton",
							text : "No",
							click : function() {
								//remove pressed state on the button
								$("#deleteButton").removeClass("ui-state-focus ui-state-hover");
								//Just Close
								$(this).dialog("close");
							}
						} ]
			});
	}

// 	function OnClose()
// 	{
// 	    if(window.opener != null && !window.opener.closed) 
// 	    {
// 	    	window.opener.location.reload(false);
// 	       window.opener.hideModalDiv();
// 	    }
// 	}
// 	window.onunload = OnClose;
	
</script>

<div id="cancel-message" title="Warning!" class="hide-label">
	<p>
		<span class="ui-icon  ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
		<span style="color: red;">Changes made will be lost! Do you want to cancel?</span>
	</p>
</div>

<div id="add-message" title="Success!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;">Bank Information Successfully added!</span>
	</p>
</div>

<div id="update-message" title="Success!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info " style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;">Bank Information Successfully updated!</span>
	</p>
</div>

<div id="delete-message" title="Success!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info " style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;">Bank Information Successfully deleted!</span>
	</p>
</div>

<div id="delete-warning" title="Warning!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Bank Information will be permanently deleted.  Do you want to continue deleting?</span>
	</p>
</div>

<div id="error-message" title="Error!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Request could not be completed at this time. Please try after some time. If this continues, contact System Administrator!</span>
	</p>
</div>

<div id="one">
	<!--  contentDiv - begin -->
	<div class="item" id="contentDiv" name="contentDiv">
		<!-- Top row customer info - begin -->
		<!-- 1. If row being edited or added, disable all other buttons except the ones on the row being changed -->
		<form action="/gm/web/CustomerBankInfoServlet" method="post">
			<div>
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width: 250px; text-align: center;"><h2>Bank
								information for: &nbsp;&nbsp;&nbsp;</h2></td>
						<td style="width: 450px; text-align: left;">
							<h2><%=custNameFromCustomerScreen %>,&nbsp<%=custCityFromCustomerScreen %></h2>
						</td>
						<%
						if (rowBeingEdited) {
						%>
							<td style="width: 220px; text-align: right;">
							<button type="submit" class="addButton" name="rowAction" value="customerBankInfoAdd" disabled>&nbsp;
							</button>&nbsp;&nbsp;&nbsp;Add Bank Information</td>
						<%
						} else {
						%>
							<td style="width: 220px; text-align: right;">
							<button type="submit" class="addButton" name="rowAction"
									value="customerBankInfoAdd">&nbsp;
							</button>&nbsp;&nbsp;&nbsp;Add Bank Information</td>
						<%
						}
						%>
					</tr>
				</table>
			</div>
			
			<!-- Top row customer info - end -->
			<input type="hidden" id="custIdFromCustomerScreen" value="<%=custIdFromCustomerScreen%>">
			<!-- Tabular bank info - begin -->
			<table id="scrollCustomerBankInfoTable" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>Bank Name&nbsp;<span title="Required"><sup>*</sup></span></th>
						<th>Bank Branch</th>
						<th>Account Name</th>
						<th>Account Type</th>
						<th>Account Number</th>
						<th class="nosorting">&nbsp;&nbsp;&nbsp;</th>
						<th class="nosorting">&nbsp;&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (addOptionClicked) {
					%>
					<!-- Handle addOption here -->
					<tr>
						<td align="left"><input type="text" name="jspcustBankName"
							placeholder="Bank Name" maxlength="50" id="custBankName"
							class="inputElements"> <label id="custBankName-mesg"
							class="hide-label err-mesg-font">Bank Name Required</label></td>
						<td align="left"><input type="text" name="jspcustBankBranch"
							placeholder="Bank Branch" maxlength="50" class="inputElements"
							id="custBankBranch"></td>
						<td align="left"><input type="text" name="jspcustAccountName"
							placeholder="Account Name" maxlength="30" class="inputElements"
							id="custAccountName"></td>
						<td align="left"><select name="jspcustAccountType"
							class="inputElements" id="custAccountType">
								<option value="Savings">Savings</option>
								<option value="Current">Current</option>
								<option value="CC">CC</option>
						</select></td>
						<td align="left"><input type="text"
							name="jspcustAccountNumber" placeholder="Account Number"
							maxlength="50" class="inputElements" id="custAccountNumber"></td>
						<td style="width: 30px" align="center" title="Cancel Bank Information">
							<button type="submit" class="cancelButton" name="rowAction"
								value="customerBankInfoAddCancel_0" onclick="cancelAddChanges('0'); return false;"
								id="addCancelButton">&nbsp;</button></td>
						<td style="width: 30px" align="center" title="Save Bank Information">
							<input type="hidden" name="jspCustomerBankId" value="0">
							<button type="submit" class="saveButton" name="rowAction"
								value="customerBankInfoAddSave_0" onclick="saveAddChanges('0'); return false;" 
								id="addSaveButton">&nbsp;</button></td>
					</tr>
					<%
					} // Add option ends
					%>
					<%	
					  if ((result != null) && (result.size() > 0)) { 

						for (Object object : result) {
							Map row = (Map) object;
							String dispKey = row.get("CustBankId").toString();
							String dispBankName = row.get("CustBank").toString();
							String dispBankBranch = row.get("CustBankBranch").toString();
							String dispAccountName = row.get("CustBankAccountName").toString();
							String dispAccountType = row.get("CustBankAccountType").toString();
							String dispAccountNumber = row.get("CustBankAccountNumber").toString();

							if (!customerBankInfoId.isEmpty() && customerBankInfoId.equalsIgnoreCase(dispKey)
									&& !addOptionClicked) {
					%>
					<!-- Fill the table with existing bank information.  If edit is clicked on that row, open it up for edit -->
							<tr>
								<td align="left">
									<input type="text" name="jspcustBankName" value="<%=dispBankName%>" class="inputElements" maxlength="50" id="custBankName"> 
									<label id="custBankName-mesg" class="hide-label err-mesg-font">Bank Name Required</label>
									<input type="hidden" id="custBankId" value="<%=dispKey%>">
								</td>
								<td align="left"><input type="text" name="jspcustBankBranch"
									value="<%=dispBankBranch%>" class="inputElements" maxlength="50" id="custBankBranch">
								</td>
								<td align="left"><input type="text" name="jspcustAccountName"
									value="<%=dispAccountName%>" class="inputElements" maxlength="30" id="custAccountName">
								</td>
								<td align="left"><select name="jspcustAccountType" class="inputElements" id="custAccountType">
									<option value="Savings"  <%if (dispAccountType.equalsIgnoreCase("Savings")) { %>selected="selected" <% } %>>Savings</option>
									<option value="Current" <%if (dispAccountType.equalsIgnoreCase("Current")) { %>selected="selected" <% } %>>Current</option>
									<option value="CC" <%if (dispAccountType.equalsIgnoreCase("CC")) { %>selected="selected" <% } %>>CC</option>
									</select>
								</td>
								<td align="left">
									<input type="text" name="jspcustAccountNumber" value="<%=dispAccountNumber%>"
									class="inputElements" maxlength="50" id="custAccountNumber">
								</td>
								<td style="width: 30px" align="center" title="Cancel Update">
									<button type="submit" class="cancelButton" name="rowAction" value="customerBankInfoCancel_<%=dispKey%>"
										onclick="cancelUpdateChanges('<%=dispKey%>'); return false;"
										id="cancelButton">&nbsp;</button>
								</td>
								<td style="width: 30px" align="center" title="Save Bank Information">
									<button type="submit" class="saveButton" name="rowAction"
										value="customerBankInfoSave_<%=dispKey%>"
										onclick="saveUpdateChanges('<%=dispKey%>'); return false;"
										id="saveButton">&nbsp;</button>
								</td>
							</tr>
							<%
							} 
							else //Else to display database values as is 
							{
							%>
							<tr>
								<td align="left" class="inputElements" style="word-break: break-all;"><%=dispBankName%>
									<input type="hidden" id="custBankId" value="<%=dispKey%>">
								</td>
								<td align="left" style="word-break: break-all;"><%=dispBankBranch%></td>
								<td align="left" style="word-break: break-all;"><%=dispAccountName%></td>
								<td align="left"><%=dispAccountType%></td>
								<td align="left" style="word-break: break-all;"><%=dispAccountNumber%></td>
								<!--  if rowbeing edited - disable other buttons -->
								<%
								if (rowBeingEdited) {
								%>
								<td style="width: 30px" align="center" title="Delete Bank Information">
									<button type="submit" class="deleteButton" name="rowAction"
										value="customerBankInfoDelete_<%=dispKey%>" disabled>&nbsp;</button>
								</td>
								<td style="width: 30px" align="center" title="Edit Bank Information">
									<button type="submit" class="editButtonNolabel" name="rowAction"
										value="customerBankInfoUpdate_<%=dispKey%>" disabled>&nbsp;</button>
								</td>
								<%
								} 
								else //else of rowbeingedited 
								{
								%>
								<td style="width: 30px" align="center" title="Delete Bank Information">
									<button type="submit" class="deleteButton" name="rowAction"
										value="customerBankInfoDelete_<%=dispKey%>" onclick="deleteChanges('<%=dispKey%>' ); return false;" id="deleteButton"
										<%if (custProcTypeFromCustomerScreen.equalsIgnoreCase("supplier")) { %> disabled <% } %>>&nbsp;</button>
								</td>
								<td style="width: 30px" align="center" title="Edit Bank Information">
									<button type="submit" class="editButtonNolabel" name="rowAction"
										value="customerBankInfoUpdate_<%=dispKey%>">&nbsp;</button>
								</td>
								<%
								}
								%>
							</tr>
					<%
							} //else of not null
						} //for loop
					} //if has records
					%>
				</tbody>
			</table>
		</form>
	</div>
	<!--  contentDiv - end -->
</div>
</body>
</html>