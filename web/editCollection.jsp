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
	String selectedDate = (String) request.getParameter("date");
	String merchantId = (String) request.getParameter("merchantId");
	
	CollectionBean bean = DBHandler.getCollectionInfo(invoiceId);
	if(bean != null)
	{
		// Add an empty bean to create fields that serve as template
		bean.getDetailsList().add(0, new CollectionDetailBean());
	}
	
	LinkedHashMap<Long, CustomerBean> supplierMap = DBHandler.getSuppliers();
	
	LinkedHashMap<Long, String> companyMap = DBHandler.getCustomerIdMap("Company");
%>

<script>
	function pageInit()
	{
		
	}

	function addPayment()
	{
		var div1 = document.createElement('div');

		var id = "_" + Math.floor((Math.random() * 100) + 1);

		// Get template data
		var htmlStr = document.getElementById('detailPanel_0').innerHTML;
		htmlStr = htmlStr.replace(/_0/g, id);

		div1.innerHTML = htmlStr;
		div1.id='detailPanel' + id;

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
	
	function selectSupplier(supplierCompId, supplierBankCompId)
	{
		var supplierId = nidsGetSelectedOptionValue(supplierCompId);
		var supplierBankComp = nidsGetElementByName(supplierBankCompId);
		var bankoptions = supplierBankComp.options;
    		for (var i = 1; i < bankoptions.length; i++) {
        		nidsRemoveOption(supplierBankCompId, 1);
    		}
		
		var option;
		
<%  
	int insertIndex = 1;
	
	for(long id : supplierMap.keySet())
	{
%>
		if(supplierId == '<%= id %>')
		{
			
		<% CustomerBean supplierBean = supplierMap.get(id);
			for(CustomerBankBean bankBean: supplierBean.getBankAccountList()) { %>
				option = document.createElement("option");
				option.text = '<%= bankBean.getBankName() + ", " + bankBean.getBranchName() + ", A/c #" + bankBean.getAccountNumber() %>';
				option.value = "<%= bankBean.getBankId()%>";
				supplierBankComp.add(option);
		<%	} %>
		}
<%	} %>
	}
		

</script>
<form action="/gm/servlet/collection" id="editForm">
<div id="contentDiv" style="color: #333; padding: 10px; margin: 0px; background: #FFF; width:100% min-height: 100%">
<% if(bean != null) { %>
	
	<input type="hidden" name="action" value="saveCollection" /> 
	<input type="hidden" name="collectionRefId" value="<%= bean.getCollectionId() %>"/>
	<input type="hidden" name="selectedDate" value="<%= selectedDate %>"/>
	<input type="hidden" name="merchantId" value="<%= merchantId %>"/>
	<input type="hidden" name="invoiceAmt" value="<%= bean.getInvoiceAmount()  %>"/>
 	<fieldset>
	<legend>&nbsp;Invoice Details</legend>
	<table  cellspacing="5" cellpadding="5">
		<tr>
			<td>Merchant Name:</td>
			<td><%=bean.getCustName()%></td>
		</tr>
		<tr>
			<td>Phone Number:</td>
			<td><%=bean.getCustPhoneNumber() %></td>
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
			<td>Deferred Date:</td>
			<td><input type="text" name="deferredDate" value="<%= bean.getDeferredDateStr() %>">
			<label class="instructions">(Example: 31/12/2014 for Dec 31, 2014)</label>
			</td>
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
		
		String supplierName = "";
		String supplierBankInfo = "";
		for(CustomerBean supplierBean : supplierMap.values()) 
		{
			if (detailBean.getSupplierCode() == supplierBean.getId()) 
			{
				supplierName = supplierBean.getName();
				for(CustomerBankBean bankBean : supplierBean.getBankAccountList() ) 
				{ 
					if( bankBean.getBankId() == detailBean.getSupplierBankId()) 
					{
						supplierBankInfo = bankBean.getBankName() + ", " + bankBean.getBranchName() + ", A/c #" + bankBean.getAccountNumber();
					}
				}
			}
		}
		
   
%>
<div id="detailPanel_<%=i %>" <%=hideStr%>>
	<input type="hidden" name="detailRefID_<%= i %>" value="<%= detailBean.getCollectionDetailId() %>" />
	<fieldset>
	<legend>&nbsp;Payment Details</legend>
	<table  cellspacing="5" cellpadding="5" width="100%">
		<tr>
			<td nowrap>Paid To Supplier:</td>
			<td nowrap> 
			<% if(supplierName.length() > 0) { %>
				<%= supplierName %>
			<% } else { %>
				<select name="supplierId_<%=i%>" 
						id="supplierId_<%=i%>" 
						onChange="selectSupplier('supplierId_<%=i%>', 'supplierBankId_<%=i%>');">
						<option value="" selected disabled>Select Supplier</option>
					<% for(CustomerBean supplierBean : supplierMap.values()) { %>
						<option value="<%= supplierBean.getId() %>">
							<%= supplierBean.getName() %>
						</option>
					<% } %>
				</select>
			<%  } %>
			</td>
			<td valign="top" align="right" width="100%" rowspan="9">
				<% if(detailBean.getLedgerNumber() == 0) { %>
				<button type="button" class="deleteButton" onClick="deletePayment('detailPanel_<%=i%>');">&nbsp;&nbsp;Delete Payment</button>
				<% } %>
			</td>
		</tr>
		<tr>
			<td nowrap>Supplier's Bank Account:</td>
			<td nowrap>
			<% if(supplierBankInfo.length() > 0) { %>
				<%= supplierBankInfo %>
			<% } else { %>
				<select name="supplierBankId_<%=i%>" id="supplierBankId_<%=i%>">
					<option value="" selected disabled>Select Bank</option>
				</select>
			<%  } %>
			</td>
		</tr>
		<tr>
			<td nowrap>Merchant's Bank Name:</td>
			<td nowrap>
				<% if(i == 0) { %>
				<input type="text" name="merchantBank_<%=i%>" value="" /></td>
				<% } else { %>
					<%= detailBean.getCustomerBankName() %>
				<% } %>
		</tr>
		<tr>
			<td nowrap>Paid Amount:</td>
			<td nowrap>
				<input type="text" name="paidAmt_<%=i%>" 
						value="<%= detailBean.getPaidAmount() %>" 
						<% if(i>0) {%> hidden <%} %>/>
				<% if(i>0) { out.print(detailBean.getPaidAmount()); } %>
			</td>
			 
		</tr>
		<tr>
			<td nowrap>Payment Date:</td>
			<td nowrap>
			<% if(i == 0) { 
				// TODO: Date picker is not working. Add validations
			%>
				<input type="text" name="collectionDate_<%=i%>" class="datepicker"/> 
				<label class="instructions">(Example: 31/12/2014 for Dec 31, 2014)</label>
			<% } else {%>
					<%= detailBean.getCollectionDateStr() %>
			<% } %>
			</td>
		</tr>
		<tr>
			<td nowrap>Ledger Page Number:</td>
			<td nowrap><input type="text" name="ledger_<%=i%>" value="<%= detailBean.getLedgerNumber() %>"/></td>
		</tr>
		<tr>
			<td nowrap>Company Name:</td>
			<td nowrap>
				<select name="companyID_<%=i%>" 
						id="companyID_<%=i%>">
						<option value="" selected disabled>Select Company</option>
					<% for(Entry<Long, String> entry : companyMap.entrySet()) { %>
						<option value="<%= entry.getKey() %>"
							<%if(entry.getKey() == detailBean.getCompanyCode()) {%> selected <%} %>>
							<%= entry.getValue() %>
						</option>
					<% } %>
				</select>
			</td>
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