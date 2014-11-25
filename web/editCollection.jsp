<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
<%@ page import="com.svnitsai.gm.CustomerBean" %>
<%@ page import="com.svnitsai.gm.CustomerBankBean" %>
<jsp:include page="header.jsp?hideHeader=all" />

<%
	String invoiceId = (String) request.getParameter("id");
	
	CollectionBean bean = DBHandler.getCollectionInfo(invoiceId);
	if(bean != null)
	{
		// Add an empty bean to create fields that serve as template
		bean.getDetailsList().add(0, new CollectionDetailBean());
	}
	
	LinkedHashMap<Integer, CustomerBean> supplierMap = DBHandler.getSuppliers();
%>

<script>
	function addPayment()
	{
		var div1 = document.createElement('div');

		var id = "_" + Math.floor((Math.random() * 100) + 1);

		// Get template data
		var htmlStr = document.getElementById('detailPanel0').innerHTML;
		htmlStr = htmlStr.replace("_0", id);

		div1.innerHTML = htmlStr;

		// append to our form, so that template data
		//become part of form
		document.getElementById('contentDiv').appendChild(div1);
		nidsSetElementFocus("partyBank" + id);
	}

	function deletePayment(panelName)
	{
		var nodeToDelete = document.getElementById(panelName);
		document.getElementById('contentDiv').removeChild(nodeToDelete);
	}
</script>

<form action="/gm/servlet/collection" id="editForm">
<div id="contentDiv" style="color: #333; padding: 10px; margin: 0px; background: #FFF; width:100% min-height: 100%">
<% if(bean != null) { %>
	
	<input type="hidden" name="action" value="saveCollection" /> 
	<input type="hidden" name="collectionRefID" value="<%= bean.getCollectionId() %>"/>
 	<fieldset>
	<legend>&nbsp;Invoice Details</legend>
	<table  cellspacing="5" cellpadding="5">
		<tr>
			<td>Merchant Name:</td>
			<td><%= bean.getPartyName() %></td>
		</tr>
		<tr>
			<td>Phone Number:</td>
			<td><%=bean.getPartyInfo()%></td>
		</tr>
		<tr>
			<td>Invoice Number:</td>
			<td><%= bean.getInvoiceNumber() %></td>
		</tr>
		<tr>
			<td>Invoice Amount:</td>
			<td><%= bean.getInvoiceAmount() %></td>
		</tr>
		<tr>
			<td>Collection Due Date:</td>
			<td><%= bean.getDueDateStr() %></td>
		</tr>
		<tr>
			<td>Status:</td>
			<td><%= bean.getStatus() %></td>
		</tr>
	</table>
	</fieldset>
	<br>
	<button type="button" class="addButton" onClick="addPayment();">&nbsp;&nbsp;Add Payment</button>
	<br>
	<br>
<% for(int i=0; i< bean.getDetailsList().size(); i++)
   { 
		CollectionDetailBean detailBean = bean.getDetailsList().get(i);
		String hideStr = "";
		if(i == 0)
		{
			hideStr = "style=\"display:none\"";
		}
		
		CustomerBean supplierBean = supplierMap.get(detailBean.getS)
%>
<div id="detailPanel<%=i %>" <%=hideStr%>>
	<input type="hidden" name="detailRefID_<%= i %>" value="<%= detailBean.getCollectionDetailId() %>" />
	<fieldset>
	<legend>&nbsp;Payment Detail</legend>
	<table  cellspacing="5" cellpadding="5" width="100%">
		<tr>
			<td nowrap>Paid To Supplier:</td>
			<td nowrap> <%= detailBean.getSupplierName() %></td>
			<td valign="top" align="right" width="100%" rowspan="9">
				<button type="button" class="deleteButton" onClick="deletePayment('detailPanel<%=i%>');">&nbsp;&nbsp;Delete Payment</button>
			</td>
		</tr>
		<tr>
			<td nowrap>Supplier's Bank Name:</td>
			<td nowrap> <%= detailBean.getSupplierBank() %></td>
		</tr>
		<tr>
			<td nowrap>Supplier's A/C Number:</td>
			<td nowrap> <%= detailBean.getSupplierAccountNum() %></td>
		</tr>
		<tr>
			<td nowrap>Merchant's Bank:</td>
			<td nowrap><input type="text" name="partyBank_<%=i%>" value="<%= detailBean.getPartyBank() %>" /></td>
		</tr>
		<tr>
			<td nowrap>Merchant's Bank Branch:</td>
			<td nowrap><input type="text" name="partyBankBranch_<%=i%>" value="<%= detailBean.getPartyBankBranch() %>" /></td>
		</tr>
		<tr>
			<td nowrap>Collection Amount:</td>
			<td nowrap><input type="text" name="collectionAmt_<%=i%>" value="<%= detailBean.getCollectionAmount() %>"/></td>
		</tr>
		<tr>
			<td nowrap>Collection Date:</td>
			<td nowrap><input type="text" name="collectionDate_<%=i%>" value="<%= detailBean.getCollectionDateStr() %>" class="datepicker" /></td>
		</tr>
		<tr>
			<td nowrap>Deferred Date:</td>
			<td nowrap><input type="text" name="deferedDate_<%=i%>" value="<%= detailBean.getDeferedDateStr() %>" class="datepicker" /></td>
		</tr>
		<tr>
			<td nowrap>Ledger Page Number:</td>
			<td nowrap><input type="text" name="ledger_<%=i%>" value="<%= detailBean.getLedgerNumber() %>"/></td>
		</tr>
	</table>
	</fieldset>
	<br/>
</div>
	<% } %>
	
	
  <% } else { %>
	<h3>No results found</h3>
  <% } %>
</div>
</form>

</body>
</html>