<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.CustomerInfoProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<!-- 
 * smsTemplates.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen helps in creating SMS templates for sending bulk email
 *              to customer.
 * 
-->
<%

	//customerBankInfoId is passed from CustomerBankInfoServlet
	String templateName = "";
	boolean addOptionClicked = false;
	boolean templateBeingEdited = false;
	
	if (session.getAttribute("templateName") != null) {
		templateName = (String) session.getAttribute("templateName");
		if (templateName.length() > 0) {
			templateBeingEdited = true;
		}
	}
	//System.out.println (" JSP - customerBankInfoId " + customerBankInfoId);
	session.removeAttribute("templateName"); //So that page will refresh next time around
	
	//Add option has 0 in customerBankInfoId
	if (templateName.equals("0")) {
		addOptionClicked=true;
	}
%>
<script type="text/javascript">
	$(document).ready(function() {

		$('#breadCrumps').text("SMS >> Templates");
		$('#breadCrumps').show();

		$('#scrollCustomerInfoTable').dataTable({
			"scrollY" : "250px", //Height of the table
			"scrollCollapse" : true,
			"paging" : true
		});
		
		$(".nosorting").each(function() {
			$(this).removeClass("sorting");
		})

	});
	
// 	$("#templateNameDiv").hide();
 	function startAddingTemplate(templateActionIn) {
// 		$("#templateNameDiv").show();
// 		$("#templateSelectDiv").attr('disabled','disabled');

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
		<div id="templateSelectDiv">
			<form action="/gm/web/SMSTemplateServlet" method="post">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: center; ">Choose Template to edit: &nbsp;&nbsp;&nbsp;</td>
						<td style="width:250px; text-align: center;">
							<select id="templateName" class="inputBoxBorder" style="width:180px;" <%if (addOptionClicked) { %> disabled <% } %>>
								<option value="Past-due" >Past Due</option>
								<option value="Immediate-Due">Immediate Due</option>
						</select></td>
						<td style="width: 420px; text-align: right;">
							<button type="submit" class="addButton" name="templateAction"
									value="templateAdd" onclick="startAddingTemplate('templateAdd'); return false;" <%if (addOptionClicked) { %> disabled <% } %>>&nbsp;
							</button>&nbsp;&nbsp;&nbsp;Create New Template</td>
					</tr>
				</table>
			</form>
		</div>

		<!--  Template Text box - begins -->
		<div id="templateDiv" style="height: 275px;">
				<div id="templateNameDiv" style="width: 100%;"  <%if (!addOptionClicked) { %> class="hide-label" <% } %>>
					<table style="border-collapse: collapse; height: 50px;">
						<tr>
							<td style="width:250px; text-align: center; ">Template Name <sup>*</sup>: &nbsp;&nbsp;&nbsp;</td>
							<td style="width:200px; text-align: center; "> 
								<input type="text" placeholder="Template Name" maxlength="50" class="inputBoxBorder" style="height: 20px;"> 
							</td>
						</tr>
					</table>
				</div>
				<div style="width: 650px; float: left;">
					<textarea class="inputBoxBorder" name="smsMessage" 
					style="size: 500px; width: 550px; height: 150px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;">Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment which was due on [due_date] is not settled.  Pls. settle.</textarea>
				</div>
				<div style="width: 250px; float: left;">
				  <table id="variableList" style="size: 500px; width: 228px; height: 172px; margin-left: 30px; margin-top: 25px; background-color: rgba(241, 245, 235, 0.61); border-radius: 5px;">
					<thead style="height: 20px;">
						<tr> <td style="text-align: center; font-weight: bold;">Available Variables</td></tr>
					</thead>
					<tbody>
						<tr><td class="paddedColumn">contact_name</td></tr>
						<tr><td class="paddedColumn">due_date</td></tr>
						<tr><td class="paddedColumn">due_amount</td></tr>
						<tr><td class="paddedColumn">invoice_amount</td></tr>
						<tr><td style="height: 8px; font-style: italic; color: rgb(201, 195, 195); font-size: 11px;">Use above variables to construct SMS templates.  Maximum of 160 chars allowed.</td></tr>
					</tbody>  
				  </table>
<!-- 					<textarea class="borderLessBox" name="smsVariables"  -->
<!-- 					style="size: 500px; width: 220px; height: 200px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.0;"> -->
<!-- 					<span> Available Variable List </span>contact_name due_amount invoice_amount due_date invoice_number</textarea> -->
				</div>
			</div>
		<div id="saveCancel" style="margin-left: 250px;">
			<button type="button" class="saveButton" style="margin-right: 50px;">&nbsp;&nbsp;Save</button> 
			&nbsp;&nbsp;
			<button type="button" class="cancelButton">&nbsp;&nbsp;Cancel</button> 
		</div>
	</div>
	<!-- Overlay transparent screen to implement modal window -->
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
	
</div>
<!--  TODO: delme -->
<div id="waterMark"> SAMPLE ONLY </div>

<jsp:include page="footer.jsp" />