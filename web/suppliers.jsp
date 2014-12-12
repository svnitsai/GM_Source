<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.CustomerBean" %>
<%@ page import="com.svnitsai.gm.Util" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<script>
	$( document ).ready(function() {
		addSupplier();
	});
	
	function addSupplier()
	{
		var div1 = document.createElement('div');
	
		var id = "_" + Math.floor((Math.random() * 100) + 1);
	
		// Get template data
		var htmlStr = document.getElementById('supplierPanel_0').innerHTML;
		htmlStr = htmlStr.replace(/_0/g, id);
	
		div1.innerHTML = htmlStr;
		div1.id='supplierPanel' + id;
	
		// append to our form, so that template data
		//become part of form
		document.getElementById('contentDiv').appendChild(div1);
	}
	
	function deleteSupplier(panelName)
	{
		var nodeToDelete = document.getElementById(panelName);
		document.getElementById('contentDiv').removeChild(nodeToDelete);
	}
	
	function isPageValid()
	{
		return true;
	}
	
	function printReport()
	{
		$('#action').val('print');
		nidsSubmitDocumentForm(false);
	}
</script>
<%
LinkedHashMap<Long, CustomerBean> supplierMap = DBHandler.getSuppliers();
SimpleDateFormat todayDateFormatter = new SimpleDateFormat("dd/MM/yyyy");

String todayDate = todayDateFormatter.format(Calendar.getInstance().getTime());
String status = request.getParameter("status");
%>
<form action="/gm/servlet/payable">
<input type="hidden" name="action" id="action" value="save">
<% if(status != null) { %>    
  	<h2><%= status %></h2>
  <% } %>
<div id="two">
  <div class="item" id="contentDiv" name="contentDiv">
  	<table width="100%" cellpadding="10" cellspacing="5">
		<tr>
			<td nowrap align="center" width="25%"><label class="colHead">Supplier</label></td>
			<td align="center"><label class="colHead">Payment Amount</label></td>
			<td align="center"><label class="colHead">Pay By Date</label></td>
			<td align="center" width="43%"><label class="colHead">Instructions</label></td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<div id="supplierPanel_0" name="supplierPanel_0" style="display:none">
		<table width="100%" cellpadding="10" cellspacing="5">
			<tr>
				<td align="center" width="25%" valign="top">
					<select name="supplier_0" id="supplier_0" class="chosen-select" style="width:175px">
	      				<option value="" selected disabled>Select Supplier</option>
	      				<% for(CustomerBean supplierBean : supplierMap.values()) { %>
	      					<option value="<%= supplierBean.getId() %>">
	      						<%= supplierBean.getName() %>
	      					</option>
	      				<% } %>
	      			</select>	
				</td>
				<td align="center" valign="top">
					<input type="text" name="amount_0" id="amount_0" size="20px"/>
				</td>
				<td align="center" valign="top">
					<input type="text" name="date_0" id="date_0" value="<%= todayDate%>" size="12px"/>
				</td>
				<td valign="top" width="40%" align="center">
					<textarea name="instructions_0" id="instructions_0" cols="40"></textarea>
				</td>
				<td valign="top">
					<button type="button" class="deleteButton" onclick="deleteSupplier('supplierPanel_0');">&nbsp;</button>
				</td>
			</tr>
		</table>
	</div>
  </div>
  <br>
	<button type="button" class="addButton" onClick="addSupplier();">&nbsp;&nbsp;Add Supplier</button> 
	&nbsp;
	<button type="submit" class="saveButton">&nbsp;&nbsp;Save</button> 
	&nbsp;
	<button type="button" class="printButton" onClick="printReport();">&nbsp;&nbsp;Print Report</button> 
	&nbsp;
	<br>
</div>
 <jsp:include page="footer.jsp" />