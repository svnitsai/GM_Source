ï»¿<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<jsp:include page="header.jsp?hideHeader=true" />
<%
	Collection<CollectionBean> collectionBeanList = (Collection<CollectionBean>) request.getAttribute("collectionData");
	String action = (String) request.getAttribute("action");
	String selectedDate = (String) request.getAttribute("selectedDate");
	String merchantId = (String) request.getAttribute("merchantId");
	String merchantName = (String) request.getAttribute("merchantName");
	
	LinkedHashMap<Long, String> merchantMap = DBHandler.getMerchants();
	
	boolean isMerchantSelected = false;
	String displayStr = "";
	if(action != null)
	{
		displayStr = "Changes saved successfully";
	}
	else if(selectedDate != null && selectedDate.length() > 0)
	{
		displayStr = "Payments due on or before " + selectedDate;
	}
	else if(merchantName != null  && merchantName.length() > 0)
	{
		displayStr = "Collection details for " + merchantName;
		isMerchantSelected = true;
	}
	
	if(selectedDate == null)
	{
		selectedDate = "";
	}
	
	if(merchantId == null)
	{
		merchantId = "";
	}
%>
<script>
$( document ).ready(function() {
	handleFilterChange();
});
function isPageValid()
{
	$.validator.messages.required = 'Please specify value';
	var form = $( "#editForm" );
   	form.validate();
 		if(form.valid() == false)
  		{
   			return false;
  		}

	return true;
}

function saveChanges()
{
    	nidsSubmitDocumentForm(true);
}

	function setMerchantName()
	{
		var selectedMerchant = $( "#merchantId option:selected" ).text().trim();
		nidsSetElementValue("merchantName", selectedMerchant);
	}

var popUpObj;
        
	function editCurrentRow(invoiceId) 
	{


		popUpObj=window.open("/gm/web/editCollection.jsp?id=" + invoiceId + "&date=<%=selectedDate%>&merchantId=<%= merchantId %>",
		    "Edit Collection Details",
		    "toolbar=no," +
		    "scrollbars=no," +
		    "location=no," +
		    "statusbar=no," +
		    "menubar=no," +
		    "status=no," + 
		    "resizable=0," +
		    "width=800," +
		    "height=550," +
		    "left = 490," +
		    "top=300"
		    );
	    	popUpObj.focus(); 
		loadModalDiv();

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

	function handleFilterChange()
	{
		var filter = nidsGetElementValue("filterBy");
		if(filter == 'date')
		{
			nidsEnableControl("selectedDate", true);
			nidsEnableControl("merchantId", false);
		}
		else
		{
			nidsEnableControl("selectedDate", false);
			nidsEnableControl("merchantId", true);
		}
		
	}
	
		
</script>

  <div id="two">
    <div class="item">    
    	Get collection details for:
      <form action="/gm/servlet/collection" id="editForm">
      	<input type="hidden" name="action" value="getCollections" /> 
      	<input type="hidden" name="merchantName" id="merchantName"/>
      	<table cellspacing="5" cellpadding="5">
      		<tr>
      			<td>
      				<label><input type="radio" name="filterBy" id="filterBy" value="date" 
      						<% if(!isMerchantSelected) {%>checked <%} %> onclick="handleFilterChange();"> Selected Date:</label>
      			</td>
      			<td><input type="text" name="selectedDate" id="selectedDate" class="datepicker" value="<%= selectedDate %>" required></td>
      		</tr>
      		<tr>
      			<td>
      				<label>
      					<input 	type="radio" name="filterBy" id="filterBy" value="merchant"
      							<% if(merchantMap.size() == 0) {%> disabled 
      							<%} else if (isMerchantSelected) { %> checked <%} %>
      							 onclick="handleFilterChange();"
      							> Customer:</label>
      			</td>
      			<td>
      				<select name="merchantId" id="merchantId"
      						onChange="setMerchantName();" required>
      					<option value="" selected disabled>Select Customer Name</option>
      					<% for(Entry<Long, String> entry : merchantMap.entrySet())
      					   {
      					%>
      							<option value="<%= entry.getKey()%>"
      								<% if(String.valueOf(entry.getKey()).equals(merchantId)){ %> selected <%} %>>
      								<%= entry.getValue() %>
      							</option>
      					<% } %>
      				</select>
      			</td>
      		</tr>
      	</table>
      	<button type="submit" class="anyButton">Get Collection Details</button>
      </form>
  	</div>
  </div>

<% if(collectionBeanList != null) { %>
  <div id="one">
  	<% if(collectionBeanList.size() > 0) { %>
  		<h3><%= displayStr %></h3>
		<table id="borderTable">
			<thead>
			<tr>
				<th>S.No</th>
				<th>Customer</th>
				<th>Invoice No.</th>
				<th>Due Date /<br>Status</th>
				<th>Invoice Amount</th>
				<th>Paid Amount</th>
				<th>Payment Details</th>
				<th>Payment Date</th>
				<th>&nbsp;</th>
			</tr>
			</thead>
<tbody>
		<% 
			int serialNum = 0;
			for(CollectionBean bean : collectionBeanList) 
		   { 
				serialNum++;
				String id = String.valueOf(bean.getInvoiceNumber());
				int detailsNum = bean.getDetailsList().size();
				String rowspanStr = "";
				if(detailsNum > 1)
				{
					rowspanStr = " rowspan=\"" + detailsNum + "\"";
				}
				/* Format Amounts to display */
				String dispInvoiceAmount = " " + DisplayUtil.getDisplayAmount(bean.getInvoiceAmount());

		%>
			<tr>
				<td <%= rowspanStr%>><%= serialNum %></td>
				<td <%= rowspanStr%> nowrap><%=bean.getCustName()%><br/>Phone: <%=bean.getCustPhoneNumber()%></td>
				<td <%= rowspanStr%> align="right"><%= bean.getInvoiceNumber() %> </td>
				<td <%= rowspanStr%> nowrap><%= bean.getDueDateForDisplay() %></td>
				<td <%= rowspanStr%> align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span><%=dispInvoiceAmount %> </td>
				
				
			<% if(bean.getDetailsList().size() > 0) { 
				/* Format Amounts to display */
				String dispPaidAmount = " " + DisplayUtil.getDisplayAmount(bean.getDetailsList().get(0).getPaidAmount());
				String supplierName = "";
				if(bean.getDetailsList().get(0).getPaidAmount() > 0)
				{
					if(bean.getDetailsList().get(0).getSupplierCode() == 0)
					{
						supplierName = "Cash";
					}
					else if(bean.getDetailsList().get(0).getSupplierCode() == -1)
					{
						supplierName = "Adjustment";
					}
					else if(bean.getDetailsList().get(0).getSupplierCode() == -2)
					{
						supplierName = "RG";
					}
					else
					{
						supplierName = bean.getDetailsList().get(0).getSupplierName();
					}
				}
			%>
				<td align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span><%= dispPaidAmount%></td>
				<td nowrap><%= supplierName %>
				<td nowrap><%= bean.getDetailsList().get(0).getCollectionDateStr() %>
				
			<% } else { %>
				<td align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span> 0</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			<% } %>	
				<td <%= rowspanStr%>>
					<button class="editButton" 
							onclick="editCurrentRow('<%= bean.getInvoiceNumber() %>'); return false;">
							Edit
					</button>
				</td>
			</tr>
			<% if(bean.getDetailsList().size() > 1) 
			   {
					int i=-1;
					for(CollectionDetailBean detailBean : bean.getDetailsList())
					{
						i++;
						if(i == 0)
						{
							// skip first entry as it is already displayed
							continue;
						}
						
						/* Format Amounts to display */
						String dispPaidAmount = " " + DisplayUtil.getDisplayAmount(detailBean.getPaidAmount());
						
						String supplierName = "";
						if(detailBean.getPaidAmount() > 0)
						{
							if(detailBean.getSupplierCode() == 0)
							{
								supplierName = "Cash";
							}
							else if(detailBean.getSupplierCode() == -1)
							{
								supplierName = "Adjustment";
							}
							else if(detailBean.getSupplierCode() == -2)
							{
								supplierName = "RG";
							}
							else
							{
								supplierName = detailBean.getSupplierName();
							}
						}
			%>
						<tr>
							<td align="right"><span style="font-family: DejaVu Sans;">&#x20b9; </span><%= dispPaidAmount %></td>
							<td nowrap><%= supplierName %></td>
							<td nowrap><%= detailBean.getCollectionDateStr() %></td>
						</tr>
			<% 		}
			   }%>
		<% } %>
		</tbody>
		</table> 
	<% } else { %>
		<h3>No results found</h3>
	<% } %>
  </div>
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
 <% } %>
 <jsp:include page="footer.jsp" />