<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.CustomerBean"%>
<%@ page import="com.svnitsai.gm.Util"%>
<%@ page import="com.svnitsai.gm.DBHandler"%>
<%@ page import="com.svnitsai.gm.DailyPayableBean"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	LinkedHashMap<Long, CustomerBean> supplierMap = DBHandler.getSuppliers();
SimpleDateFormat todayDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

String todayDate = todayDateFormatter.format(Calendar.getInstance().getTime());
String status = request.getParameter("status");

LinkedList<DailyPayableBean> payableList = DBHandler.getDailyPayables(Calendar.getInstance().getTime());
if(payableList == null)
{
	payableList = new LinkedList<DailyPayableBean>();
}
DailyPayableBean dummyBean = new DailyPayableBean();
payableList.add(0, dummyBean);
%>
<script type="text/javascript">
	$(document).ready(function() {
		$('#scrollCustomerInfoTable').dataTable({
			"scrollY" : "250px", //Height of the table
			"scrollCollapse" : true,
			"paging" : false
		});

	});
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
			<!-- TODO: change me -->
			<form action="/gm/web/DataRefreshServlet" method="post">
				<table style="border-collapse: collapse; height: 50px;">
					<tr style="background-color: rgba(241, 245, 235, 0.61);">
						<td style="width:250px; text-align: center; ">Customers to show: &nbsp;&nbsp;&nbsp;</td>
						<td style="width:250px; text-align: center;"><input type="radio" name="filterCustomerBy"
								value="merchant" onSelect="filterCustomer()" checked>
								Merchants</td>
						<td style="width:420px; text-align: left;"><input type="radio" name="filterCustomerBy"
								value="supplier" onSelect="filterCustomer()">
								Suppliers
						</td>
					</tr>
				</table>
			</form>
		</div>

		<!--  Customer info table - begins -->
		<form action="/gm/servlet/payable" id="payableform">
			<input type="hidden" name="action" id="action" value="save">
			<table id="scrollCustomerInfoTable" class="display" cellspacing="0"
				width="100%">
				<thead>
					<tr>
						<th>Customer Name</th>
						<th>Customer Address</th>
						<th>Customer City</th>
						<th>&nbsp;&nbsp;&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
					<tr>
						<td style="width: 160px" align="left">RAMCHANDRA DRESSESS</td>
						<td style="width: 160px" align="left">C-WING,1 TO 32,
							AMRAVATI NAGPUR HIGHWAY, NH6 TABORGAON,</td>
						<td style="width: 110px" align="left">DHARMALE</td>
						<td style="width: 30px" align="center"
							title="Bank Information"><button type="button"
								class="infoButton" onClick="addCustBank();">&nbsp;</button></td>
					</tr>
				</tbody>
			</table>
			<!--  Customer info table ends -->
		</form>
	</div>

	<!--   <br> -->
	<!-- 	<button type="button" class="addButton" onClick="addSupplier();">&nbsp;&nbsp;Add Supplier</button>  -->
	<!-- 	&nbsp; -->
	<!-- 	<!-- Fix to display ICONS -->
	<!-- 	<input type="submit" class="saveButton" onClick="saveChanges();" value="Save"/>  -->
	<!-- 	&nbsp;  -->
	<!-- 		<input type="submit"  class="printButton" onClick="printReport();" value="Print Report"> -->
	<!-- 	&nbsp; -->
	<!-- 	-->
	<!-- 	<button type="button" class="saveButton" onClick="saveChanges();">&nbsp;&nbsp;Save</button>  -->
	<!-- 	&nbsp; -->
	<!-- 	<button type="button"  class="printButton" onClick="printReport();">&nbsp;&nbsp;Print Report</button> -->
	<!-- 	&nbsp; -->
	<!-- 	<br> -->
</div>
<jsp:include page="footer.jsp" />