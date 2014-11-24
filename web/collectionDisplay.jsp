<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
<jsp:include page="header.jsp?hideHeader=true" />
<script>
	function setMerchantName()
	{
		var selectedMerchant = $( "#merchantId option:selected" ).text();
		nidsSetElementValue("merchantName", selectedMerchant);
	}
        
	function editCurrentRow(invoiceId) {
    		$("#editRowDialog").dialog({
        		title: 'Edit Collection Details',
        		autoOpen: false,
        		resizable: true,
        		height: 500,
        		width: 600,
        		show: { effect: 'drop', direction: "up" },
        		modal: true,
        		draggable: true,
        		open: function (event, ui) {
            			$(this).load('/gm/web/editCollection.jsp', { id: invoiceId, isEdit: true }); 
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
<%
	LinkedList<CollectionBean> collectionBeanList = (LinkedList<CollectionBean>) request.getAttribute("collectionData");
	String displayStr = (String) request.getAttribute("displayStr");
	
	LinkedHashMap<Integer, String> merchantMap = DBHandler.getMerchants();
	
%>
  <div id="two">
    <div class="item">    
    	Get collection details for:
      <form action="/gm/servlet/collection">
      	<input type="hidden" name="action" value="getCollections" /> 
      	<input type="hidden" name="merchantName" id="merchantName"/>
      	<table cellspacing="5" cellpadding="5">
      		<tr>
      			<td>
      				<label><input type="radio" name="filterBy" value="date" checked> Selected Date:</label>
      			</td>
      			<td><input type="text" name="selectedDate" class="datepicker"></td>
      		</tr>
      		<tr>
      			<td>
      				<label>
      					<input 	type="radio" name="filterBy" value="merchant"
      							<% if(merchantMap.size() == 0) {%> disabled <%} %>
      							> Merchant:</label>
      			</td>
      			<td>
      				<select name="merchantId" id="merchantId"
      						onChange="setMerchantName();">
      					<option value="" selected disabled>Select Merchant Name</option>
      					<% for(Entry<Integer, String> entry : merchantMap.entrySet())
      					   {
      					%>
      							<option value="<%= entry.getKey()%>">
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
				<th>Merchant Name</th>
				<th>Invoice Number</th>
				<th>Status</th>
				<th>Invoice Amount</th>
				<th>Collection Amount</th>
				<th>Paid To</th>
				<th>Ledger Page</th>
				<th>Collection Date</th>
				<th>Deferred Date</th>
				<th>&nbsp;</th>
			</tr>
			</thead>
<tbody>
		<% for(CollectionBean bean : collectionBeanList) 
		   { 
				String id = String.valueOf(bean.getInvoiceNumber());
				int detailsNum = bean.getDetailsList().size();
				String colspanStr = "";
				if(detailsNum > 1)
				{
					detailsNum++;
					colspanStr = " rowspan=\"" + detailsNum + "\"";
				}
		%>
			<tr>
				<td <%= colspanStr%>><%= bean.getPartyName() %></td>
				<td <%= colspanStr%>><%= bean.getInvoiceNumber() %> </td>
				<td <%= colspanStr%>><%= bean.getStatus() %></td>
				<td <%= colspanStr%>><%= bean.getInvoiceAmount() %></td>
				
			<% if(bean.getDetailsList().size() == 1) { %>
				<td><%= bean.getTotalCollectionAmount()%></td>
				<td><%= bean.getDetailsList().get(0).getSupplierAccountInfo() %>
				<td><%= bean.getDetailsList().get(0).getLedgerNumber() %>
				<td><%= bean.getDetailsList().get(0).getCollectionDateStr() %>
				<td><%= bean.getDetailsList().get(0).getDeferedDateStr() %>
			<% } else if(bean.getDetailsList().size() > 1) { %>
				<td colspan="5">Pending Amount: <%= (bean.getInvoiceAmount() - bean.getTotalCollectionAmount())%></td>
			<% } else { %>
				<td colspan="5">0</td>
			<% } %>	
				<td><button class="editButton" onclick="editCurrentRow('<%= bean.getInvoiceNumber() %>'); return false;">Edit</button></td>
			</tr>
			<% if(bean.getDetailsList().size() > 1) 
			   {
					for(CollectionDetailBean detailBean : bean.getDetailsList())
					{
			%>
						<tr>
							<td><%= detailBean.getCollectionAmount() %></td>
							<td><%= detailBean.getSupplierAccountInfo() %></td>
							<td><%= detailBean.getLedgerNumber() %></td>
							<td><%= detailBean.getCollectionDateStr() %></td>
							<td><%= detailBean.getDeferedDateStr() %></td>
							<td>&nbsp;</td>
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