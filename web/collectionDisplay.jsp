<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.svnitsai.gm.DBHandler" %>
<%@ page import="com.svnitsai.gm.CollectionBean" %>
<%@ page import="com.svnitsai.gm.CollectionDetailBean" %>
<%@ page import="com.svnitsai.gm.util.display.DisplayUtil"%>
<%@ page import="com.svnitsai.gm.CollectionBean.DeferredReasonEnum" %>
<jsp:include page="header.jsp?hideHeader=true" />
<script>
	$(function() 
	{
		$( ".editAgentButton" ).button({
			icons: {
				primary: "ui-icon-pencil"
			},
			label: "&nbsp;&nbsp;Edit Agent"
		});

		$.datepicker.setDefaults({dateFormat:"dd/mm/yy"});

		$('#collectionsTable').dataTable({
			  "lengthChange": false,
			  "pageLength": 200,
			  "processing": true,
			  "ordering": false
		});

		$(window).load(function() {
			// Animate loader off screen
			$(".loadingImage").fadeOut("slow");;
		});
	});	
</script>
<%
	Collection<CollectionBean> collectionBeanList = (Collection<CollectionBean>) request.getAttribute("collectionData");
	String action = (String) request.getAttribute("action");
	String selectedDate = (String) request.getAttribute("selectedDate");
	String selectedCustGroup = (String) request.getAttribute("selectedCustGroup");
	String merchantId = (String) request.getAttribute("merchantId");
	String merchantName = (String) request.getAttribute("merchantName");
	String agentId = (String) request.getAttribute("agentId");
	String agentName = (String) request.getAttribute("agentName");
	
	LinkedHashMap<Long, String> merchantMap = DBHandler.getMerchants();
	LinkedHashMap<Long, String> agentMap = DBHandler.getCustomerIdMap("Agent");
	
	boolean isMerchantSelected = false;
	boolean isAgentSelected = false;
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
	else if(agentName != null  && agentName.length() > 0)
	{
		displayStr = "Collection details for " + agentName;
		isAgentSelected = true;
	}
	
	
	if(selectedDate == null)
	{
		selectedDate = "";
	}
	
	if(selectedCustGroup == null)
	{
		selectedCustGroup = "All";
	}
	
	if(merchantId == null)
	{
		merchantId = "";
	}

	if(agentId == null)
	{
		agentId = "";
	}

	if(agentName == null)
	{
		agentName = "";
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

	function saveChanges(suffix, close)
	{
		nidsSetElementValue("action", "saveCollectionFromList");
		nidsSetElementValue("collectionRefId", nidsGetElementValue("collectionRefId" + suffix));
		nidsSetElementValue("deferredDate", nidsGetElementValue("deferredDate" + suffix));
		nidsSetElementValue("deferredReason", nidsGetElementValue("deferredReason" + suffix));
		nidsSetElementValue("deferredReasonChoice", nidsGetElementValue("deferredReasonChoice" + suffix));
		nidsSetElementValue("colLedgerNo", nidsGetElementValue("colLedgerNo" + suffix));
		nidsSetElementValue("remarks", nidsGetElementValue("remarks" + suffix));
		nidsSetElementValue("status", close);
		nidsSubmitDocumentForm(false);
        
	}

	function setMerchantName()
	{
		var selectedMerchant = $( "#merchantId option:selected" ).text().trim();
		nidsSetElementValue("merchantName", selectedMerchant);
	}

	function setAgentName()
	{
		var selectedAgent = $( "#agentId option:selected" ).text().trim();
		nidsSetElementValue("agentName", selectedAgent);
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
	
	function editAgent()
	{
		popUpObj=window.open("/gm/web/editCollection.jsp?agentName=<%=agentName%>&agentId=<%=agentId%>",
			    "Change Agent Payments",
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
			nidsEnableControl("selectedCustGroup", true);
			nidsEnableControl("merchantId", false);
			nidsEnableControl("agentId", false);
		}
		else if(filter == 'merchant')
		{
			nidsEnableControl("selectedDate", false);
			nidsEnableControl("selectedCustGroup", false);
			nidsEnableControl("merchantId", true);
			nidsEnableControl("agentId", false);
		}
		else
		{
			nidsEnableControl("selectedDate", false);
			nidsEnableControl("selectedCustGroup", false);
			nidsEnableControl("merchantId", false);
			nidsEnableControl("agentId", true);
		}
	}

	function deferredDateSet(suffix)
	{
		var selectedDate = nidsGetElementValue('deferredDate' + suffix);
		if(selectedDate != '')
		{
			// show the text field to type the reason
			nidsEnableControl('deferredReasonChoice' + suffix, true);
			deferredReasonSet(suffix);
		}
		else
		{
			nidsEnableControl('deferredReasonChoice' + suffix, false);
			nidsHideElement('deferredReason' + suffix);
		}
	}
	
	function deferredReasonSet(suffix)
	{
		var selectedReason = nidsGetSelectedOptionValue('deferredReasonChoice' + suffix);
		if(selectedReason == '<%= DeferredReasonEnum.OTHER.getId()%>')
		{
			// show the text field to type the reason
			nidsShowElement('deferredReason' + suffix);
		}
		else
		{
			nidsHideElement('deferredReason' + suffix);
		}
	}
	
</script>

  <div id="two">
    <div class="item">    
    	Get collection details for:
      <form action="/gm/servlet/collection" id="editForm">
      	<input type="hidden" name="action" id="action" value="getCollections" /> 
      	<input type="hidden" name="merchantName" id="merchantName"/>
      	<input type="hidden" name="agentName" id="agentName" value="<%=agentName%>"/>
      	<table cellspacing="5" cellpadding="5">
      		<tr>
      			<td>
      				<label><input type="radio" name="filterBy" id="filterBy" value="date" 
      						<% if(!isMerchantSelected) {%>checked <%} %> onclick="handleFilterChange();"> Selected Date:</label>
      			</td>
      			<td><input type="text" name="selectedDate" id="selectedDate" class="datepicker" value="<%= selectedDate %>" required>
      			      			&nbsp;Customers: 
      				<select name="selectedCustGroup" id="selectedCustGroup">
      					<option value="All" selected>All</option>
      					<option value="a-m,A-M,0-9" <% if(selectedCustGroup.equals("a-m,A-M,0-9")){ %>selected<%}%>>A-M</option>
      					<option value="n-z,N-Z" <% if(selectedCustGroup.equals("n-z,N-Z")){ %>selected<%}%>>N-Z</option>
      					
      				</select>
      			</td>
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

      		<tr>
      			<td>
      				<label>
      					<input 	type="radio" name="filterBy" id="filterBy" value="agent"
      							<% if(agentMap.size() == 0) {%> disabled 
      							<%} else if (isAgentSelected) { %> checked <%} %>
      							 onclick="handleFilterChange();"
      							> Agency:</label>
      			</td>
      			<td>
      				<select name="agentId" id="agentId"
      						onChange="setAgentName();" required>
      					<option value="" selected disabled>Select Agency Name</option>
      					<% for(Entry<Long, String> entry : agentMap.entrySet())
      					   {
      					%>
      							<option value="<%= entry.getKey()%>"
      								<% if(String.valueOf(entry.getKey()).equals(agentId)){ %> selected <%} %>>
      								<%= entry.getValue() %>
      							</option>
      					<% } %>
      				</select>
      			</td>
      		</tr>

      	</table>
      	<button type="submit" class="anyButton">Get Collection Details</button>
      	
      	<!--  These are set only when Save or Close is pressed for an individual entry.
      			The changes are saved and the page is reloaded for the selected date, etc.
      	-->
   		<input type="hidden" name="collectionRefId" id="collectionRefId"/>
		<input type="hidden" name="deferredDate" id="deferredDate"/>
   		<input type="hidden" name="deferredReason" id="deferredReason"/>
   		<input type="hidden" name="deferredReasonChoice" id="deferredReasonChoice"/>
   		<input type="hidden" name="colLedgerNo" id="colLedgerNo"/>
   		<input type="hidden" name="remarks" id="remarks"/>
   		<input type="hidden" name="status" id="status"/>
   		<input type="hidden" name="doRedirect" value="true" />
      </form>
  	</div>
  </div>

<% if(collectionBeanList != null) { %>
  <div id="one">
  	<% if(collectionBeanList.size() > 0) { %>
  		<h3>
  			<%= displayStr %>
  			<% if(isAgentSelected) { %>
  				<img src="/gm/web/css/images/blank.gif" width="50" height="1">
  				<button class="editAgentButton"
  						onclick="editAgent()"
  						>Edit Agent Payment
  				</button>
  			<% } %>
  		</h3>
		<table id="collectionsTable" name="collectionsTable" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>S.No</th>
				<th>Customer</th>
				<th>Invoice No.</th>
				<th>Due Date /<br>Status</th>
				<th>Deferred<br>Date</th>
				<th>Deferred<br>Reason</th>
				<th>Remarks</th>
				<th>LF</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
		<% 
			int serialNum = 0;
			for(CollectionBean bean : collectionBeanList) 
		   { 
				serialNum++;
				
				/* Format customer info display */
				String customerInfo = "";
				if(bean.getAgentCode() > 0)
				{
					String agencyName = agentMap.get(bean.getAgentCode());
					if(agencyName != null)
					{
						customerInfo += "Agency: " + agencyName + "<br/>";
					}
				}
				customerInfo += bean.getCustName() + "<br/>Phone: " + bean.getCustPhoneNumber();
				
				String deferredDate = "";
				if(bean.getDeferredDate() != null)
				{
					deferredDate = new SimpleDateFormat("dd/MM/yyyy").format(bean.getDeferredDate());
				}
				

		%>
			<tr>
				<td><%= serialNum %></td>
				<td><%= customerInfo %></td>
				<td align="right"><%= bean.getInvoiceNumber() %> </td>
				<td nowrap><%= bean.getDueDateForDisplay() %></td>
				<td>
					<input type="text" class="datepicker" size="10"
								name="deferredDate_<%= serialNum %>" id="deferredDate_<%= serialNum %>" 
								value="<%= deferredDate %>"
								onchange="deferredDateSet('_<%= serialNum %>')">
				</td>
				<td>
					<select name="deferredReasonChoice_<%= serialNum %>" id="deferredReasonChoice_<%= serialNum %>" 
							onchange="deferredReasonSet('_<%= serialNum %>')"
							<% if(deferredDate.length() == 0) { %> disabled <%} %>>
						<option value="0" selected disabled>Select</option>
						<option value="<%= DeferredReasonEnum.NO_REPLY.getId() %>" 
							<% if(bean.getDeferredReasonChoice() == DeferredReasonEnum.NO_REPLY.getId()) { %> selected <%} %>>
								<%= DeferredReasonEnum.NO_REPLY.getReason() %>
						</option>
						<option value="<%= DeferredReasonEnum.NO_CONTACT.getId() %>"
							<% if(bean.getDeferredReasonChoice() == DeferredReasonEnum.NO_CONTACT.getId()) { %> selected <%} %>>
								<%= DeferredReasonEnum.NO_CONTACT.getReason() %>
						</option>
						<option value="<%= DeferredReasonEnum.NEXT_DAY.getId() %>"
							<% if(bean.getDeferredReasonChoice() == DeferredReasonEnum.NEXT_DAY.getId()) { %> selected <%} %>>
								<%= DeferredReasonEnum.NEXT_DAY.getReason() %>
						</option>
						<option value="<%= DeferredReasonEnum.NOT_REACHABLE.getId() %>"
							<% if(bean.getDeferredReasonChoice() == DeferredReasonEnum.NOT_REACHABLE.getId()) { %> selected <%} %>>
								<%= DeferredReasonEnum.NOT_REACHABLE.getReason() %>
						</option>
						<option value="<%= DeferredReasonEnum.OTHER.getId() %>"
							<% if(bean.getDeferredReasonChoice() == DeferredReasonEnum.OTHER.getId()) { %> selected <%} %>>
								<%= DeferredReasonEnum.OTHER.getReason() %>
						</option>
					</select>
					<br>
					<textarea name="deferredReason_<%= serialNum %>" id="deferredReason_<%= serialNum %>" rows="1" cols="13" 
							<% if(bean.getDeferredReasonChoice() != DeferredReasonEnum.OTHER.getId()) { %> style="display:none" <% } %>
						><%= (bean.getDeferredReason() != null) ? bean.getDeferredReason() : ""%></textarea>
				</td>
				<td>
					<textarea rows="3" cols="25" name="remarks_<%= serialNum %>" id="remarks_<%= serialNum %>"><%= bean.getRemarks()%></textarea>
				</td>
				<td>
					<input name="colLedgerNo_<%= serialNum %>" id="colLedgerNo_<%= serialNum %>" value="<%= bean.getLedgerNumber() %>" size="3">
				</td>
				<td nowrap width="100%" align="center">
					<input type="hidden" name="status_<%= serialNum %>" id="status_<%= serialNum %>" value="<%= bean.getStatus() %>">
					<input type="hidden" name="collectionRefId_<%= serialNum %>" id="collectionRefId_<%= serialNum %>" value="<%= bean.getCollectionId() %>"/>
					<button type="button" class="saveButton" onClick="saveChanges('_<%= serialNum %>');">&nbsp;&nbsp;Save</button>
					&nbsp;
					<button type="button" class="cancelButton" onClick="saveChanges('_<%= serialNum %>', 'CLOSED');">&nbsp;&nbsp;Close</button>
				</td>
			</tr>
		<% } %>
		</tbody>
		</table> 
	<% } else { %>
		<h3>No results found</h3>
	<% } %>
  </div>
  <div id="overlay" class="ui-widget-overlay ui-front" style="z-index: 100; display:none"></div>
  <div class="loadingImage"></div>
 <% } %>
 <jsp:include page="footer.jsp" />