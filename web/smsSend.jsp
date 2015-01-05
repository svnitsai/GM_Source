<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.CustomerInfoProvider"%>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
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
//Default (on first entry), screen will show customerType of SUPPLIER
//Second time around, customerServlet will pass radio button clicked in session variable
String customerTypeProc = "supplier";
if (session.getAttribute("customerTypeProc") != null) {
	customerTypeProc = (String) session.getAttribute("customerTypeProc");
}

CustomerInfoProvider customerInfoProvider = new CustomerInfoProvider();
/* Get Customer Information for Customer aka merchants */
List customerInfoList = (List) customerInfoProvider.getCustomerInfoList(customerTypeProc);

%>
<script type="text/javascript">
	$(document).ready(
			function() {

				$('#breadCrumps').text("SMS >> Send");
				$('#breadCrumps').show();

				$('#scrollSMSRecipientTable').dataTable({
					"order" : [ [ 4, "asc" ] ],
					"scrollY" : "120px", //Height of the table
					"scrollCollapse" : true,
					"paging" : true,
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
					} ]
				});

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
					$("#tabs").tabs();
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
						});

				$("#Message #customerCriteria").addClass("inputBoxBorder");
				$('#messageBox').attr("disabled", "disabled");
				$('#selectedMessageBox').text($('#messageBox').text());

				// 		 $(document).on('change',"#customerCriteria",function () {
				// 		    var optionSelected = $(this).find("option:selected");
				// 		    var valueSelected  = optionSelected.val();
				// 		    var textSelected   = optionSelected.text();
				// 			if (valueSelected != 'Individual') {
				// 				$('#messageBox').attr("disabled","disabled");
				// 			}
				// 		});

			});

	$(document)
			.on(
					'change',
					"#customerCriteria",
					function() {
						var optionSelected = $(this).find("option:selected");
						var valueSelected = optionSelected.val();
						var textSelected = optionSelected.text();
						if (valueSelected != 'Individual') {
							$('#messageBox').prop('disabled', true);
							$('#messageBox')
									.text(
											'Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment which was due on [due_date] is not settled.  Pls. settle.');
						} else {
							$('#messageBox').prop('disabled', false);
							$('#messageBox').text('');
						}
						$('#selectedMessageBox').text($('#messageBox').text());
					});

	var popUpObj;
	function addCustBank(custId, custName, custCity, custProcType) {
		popUpObj = window
				.open(
						"/gm/web/editCustomerBankInfo.jsp?custId=" + custId
								+ "&custName=" + custName + "&custCity="
								+ custCity + "&custProcType=" + custProcType,
						"Edit Customer Bank Details",
						"toolbar=no, scrollbars=no,location=no,statusbar=no,menubar=no,status=no,resizable=no,width=950,height=500,left=350,top=150");
		popUpObj.focus();
	}

	function loadModalDiv() {
		var bcgDiv = document.getElementById("overlay");
		bcgDiv.style.display = "block";
	}

	function hideModalDiv() {
		var bcgDiv = document.getElementById("overlay");
		bcgDiv.style.display = "none";
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
							id="customerCriteria" class="inputCustomerCriteria"
							style="width: 180px;" class="inputBoxBorder">
								<option value="Past-due" selected="selected">Past Due</option>
								<option value="Immediate-Due">Immediate Due</option>
								<option value="All">All</option>
						</select></td>
						<td style="width: 250px; text-align: right;"><input
							type="radio" name="filterSelection" id="filterSelection"
							value="includeAll" title="Send SMS to all">&nbsp;&nbsp;&nbsp;Include
							All</td>
						<td style="width: 220px; text-align: center;"><input
							type="radio" name="filterSelection" id="filterSelection"
							value="clearAll" title="Clear All Selection">&nbsp;&nbsp;&nbsp;Clear
							All</td>
					</tr>
				</table>
				<table id="scrollSMSRecipientTable" class="display" cellspacing="0"
					width="100%">
					<thead>
						<tr>
							<th style="width: 62px">Include</th>
							<th style="width: 155px">Customer Name</th>
							<th style="width: 122px">Contact Name</th>
							<th style="width: 122px">Contact Number</th>
							<th style="width: 79px">Due Date</th>
							<th>Sortable Due Date</th>
							<th style="width: 81px">Due Amount</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 200px" align="left">Sridevi Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 12345</td>
							<td style="width: 110px" align="left">14 Dec. 2014</td>
							<td>20141214</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 160px" align="left">Sridevi Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 12345</td>
							<td style="width: 110px" align="left">21 Dec. 2014</td>
							<td>20141221</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 160px" align="left">Sridevi Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 12345</td>
							<td style="width: 110px" align="left">28 Dec. 2014</td>
							<td>20141228</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 160px" align="left">Kumaran Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 54321</td>
							<td style="width: 110px" align="left">11 Dec. 2014</td>
							<td>20141211</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 160px" align="left">Kumaran Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 54321</td>
							<td style="width: 110px" align="left">18 Dec. 2014</td>
							<td>20141218</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
						<tr>
							<td style="width: 70px"><input type="checkbox"
								class="includeQualified" name="checkbox" value="value"
								style="margin-left: 25px;"></td>
							<td style="width: 160px" align="left">Kumaran Textiles</td>
							<td style="width: 160px" align="left">Rajesh</td>
							<td style="width: 160px" align="left">94428 54321</td>
							<td style="width: 110px" align="left">25 Dec. 2014</td>
							<td>20141225</td>
							<td style="width: 100px" align="left"><span
								style="font-family: DejaVu Sans;">&#x20b9; </span>1,00,000</td>
						</tr>
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
								<option value="Past-due" selected="selected">Template:
									Past Due</option>
								<option value="Immediate-Due">Template: Immediate Due</option>
								<option value="Individual">Individual SMS</option>
						</select></td>
						<td style="width: 250px; text-align: right;"></td>
						<td style="width: 220px; text-align: center;"></td>
					</tr>
				</table>
				<textarea id="messageBox" class="inputBoxBorder" name="smsMessage"
					maxlength="160"
					placeholder="Please enter your individual SMS message here.  Please do not use any variables. Max 160 chars."
					style="size: 500px; width: 550px; height: 150px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.5; padding-top: 20px; padding-left: 20px; padding-right: 20px;">Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment which was due on [due_date] is not settled.  Pls. settle.</textarea>
			</div>
			<!-- Message tab ends -->

			<div id="Send" style="height: 335px;">
				<div id="leftSend" style="width: 200px; float: left;">
					<table id="titleTable" class="display" cellspacing="0">
						<tbody>
							<tr>
								<td
									style="height: 175px; padding-left: 70px; font-weight: bold;">Recipient(s)
									:</td>
							</tr>
							<tr>
								<td
									style="padding-left: 70px; height: 140px; font-weight: bold;">Message
									:</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div id="rightSend" style="width: 600px; float: left;">
					<table id="scrollSMSSelectedRecipientTable" class="display"
						cellspacing="0">
						<thead>
							<tr>
								<th style="width: 155px">Customer Name</th>
								<th style="width: 122px">Contact Name</th>
								<th style="width: 122px">Contact Number</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td style="width: 200px" align="left">Sridevi Textiles</td>
								<td style="width: 160px" align="left">Rajesh</td>
								<td style="width: 160px" align="left">94428 12345</td>
							</tr>
							<tr>
								<td style="width: 200px" align="left">Kumaran Textiles</td>
								<td style="width: 160px" align="left">Murugan</td>
								<td style="width: 160px" align="left">94428 54321</td>
							</tr>
							<tr>
								<td style="width: 200px" align="left">Sri Textiles</td>
								<td style="width: 160px" align="left">Devan</td>
								<td style="width: 160px" align="left">91234 54321</td>
							</tr>
							<tr>
								<td style="width: 200px" align="left">Rajesh Textiles</td>
								<td style="width: 160px" align="left">Ravi</td>
								<td style="width: 160px" align="left">94321 12345</td>
							</tr>
						</tbody>
					</table>
					<textarea id="selectedMessageBox" class="inputBoxBorder"
						name="selectedSMSMessage"
						style="size: 500px; width: 520px; resize: none; margin: 25px 30px; text-align: left; line-height: 2.5; padding-left: 20px; padding-right: 20px;"
						disabled></textarea>
				</div>
				<div id="sendOption"
					style="width: 100%; position: absolute; margin-top: 310px; font-weight: bold;">
					<table id="sendActionTable"
						style="border-collapse: collapse; height: 35px;">
						<tr style="background-color: rgba(241, 245, 235, 0.61);">
							<td style="width: 175px; text-align: right;">Send Option
								:&nbsp;&nbsp;</td>
							<td style="width: 250px; text-align: center; padding-left: 20px;">
								<select id="sendOption" style="width: 180px;"
								class="inputBoxBorder">
									<option value="SMSVender1" selected="selected">SMS
										Vender 1</option>
									<option value="SMSVender2">SMS Vender 2</option>
							</select>
							</td>
							<td style="width: 150px;"></td>
							<td style="width: 195px; text-align: right;"><button
									type="button" style="margin-right: 100px; font-weight: bold;">
									<img src="css/images/sms.ico" alt="" height=20 width=20>&nbsp;&nbsp;&nbsp;Send
								</button></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!-- Tabs end -->
	</div>
</div>
<!-- Overlay transparent screen to implement modal window -->
<div id="overlay" class="ui-widget-overlay ui-front"
	style="z-index: 100; display: none"></div>
<!--  TODO: delme -->
<div id="waterMark"> SAMPLE ONLY </div>

<jsp:include page="footer.jsp" />

