<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.CustomerInfoProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<!-- 
 * smsHistory.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen lists SMS history for a given date range.
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
	$(document).ready(function() {

		$('#breadCrumps').text("SMS >> History");
		$('#breadCrumps').show();
		
		$('#scrollSMSHistoryTable').dataTable({
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
		
		$(".nosorting").each(function() {
			$(this).removeClass("sorting");
		})

	});

	var popUpObj;
	function addCustBank(custId, custName, custCity, custProcType) 
	{
		popUpObj=window.open("/gm/web/editCustomerBankInfo.jsp?custId=" + custId + "&custName=" + custName + "&custCity=" + custCity + "&custProcType=" + custProcType,
		    "Edit Customer Bank Details",
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
		<!-- Customer Filter by Supplier / Merchant -->
		<div>
			<form action="/gm/web/smsHistoryServlet" method="post">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: right; ">SMS History date from&nbsp;&nbsp;</td>
						<td style="width:150px; text-align: left;">
							<input type="text" name="selectedFromDate" id="selectedFromDate" class="datepicker inputBoxBorder" value="07/12/2014"
							  style="text-align: center;">
						</td>
						<td style="width:250px; text-align: center;">&nbsp;&nbsp;to&nbsp;&nbsp;
							<input type="text" name="selectedtoDate" id="selectedtoDate" class="datepicker inputBoxBorder" value="06/01/2015"
							  style="text-align: center;">
						</td>
						<td style="width:320px; text-align: right;">&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</div>

		<!--  Customer info table - begins -->
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
					<tr>
						<td style="width: 160px" align="left">94428 12345</td>
						<td style="width: 160px" align="left">Sridevi Textiles</td>
						<td style="width: 110px" align="left">14 Dec. 2014</td>
						<td>20141214</td>
						<td style="width: 300px" align="left">Dear Mr.Samy, with reference to invoice number 123456. Your Rs.12,000 payment which was due on 12 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 12345</td>
						<td style="width: 160px" align="left">Sridevi Textiles</td>
						<td style="width: 110px" align="left">21 Dec. 2014</td>
						<td>20141221</td>
						<td style="width: 300px" align="left">Dear Mr.Samy, with reference to invoice number 123456. Your Rs.12,000 payment which was due on 12 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 12345</td>
						<td style="width: 160px" align="left">Sridevi Textiles</td>
						<td style="width: 110px" align="left">28 Dec. 2014</td>
						<td>20141228</td>
						<td style="width: 300px" align="left">Dear Mr.Samy, with reference to invoice number 123456. Your Rs.12,000 payment which was due on 12 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 54321</td>
						<td style="width: 160px" align="left">Raman Textiles</td>
						<td style="width: 110px" align="left">12 Dec. 2014</td>
						<td>20141212</td>
						<td style="width: 300px" align="left">Dear Mr.Ram, with reference to invoice number 22256. Your Rs.82,000 payment which was due on 9 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 54321</td>
						<td style="width: 160px" align="left">Raman Textiles</td>
						<td style="width: 110px" align="left">19 Dec. 2014</td>
						<td>20141219</td>
						<td style="width: 300px" align="left">Dear Mr.Ram, with reference to invoice number 22256. Your Rs.82,000 payment which was due on 9 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 54321</td>
						<td style="width: 160px" align="left">Raman Textiles</td>
						<td style="width: 110px" align="left">26 Dec. 2014</td>
						<td>20141226</td>
						<td style="width: 300px" align="left">Dear Mr.Ram, with reference to invoice number 22256. Your Rs.82,000 payment which was due on 9 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">94428 54321</td>
						<td style="width: 160px" align="left">Raman Textiles</td>
						<td style="width: 110px" align="left">1 Jan. 2015</td>
						<td>20150101</td>
						<td style="width: 300px" align="left">Dear Mr.Ram, with reference to invoice number 22256. Your Rs.82,000 payment which was due on 9 Dec. 2014 is not settled.  Pls. settle.</td>
						<td style="width: 30px" align="center"
							title="Customer Contact History"><button type="button"
								class="clockButton" onClick="addCustBank('');">&nbsp;</button></td>
					</tr>
				</tbody>
			</table>
			<!--  Customer info table ends -->
		</form>
	</div>
	<!-- Overlay transparent screen to implement modal window -->
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
	
</div>
<!--  TODO: delme -->
<div id="waterMark"> SAMPLE ONLY </div>
<jsp:include page="footer.jsp" />