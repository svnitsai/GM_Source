<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<jsp:include page="header.jsp?hideHeader=true" />
<%@ page import="com.svnitsai.gm.CustomerBean" %>
<%@ page import="com.svnitsai.gm.Util" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.DailyPayableBean" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.text.SimpleDateFormat" %>
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

<style>
label.error { 
	display:block;
	color: red; 
}

input.error {
	border:1px solid red;
}
</style>
<script>
	
	$( document ).ready(function() {
		nidsHideElement('supplierPanel_0');
		nidsEnableControl('supplier_0', false);
		nidsEnableControl('amount_0', false);
		nidsEnableControl('date_0', false);
		
		<% if(payableList.size() == 1) { %>
		addSupplier();
		<% } %>
		
	<% if(status != null) { %>
		$( "#status-message" ).dialog({
			modal: true,
			buttons: {
				Ok: function() {
					$( this ).dialog( "close" );
				}
			}
		});
	<% } %>
		
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
		
		nidsEnableControl('supplier' + id, true);
		nidsEnableControl('amount' + id, true);
		nidsEnableControl('date' + id, true);

		$.datepicker.setDefaults({dateFormat:"dd/mm/yy", minDate:0});  
		$('#date' + id).datepicker();
		$('#date' + id).datepicker('setDate', new Date());


	}
	
	function deleteSupplier(panelName)
	{
		var nodeToDelete = document.getElementById(panelName);
		document.getElementById('contentDiv').removeChild(nodeToDelete);
	}
	
	function isPageValid()
	{
		$.validator.messages.required = 'Please specify value';
		var form = $( "#payableform" );
	   	form.validate();
     	if(form.valid() == false)
      	{
       		return false;
      	}

		return true;
	}
	
	function printReport()
	{
		
		$("#payableform").prop("target", "_blank");
     	$('#action').val('print');
    	nidsSubmitDocumentForm(false);
	}

	function saveChanges()
	{
		$("#payableform").prop("target", "_self");
    	$('#action').val('save');
    	nidsSubmitDocumentForm(true);
	}
</script>
<% if(status != null) { %>
	<div id="status-message" title="Save Result">
	<p>
	<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
	<%= status %>
	</p>
	</div>
<% } %>
<form action="/gm/servlet/payable" id="payableform">
<input type="hidden" name="action" id="action" value="save">
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
	<% 	for(int i=0; i < payableList.size(); i++) 
		{
			DailyPayableBean bean = payableList.get(i);
			String amt = String.valueOf(bean.getPayableAmount());
			if(bean.getPayableAmount() == 0)
			{
				amt = "";
			}
			String date = "";
			if(bean.getPayableDate() != null)
			{
				date = new SimpleDateFormat("dd/MM/yyyy").format(bean.getPayableDate());
			}
	%>
		
		<div id="supplierPanel_<%=i %>" name="supplierPanel_<%=i %>">
			<input type="hidden" name="refId_<%= i %>" id="refId_<%=i %>" value="<%= bean.getPayableId() %>">
			<table width="100%" cellpadding="10" cellspacing="5">
				<tr>
					<td align="center" width="25%" valign="top">
						<select name="supplier_<%=i %>" id="supplier_<%=i %>" class="chosen-select" style="width:175px" 

required>
		      				<option value="" selected disabled>Select Supplier</option>
		      				<% for(CustomerBean supplierBean : supplierMap.values()) { %>
		      					<option value="<%= supplierBean.getId() %>" <% if(supplierBean.getId() == 

bean.getSupplierCode()){ %> selected<%} %>>
		      						<%= supplierBean.getName() %>
		      					</option>
		      				<% } %>
		      			</select>	
					</td>
					<td align="center" valign="top">
						<input type="text" name="amount_<%=i %>" id="amount_<%=i %>" size="20px" value="<%= amt %>" 

class="number required"/>
					</td>
					<td align="center" valign="top">
						<input type="text" name="date_<%=i %>" id="date_<%=i %>" <% if(i>0){%>class="datepicker" 

value="<%= date %>"<%}%> size="12px" readonly="true" required/>
					</td>
					<td valign="top" width="40%" align="center">
						<textarea name="instructions_<%=i %>" id="instructions_<%=i %>" cols="40"><%= 

bean.getInstructions() %></textarea>
					</td>
					<td valign="top">
						<button type="button" class="deleteButton" onclick="deleteSupplier('supplierPanel_<%=i

%>');">&nbsp;</button>
					</td>
				</tr>
			</table>
		</div>
	<% } %>
  </div>
  <br>
	<button type="button" class="addButton" onClick="addSupplier();">&nbsp;&nbsp;Add Supplier</button> 
	&nbsp;
	<input type="submit" class="saveButton" onClick="saveChanges();" value="Save"/> 
	&nbsp;
	<input type="submit"  class="printButton" onClick="printReport();" value="Print Report">
	&nbsp;
	<br>
</div>
 <jsp:include page="footer.jsp" />