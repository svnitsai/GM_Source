<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
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
	function setMerchantName()
	{
		var selectedMerchant = $( "#merchantId option:selected" ).text().trim();
		nidsSetElementValue("merchantName", selectedMerchant);
	}
        
	function editCurrentRow(invoiceId) {
    		$("#editRowDialog").dialog({
        		title: 'Edit Collection Details',
        		autoOpen: false,
        		resizable: true,
        		height: 600,
        		width: 800,
        		show: { effect: 'drop', direction: "up" },
        		modal: true,
        		draggable: true,
        		open: function (event, ui) {
            			$(this).load('/gm/web/editCollection.jsp', { id: invoiceId, date: '<%=selectedDate%>', merchantId: '<%= merchantId %>' }); 
        		},
        		close: function (event, ui) {
            			$(this).dialog('close');
				$(this).remove();
        		},
			buttons: [{ 
      				text: "Save", 
      				click: function() { 
         				saveChanges(); 
      				}
   			   }, { 
      				text: "Cancel", 
      				click: function() { 
         				$("#editRowDialog").dialog( 'close' ); 
				}
   			}]
    		});

		function saveChanges() 
		{
			$("#editForm").submit();
			$("#editRowDialog").dialog( 'close' );
			return true;
		}

    		$("#editRowDialog").dialog('open');

    		return false;
	}

	
		
</script>

  <div id="two">
    <div class="item">    
    	Get collection details for:
      <form action="/gm/servlet/collection">
      	<input type="hidden" name="action" value="getCollections" /> 
      	<input type="hidden" name="merchantName" id="merchantName"/>
      	<table cellspacing="5" cellpadding="5">
      		<tr>
      			<td>
      				<label><input type="radio" name="filterBy" value="date" 
      						<% if(!isMerchantSelected) {%>checked <%} %>> Selected Date:</label>
      			</td>
      			<td><input type="text" name="selectedDate" class="datepicker" value="<%= selectedDate %>"></td>
      		</tr>
      		<tr>
      			<td>
      				<label>
      					<input 	type="radio" name="filterBy" value="merchant"
      							<% if(merchantMap.size() == 0) {%> disabled 
      							<%} else if (isMerchantSelected) { %> checked <%} %>
      							> Customer:</label>
      			</td>
      			<td>
      				<select name="merchantId" id="merchantId"
      						onChange="setMerchantName();">
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
      	<input type="submit" value="Get Collection Details" />
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
				<th>Paid To</th>
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
		%>
			<tr>
				<td <%= rowspanStr%>><%= serialNum %></td>
				<td <%= rowspanStr%> nowrap><%=bean.getCustName()%><br/>Phone: <%=bean.getCustPhoneNumber()%></td>
				<td <%= rowspanStr%>><%= bean.getInvoiceNumber() %> </td>
				<td <%= rowspanStr%> nowrap><%= bean.getDueDateForDisplay() %><br/><%= bean.getStatus() %></td>
				<td <%= rowspanStr%>><%= bean.getInvoiceAmount() %></td>
				
				
			<% if(bean.getDetailsList().size() > 0) { %>
				<td><%= bean.getDetailsList().get(0).getPaidAmount()%></td>
				<td nowrap><%= bean.getDetailsList().get(0).getSupplierName() %>
				<td nowrap><%= bean.getDetailsList().get(0).getCollectionDateStr() %>
				
			<% } else { %>
				<td>0</td>
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
			%>
						<tr>
							<td><%= detailBean.getPaidAmount() %></td>
							<td nowrap><%= detailBean.getSupplierName() %></td>
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
  <div id="editRowDialog"></div>
 <% } %>
 <jsp:include page="footer.jsp" />