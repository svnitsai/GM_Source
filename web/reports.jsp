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
      					<option value="<%= ReportType.CollectionsMorningAll.name()%>">
      						<%= ReportType.CollectionsMorningAll.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsMorningAM.name()%>">
      						<%= ReportType.CollectionsMorningAM.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsMorningNZ.name()%>">
      						<%= ReportType.CollectionsMorningNZ.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsEveningAll.name()%>">
      						<%= ReportType.CollectionsEveningAll.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsEveningAM.name()%>">
      						<%= ReportType.CollectionsEveningAM.getDisplayName() %>
      					</option>
      					<option value="<%= ReportType.CollectionsEveningNZ.name()%>">
      						<%= ReportType.CollectionsEveningNZ.getDisplayName() %>
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