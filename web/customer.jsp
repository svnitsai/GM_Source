<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.database.provider.CustomerInfoProvider" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>


<!-- 
 * customer.jsp
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Screen lists customer table data by customerType; Has link
 *              to customer bank information edit screen.
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
		$('#scrollCustomerInfoTable').dataTable({
			"scrollY" : "250px", //Height of the table
			"scrollCollapse" : true,
			"paging" : true,
			"aoColumns": [
                          {"bSortable": true},
                          {"bSortable": true},
                          {"bSortable": true},
                          {"bSortable": false}
			              ]
		});
		
// 		$(".nosorting").each(function() {
// 			$(this).removeClass("sorting");
// 		})

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

<div id="one">
	<div class="item" id="contentDiv" name="contentDiv">
		<!-- Customer Filter by Supplier / Merchant -->
		<div>
			<form action="/gm/web/CustomerServlet" method="post">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: center; ">Customers to show: &nbsp;&nbsp;&nbsp;</td>
						<td style="width:250px; text-align: center;"><input type="radio" name="filterCustomerBy"
								value="merchant" onclick="submit()"<%if (customerTypeProc.equalsIgnoreCase("merchant")) {%> checked <% } %>>
								Customers</td>
						<td style="width:200px; text-align: left;"><input type="radio" name="filterCustomerBy"
								value="supplier"  onclick="submit()" <%if (customerTypeProc.equalsIgnoreCase("supplier")) {%> checked <% } %>>
								Suppliers
						</td>
						<td style="width:220px; text-align: right;">&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</div>

		<!--  Customer info table - begins -->
		<form>
			<input type="hidden" name="action" id="action" value="save">
			<table id="scrollCustomerInfoTable" class="display" cellspacing="0"
				width="100%">
				<thead>
					<tr>
						<th>Customer Name</th>
						<th>Customer Address</th>
						<th>Customer City</th>
						<th  class="nosorting">&nbsp;&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<% 		  if ((customerInfoList != null) && (customerInfoList.size() > 0)) {
								  for (Object object : customerInfoList) { 
									  Map row = (Map) object;
									  String dispCustName = row.get("CustName").toString();
									  String dispCustAddress = DisplayUtil.getAddress(row.get("CustAddress1").toString()
											  , row.get("CustAddress2").toString(), row.get("CustAddress3").toString(), row.get("CustAddress4").toString());
									  String dispCity = DisplayUtil.getCity(row.get("CustCity").toString());
									  String dispCustCode = row.get("CustCode").toString();
					%>
					<tr>
						<td style="width: 160px" align="left"><%=dispCustName %></td>
						<td style="width: 160px" align="left"><%=dispCustAddress %></td>
						<td style="width: 110px" align="left"><%=dispCity %></td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank('<%=dispCustCode%>', '<%=dispCustName%>', '<%=dispCity%>', '<%=customerTypeProc%>');">&nbsp;</button></td>
					</tr>
					
					<%
								  } 
						  }

					%>
				</tbody>
			</table>
			<!--  Customer info table ends -->
		</form>
	</div>
	<!-- Overlay transparent screen to implement modal window -->
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
	
</div>
<jsp:include page="footer.jsp" />