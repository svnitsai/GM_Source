<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.SMSTemplateCRUDProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<!-- 
 * smsTemplates.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen helps in creating SMS templates for sending bulk email
 *              to customers.
 * 
-->
<%

	String templateName = "";
	boolean addOptionClicked = false;
	
	if (session.getAttribute("templateName") != null) {
		templateName = (String) session.getAttribute("templateName");
	}
	session.removeAttribute("templateName"); //So that page will refresh next time around
	
	//Add option has 0 in templateName
	if (templateName.equals("0")) {
		addOptionClicked=true;
	}
	
	SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();
	  
 	List result = (List) SMSTemplateInfoCrud.getAllSMSTemplateList(); //Get all templates
%>
<script type="text/javascript">
	var templateChanged = "false"; //Global variable - whether the row data has been updated
	var previousOptionSelected = "";
	var previousValueSelected = "";

	$(document).ready(function() {

		$('#breadCrumps').text("SMS >> Templates");
		$('#breadCrumps').show();
		
		// Save previously selected template name in the dropdown
		previousOptionSelected = $("#templateName").find("option:selected");
		previousValueSelected = previousOptionSelected.val();

		//Check if input elements were changed by the user	
		$(".inputElements").each(
			function() {
				var elem = $(this);

				// Save current value of element to compare later
				if (elem.data('oldVal') != "")
					elem.data('oldVal', elem.val());
				
				if (!elem.hasClass('hide-label')) { //Length of visible sms message
					$("#smsLengthLabel").text("SMS Length: " + elem.val().length); //Show SMS length as soon as loaded
				}

				// Look for changes in the value
				elem.bind(
					"propertychange change click keyup input paste",
					function(event) {
						if (elem.data('oldVal') != elem.val()) {
							templateChanged = "true";
						}
						$("#smsLengthLabel").text("SMS Length: " + elem.val().length);
				});
		});

	});
	
	//Handle adding a new template CLICK
 	function startAddingTemplate(templateActionIn) { 
		$.ajax({
			type : "POST",
			url : '/gm/web/SMSTemplateServlet',
			data : {
				templateAction : templateActionIn
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
 	
	//When CANCEL button pressed on update or new template add; following function handles it
	function cancelUpdateChanges() {
		//remove pressed state on the button
		//TODO: not working!
		$("#cancelButton").removeClass("ui-state-focus ui-state-hover");
		if (templateChanged == "true") {
			$("#cancel-message").dialog({
				modal : true,
				buttons : [ {
					id : "yesButton",
					text : "Yes",
					tabIndex : -1,
					click : function() {
						templateChanged = "false";
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
 	
	//When SAVE button pressed on ADD/update; following function handles it
	function saveUpdateChanges() {
		form_valid = "true";
		var option ="update";
		var smsTemplateMessage = "";
		var smsTemplateName = "";
		var smsTemplateId = "";

		$("#userTemplateName").removeClass("error");
		$('#templateErrorMesg').hide();
		
		//Template name validation
		if ($("#userTemplateName").is(':visible')) { //#userTemplateName visible only when adding template
			option = "add";
			if ($("#userTemplateName").val().length == 0) { //Template name cannot be empty
				$('#templateErrorMesg').text('Template name required; Please enter.'); 
				$('#templateErrorMesg').show();
				$("#userTemplateName").addClass("error");
				$("#userTemplateName").focus();
				form_valid = "false";
			}
	 	     $("#templateSelectDiv select option").each(function() { //ensure new template name is different from the one in options
	 	    	// console.log('Select option template name ' + $(this).val());
	 	    	 if ($("#userTemplateName").val().toLowerCase() == $(this).val().toLowerCase()) {
					$('#templateErrorMesg').text('Template name already exists; Please enter different one.'); 
					$('#templateErrorMesg').show();
					$("#userTemplateName").addClass("error");
					$("#userTemplateName").focus();
					form_valid = "false";
	 	    	 }
	 	     })
	 	    smsTemplateName = $("#userTemplateName").val(); //Save template name
		}
		
		$("#templateDiv textarea").each(function(){// Validation of text-area - common for both ADD & UPDATE
			$(this).removeClass("error");
			if (form_valid == "true") {
				if ($(this).is(':visible')) { //validate visible text area
					var errorMessage = "";
					var templateVariable = "";
					smsTemplateId = $(this).attr('id');
					smsTemplateId = smsTemplateId.substr(11,smsTemplateId.length);
					if (option != "add") {
						smsTemplateName = $(this).attr('name');
					}
					smsTemplateMessage = $.trim($(this).val()); // remove white spaces before and after
					$(this).val(smsTemplateMessage);
					$(this).focus();

					//console.log (" template message is " + smsTemplateMessage);
					var nextSpace = -1;
					var startPar = smsTemplateMessage.indexOf("[");
					var endPar = smsTemplateMessage.indexOf("]");
					if (endPar < startPar) { // ] found before [
					    if (endPar == -1) {
					    	errorMessage = " No matching ] found for variable inclusion; Please correct."					
					    } else {
					    	errorMessage = " Unmatched ] found for variable inclusion; Please correct."					
					    }
					} else {
						while (startPar != -1) {
							endPar = smsTemplateMessage.indexOf("]", startPar);
							nextSpace = smsTemplateMessage.indexOf(" ", startPar);
							if (nextSpace == -1) nextSpace = smsTemplateMessage.length;
							if ( (endPar == -1 ) || (nextSpace < endPar) ){ //Get the variable and validate
								errorMessage = "Incomplete variable " + smsTemplateMessage.substring(startPar + 1, nextSpace)
									+ ". Ending ] not found.  Please correct.";
								break; //break while loop
							}
							templateVariable = smsTemplateMessage.substring(startPar + 1, endPar);
							//console.log ('Variable found ' + templateVariable);
							if ((templateVariable.toLowerCase() != "contact_name") &&
								(templateVariable.toLowerCase() != "due_date") &&
								(templateVariable.toLowerCase() != "due_amount") &&
								(templateVariable.toLowerCase() != "invoice_number")) {
								//(templateVariable.toLowerCase() != "invoice_amount")) {
								//console.log ('Invalid variable found ' + templateVariable);
								errorMessage = "Invalid variable found - " + templateVariable + " ; Please correct.";
								break;
							}
							startPar = smsTemplateMessage.indexOf("[", endPar);
						}
						endPar = smsTemplateMessage.indexOf("]", endPar + 1);
						if ((endPar > -1) && (errorMessage == "")){
							errorMessage = " Matching [ not found for variable inclusion; Please correct."					
						}
						if ((smsTemplateMessage.length < 1) || (smsTemplateMessage == " ")){
							errorMessage = " Empty template message; Please correct."					
						}
					}
					if (errorMessage != "") {
						$('#templateErrorMesg').text(errorMessage); 
						$('#templateErrorMesg').show();
						$(this).addClass("error");
						form_valid = "false";
					}
					
				} //Visible if ends
			}
		})

		if (form_valid == "true") {
			//Call database update
			$.ajax({
						type : "POST",
						url : '/gm/web/SMSTemplateServlet',
						data : {
							templateAction : option,
							jsptemplateId : smsTemplateId,
							jsptemplateName : smsTemplateName,
							jsptemplateMessage : smsTemplateMessage
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

	//When DELETE button pressed; following function handles it
	function deleteTemplate() {
		var smsTemplateId = "";
		var smsTemplateName = "";
		$("#templateDiv textarea").each(function(){// Get template id of template being shown
			if ($(this).is(':visible')) { //validate visible text area
				smsTemplateId = $(this).attr('id');
				smsTemplateId = smsTemplateId.substr(11,smsTemplateId.length);
				smsTemplateName = $(this).attr('name');
			}
		})
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
										url : '/gm/web/SMSTemplateServlet',
										data : {
											templateAction : 'delete',
											jsptemplateId : smsTemplateId,
											jsptemplateName : smsTemplateName,
											jsptemplateMessage : ''
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

	//Handles when drop down selection changes
 	$(document).on('change',"#templateName",function () 
 	{
	    var optionSelected = $(this).find("option:selected");
 	    var valueSelected  = optionSelected.val();
		if (templateChanged == "true") { //if template changed, give a warning...
			$("#cancel-message").dialog({
				modal : true,
				buttons : [ {
					id : "yesButton",
					text : "Yes",
					tabIndex : -1,
					click : function() {
						templateChanged = "false";
						$(this).dialog("close");
						location.reload(true);
					}
				}, {
					id : "noButton",
					text : "No",
					click : function() {
						$(this).dialog("close");
						//Just Close & undo dropdown SELECTION
				 	     $(".templateMessage").each(function() {
				 	    	 if ($(this).attr('name') == previousValueSelected) {
				 	    		 $(this).removeClass("hide-label");
				 	    	 } else $(this).addClass("hide-label");
				 	    	$("#templateName").val(previousValueSelected);
				 	     })
					}
				} ]
			});
		} else {
	 	     $(".templateMessage").each(function() {
	 	    	 previousValueSelected = valueSelected;  //Need previous dropdown value to UNDO during CANCEL
	 	    	 if ($(this).attr('name') == valueSelected) {
	 	    		 $(this).removeClass("hide-label");
	 				//Length of visible sms message
					$("#smsLengthLabel").text("SMS Length: " + $(this).val().length);
	 	    	 } else $(this).addClass("hide-label");
	 	     })
		}
 	})

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
<div id="cancel-message" title="Warning!" class="hide-label">
	<p>
		<span class="ui-icon  ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span>
		<span style="color: red;">Changes made will be lost! Do you want to cancel?</span>
	</p>
</div>

<div id="error-message" title="Error!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Request could not be completed at this time. Please try after some time. If this continues, contact System Administrator!</span>
	</p>
</div>

<div id="update-message" title="Success!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;">Template saved to system successfully!</span>
	</p>
</div>

<div id="delete-message" title="Success!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-info " style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: green;">Template Information Successfully deleted!</span>
	</p>
</div>

<div id="delete-warning" title="Warning!" class="hide-label">
	<p>
		<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 50px 0;"></span> 
		<span style="color: red;">Template Information will be permanently deleted.  Do you want to continue deleting?</span>
	</p>
</div>

<div id="oneSMS">
	<div class="item" id="contentDiv" name="contentDiv">
		<form action="/gm/web/SMSTemplateServlet" method="post">
			<div id="templateSelectDiv">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: center; ">Choose Template to edit: &nbsp;&nbsp;&nbsp;</td>
						<td style="width:250px; text-align: center;">
							<select id="templateName" class="inputBoxBorder" style="width:180px;" <%if (addOptionClicked) { %> disabled <% } %>>
							<%if ((result != null) && (result.size() > 0)) { 
							  int templateLoop = 0;
							  for (Object object : result) { 
								  templateLoop ++;
								  Map row = (Map) object; 
							%>
								<option value="<%=row.get("TemplateName")%>"  <%if (templateLoop==1) {%> selected <%}%>> <%=row.get("TemplateName")%></option>
							<%
								  } 
							  }
							 %>
							</select>
						</td>
						<td style="width: 420px; text-align: right;">
							<button type="submit" class="addButton" name="templateAction"
									value="templateAdd" onclick="startAddingTemplate('templateAdd'); return false;" <%if (addOptionClicked) { %> disabled <% } %>>&nbsp;
							</button>&nbsp;&nbsp;&nbsp;Create New Template</td>
					</tr>
				</table>
			</div>

		<!--  Template Text box - begins -->
			<div id="templateDiv" style="height: 275px;">
				<div id="templateNameDiv" style="width: 100%;"  <%if (!addOptionClicked) { %> class="hide-label" <% } %>>
					<table style="border-collapse: collapse; height: 50px;">
						<tr>
							<td style="width:250px; text-align: center; ">Template Name <sup>*</sup>: &nbsp;&nbsp;&nbsp;</td>
							<td style="width:200px; text-align: center; "> 
								<input id="userTemplateName" type="text" placeholder="Template Name" maxlength="50" class="inputBoxBorder" 
								size="25" style="height: 20px; margin-left: 30px;"> 
							</td>
						</tr>
					</table>
				</div>
				<div id="errorDiv" style="width:100%; padding-top: 15px;">
					<label id="templateErrorMesg" class="hide-label err-mesg-font">Invalid variable used in template.</label>
				</div>
				<div style="width: 650px; float: left;">
					<%if (addOptionClicked) { %>
						<textarea id="templateId_0" name="NewTemplateName" maxlength="160" placeholder="Please enter new template content here.  Instructions on the right side box."
						style="size: 500px; width: 550px; height: 150px; resize: none; margin: 5px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;"
						class="inputElements inputBoxBorder"></textarea>
					<% } else { %>
						<%if ((result != null) && (result.size() > 0)) { 
						  int templateLoop = 0;
						  for (Object object : result) { 
							  templateLoop ++;
							  Map row = (Map) object; 
							  String templatesmsMessage = row.get("Template").toString().trim();
						%>
							<textarea id="templateId_<%=row.get("TemplateId")%>" name="<%=row.get("TemplateName")%>" maxlength="160" placeholder="Please enter new template content here.  Instructions on the right side box."
							style="size: 500px; width: 550px; height: 150px; resize: none; margin: 5px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;"
							<% if (templateLoop != 1) {%> class="inputElements inputBoxBorder templateMessage hide-label" <% } else { %> class="inputElements inputBoxBorder templateMessage" <% } %> 
							><%=templatesmsMessage%></textarea>
					<%
							  } 
						  }
					 }
					 %>
					 <label id="smsLengthLabel" style="font-size: 9px; margin-left: 30px; font-style: italic;">SMS Length: 123</label>
				</div>
				<div style="width: 250px; float: left;">
				  <table id="variableList" style="size: 500px; width: 228px; height: 172px; margin-left: 30px; margin-top: 5px; background-color: rgba(241, 245, 235, 0.61); border-radius: 5px;">
					<thead style="height: 20px;">
						<tr> <td style="text-align: center; font-weight: bold;">Available Variables</td></tr>
					</thead>
					<tbody>
						<tr><td class="paddedColumn">contact_name</td></tr>
						<tr><td class="paddedColumn">due_date</td></tr>
						<tr><td class="paddedColumn">due_amount</td></tr>
						<tr><td class="paddedColumn">invoice_number</td></tr>
						<tr><td style="height: 8px; font-style: italic; color: rgb(201, 195, 195); font-size: 11px;">Use above variables to construct SMS templates.  Maximum of 160 chars allowed.</td></tr>
					</tbody>  
				  </table>
				</div>
			</div>
			<div id="saveCancel" style="margin-left: 250px;">
				<button id="saveButton" type="submit" class="saveButton" name="templateAction" style="margin-right: 50px;"
				 				value="templateSave" onclick="saveUpdateChanges(); return false;"
				>&nbsp;&nbsp;Save</button> 
				&nbsp;&nbsp;
				<button id="cancelButton" type="submit" class="cancelButton" name="templateAction" style="margin-right: 60px;"
				 				value="templateCancel" onclick="cancelUpdateChanges(); return false;"
				 >&nbsp;&nbsp;Cancel</button> 
				<button id="deleteButton" type="submit" class="deleteButton" name="templateAction" 
				 				value="templateDelete" onclick="deleteTemplate(); return false;" <%if (addOptionClicked) { %>disabled<% } %>
				 >&nbsp;&nbsp;Delete</button> 
			</div>
		</form>
		
	</div>
	<!-- Overlay transparent screen to implement modal window -->
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
	
</div>
<!--  TODO: delme -->
<div id="waterMark" class="hide-label"> SAMPLE ONLY </div>

<jsp:include page="footer.jsp" />