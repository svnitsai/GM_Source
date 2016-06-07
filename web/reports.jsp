<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.svnitsai.gm.ReportGeneratorServlet.ReportType" %>
<jsp:include page="header.jsp?hideHeader=true" />
<script>
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
</script>
  <div id="two">
    <div class="item">    
      <form action="/gm/servlet/report" id="editForm" target="_blank">
      	<table cellspacing="5" cellpadding="5">
      		<tr>
      			<td>
      				<label>Report To Generate:</label>
      			</td>
      			<td>
      				<select name="type" id="type" required>
      					<option value="" selected disabled>Select Report</option>
      					<option value="<%= ReportType.PayableMorning.name()%>">
      						<%= ReportType.PayableMorning.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsMorning.name()%>">
      						<%= ReportType.CollectionsMorning.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsEvening.name()%>">
      						<%= ReportType.CollectionsEvening.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.SupplierEvening.name()%>">
      						<%= ReportType.SupplierEvening.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.LedgerReport.name()%>">
      						<%= ReportType.LedgerReport.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.AgencyMonthlyReport.name()%>">
      						<%= ReportType.AgencyMonthlyReport.getDisplayName() %>
      					</option>
      				</select>
      			</td>
      		</tr>
      		<tr>
      			<td>
      				<label>Select Date:</label>
      			</td>
      			<td><input type="text" name="date" class="datepicker" required></td>
      		</tr>
      	</table>
      	<button type="submit" class="anyButton">Generate Report</button>
      </form>
  	</div>
  </div>
 <jsp:include page="footer.jsp" />