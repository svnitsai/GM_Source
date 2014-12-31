<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
<%@ page import="com.svnitsai.gm.CustomerBean" %>
<%@ page import="com.svnitsai.gm.CustomerBankBean" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
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

	$( document ).ready(function() {
		nidsHideElement('detailPanel_0');
		nidsEnableControl('supplierId_0', false);
		nidsEnableControl('supplierBankId_0', false);
		nidsEnableControl('paidAmt_0', false);
		nidsEnableControl('collectionDate_0', false);
		$.datepicker.setDefaults({dateFormat:"dd/mm/yy"});
		<%  Calendar dueDateCalendar = null;
			if(bean != null) { 
				Date dueDate = bean.getDueDate();
				if(dueDate != null)
				{
					dueDateCalendar = Calendar.getInstance();
					dueDateCalendar.setTime(dueDate);
				}
			}
			
			if(dueDateCalendar != null)
			{
		%>
				$('#deferredDate').datepicker({
					minDate: new Date(<%= dueDateCalendar.get(Calendar.YEAR)%>, <%= 

dueDateCalendar.get(Calendar.MONTH)%>, <%= dueDateCalendar.get(Calendar.DAY_OF_MONTH)%>)
				}); 
		<%  } %>
	});

	function OnClose()
	{
	    if(window.opener != null && !window.opener.closed) 
	    {
	    	window.opener.location.reload(false);
	       window.opener.hideModalDiv();
	    }
	}
	window.onunload = OnClose;

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

		 
		$('#collectionDate' + id).datepicker();
		$('#collectionDate' + id).datepicker('setDate', new Date());
		
		//nidsEnableControl('supplierId' + id, true);
		//nidsEnableControl('supplierBankId' + id, true);
		nidsEnableControl('paidAmt' + id, true);
		nidsEnableControl('collectionDate' + id, true);
		nidsSetElementFocus("paidAmt" + id);
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
				option.text = '<%= bankBean.getBankInfoString() %>';
				option.value = "<%= bankBean.getBankId()%>";
				supplierBankComp.add(option);
		<%	} %>
		}
<%	} %>
	}

	function enableSupplierFields(index, value)
	{
		nidsEnableControl('supplierId'+index, value);
		nidsEnableControl('supplierBankId'+index, value);
	}
	
	function isPageValid()
	{
		$.validator.messages.required = 'Please specify value';
		var form = $( "#editForm" );
	   	form.validate();
     		if(form.valid() == false)
      		{
       			return false;
      		}

     	// validate that the total paid amount did not exceed the invoice amount
     	var invoiceAmt = <%= bean.getInvoiceAmount() %>
     	var totalPaidAmt = 0;
	var elem = document.getElementById('editForm').elements;
     	for(var i = 0; i < elem.length; i++)
        {
     		var elemName = elem[i].name;
     		if(elemName.search("paidAmt_") != -1)
     		{
     			var paidAmt = elem[i].value;
     			paidAmt = paidAmt.replace(/,/g, '');
     			var iPaidAmt = parseFloat(paidAmt);
			if(isNaN(iPaidAmt) == false)
			{
				totalPaidAmt = totalPaidAmt + iPaidAmt;
 			}
     		}
        }
        
     	if(totalPaidAmt > invoiceAmt)
     	{
     		alert("Error: Total Payments (Rs. " + totalPaidAmt + ") is more than the invoice amount.");
     		return false;
     	}
     	
		return true;
	}
	
	function saveChanges()
	{
	    	nidsSubmitDocumentForm(true);
	}

</script>
<form action="/gm/servlet/collection" id="editForm">
       
<div id="contentDiv" style="width: auto; min-height: 0px; max-height: none; height: 470px; overflow:auto; padding:10px; background-color:white"> 
<% if(bean != null) { 
	String deferredDate = "";
	if(bean.getDeferredDate() != null)
	{
		deferredDate = new SimpleDateFormat("dd/MM/yyyy").format(bean.getDeferredDate());
	}
%>
	
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
			<td>Invoice Date:</td>
			<td><%= bean.getInvoiceDateStr() %></td>
		</tr>
		<tr>
			<td>Invoice Amount:</td>
			<td><span style="font-family: DejaVu Sans;">&#x20b9; </span> <%= DisplayUtil.getDisplayAmount(bean.getInvoiceAmount())%></td>
		</tr>
		<tr>
			<td>Collection Due Date:</td>
			<td><%= bean.getDueDateStr() %></td>
		</tr>
		<tr>
			<td>Deferred Date:</td>
			<td><input type="text" name="deferredDate" id="deferredDate" value="<%= deferredDate %>" readonly="true">
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
		
		
		String supplierName = "";
		String supplierBankInfo = "";
		boolean paidToSupplier = false;
		if(detailBean.getSupplierCode() > 0 )
		{
			paidToSupplier = true;
		}
		
		if(paidToSupplier)
		{
			for(CustomerBean supplierBean : supplierMap.values()) 
			{
				if (detailBean.getSupplierCode() == supplierBean.getId()) 
				{
					supplierName = supplierBean.getName();
					for(CustomerBankBean bankBean : supplierBean.getBankAccountList() ) 
					{ 
						if( bankBean.getBankId() == detailBean.getSupplierBankId()) 
						{
							supplierBankInfo = bankBean.getBankInfoString();
						}
					}
				}
			}
		}
		
   
%>
<div id="detailPanel_<%=i %>">
	<input type="hidden" name="detailRefID_<%= i %>" value="<%= detailBean.getCollectionDetailId() %>" />
	<fieldset>
	<legend>&nbsp;Payment Details</legend>
	<table  cellspacing="5" cellpadding="5" width="100%">
		<tr>
			<td nowrap>Payment Type:</td>
			<td nowrap>
				<% if(i==0) { %>
				<input 	type="radio" 
						name="paymentType_<%=i %>" 
						id="paymentType_<%=i %>" 
						value="0" 
						onChange="enableSupplierFields('_<%= i %>', false);"
						checked> Cash &nbsp;&nbsp;
						
				<input 	type="radio" 
						name="paymentType_<%=i %>" 
						id="paymentType_<%=i %>" 
						value="1" 
						onChange="enableSupplierFields('_<%= i %>', true);"
						> Paid to Supplier &nbsp;&nbsp;
						
				<input 	type="radio" 
						name="paymentType_<%=i %>" 
						id="paymentType_<%=i %>" 
						value="-1" 
						onChange="enableSupplierFields('_<%= i %>', false);"
						> Adjustment &nbsp;&nbsp;
						
				<input 	type="radio" 
						name="paymentType_<%=i %>" 
						id="paymentType_<%=i %>" 
						value="-2" 
						onChange="enableSupplierFields('_<%= i %>', false);"
						> RG
				<% } else  {
					if( paidToSupplier)
					{
						out.print("Paid To Supplier");
					}
					else if(detailBean.getSupplierCode() == 0)
					{
						out.print("Cash");
					}
					else if(detailBean.getSupplierCode() == -1)
					{
						out.print("Adjustment");
					}
					else if(detailBean.getSupplierCode() == -2)
					{
						out.print("RG");
					}
				  } %>
			</td>
			<td valign="top" align="right" width="100%">
				<% if(detailBean.getLedgerNumber() == 0) { %>
					<button type="button" class="deleteButton" onClick="deletePayment('detailPanel_<%=i%>');">&nbsp;&nbsp;Delete Payment</button>
				<% } %>
			</td>
		</tr>
		<tr <%if(i>0 && !paidToSupplier){ %>style="display:none"<%} %>>
			<td nowrap>Supplier Name:</td>
			<td nowrap colspan="2"> 
			<% if(supplierName.length() > 0) { %>
				<%= supplierName %>
			<% } else { %>
				<select name="supplierId_<%=i%>" 
						id="supplierId_<%=i%>" 
						onChange="selectSupplier('supplierId_<%=i%>', 'supplierBankId_<%=i%>');"
						<%if(!paidToSupplier){ %>disabled<%}%>
						required>
						<option value="" selected disabled>Select Supplier</option>
					<% for(CustomerBean supplierBean : supplierMap.values()) { %>
						<option value="<%= supplierBean.getId() %>">
							<%= supplierBean.getName() %>
						</option>
					<% } %>
				</select>
			<%  } %>
			</td>
		</tr>
		<tr <%if(i>0 && !paidToSupplier){ %>style="display:none"<%} %>>
			<td nowrap>Supplier's Bank Account:</td>
			<td nowrap colspan="2">
			<% if(supplierBankInfo.length() > 0) { %>
				<%= supplierBankInfo %>
			<% } else { %>
				<select name="supplierBankId_<%=i%>" 
						id="supplierBankId_<%=i%>" 
						<%if(!paidToSupplier){%>disabled<%}%>
						required>
					<option value="" selected disabled>Select Bank</option>
				</select>
			<%  } %>
			</td>
		</tr>
		<tr>
			<td nowrap>Paid Amount:</td>
			<td nowrap colspan="2">
				<% if(i==0) { %>
					<input type="text" name="paidAmt_<%=i%>"  class="number required"/>
				<% } else { %>
					<input type="hidden" name="paidAmt_<%=i%>" value="<%=detailBean.getPaidAmount()%>"/>
					<span style="font-family: DejaVu Sans;">&#x20b9; </span> <%= DisplayUtil.getDisplayAmount(detailBean.getPaidAmount())%>
				<% } %>
			</td>
			 
		</tr>
		<tr>
			<td nowrap>Payment Date:</td>
			<td nowrap colspan="2">
			<% if(i == 0) { %>
				<input type="text" 
						name="collectionDate_<%=i%>" 
						id="collectionDate_<%=i%>"
						required/> 
			<% } else {%>
					<%= detailBean.getCollectionDateStr() %>
			<% } %>
			</td>
		</tr>
		<tr>
			<td nowrap>Ledger Page Number:</td>
			<td nowrap colspan="2">
				<input type="text" name="ledger_<%=i%>" value="<%= detailBean.getLedgerNumber() %>"/>
			</td>
		</tr>
		<tr>
			<td nowrap>Company Name:</td>
			<td nowrap colspan="2">
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
<div style="position:absolute; width:100%; padding:10px;">
	<button type="button" class="saveButton" onClick="saveChanges();">&nbsp;&nbsp;Save</button> 
	&nbsp;&nbsp;
	<button type="button" class="cancelButton" onClick="window.close();">&nbsp;&nbsp;Cancel</button> 
</div>
</form>

</body>
</html>