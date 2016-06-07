<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.data.*" %>
<%@ page import="com.svnitsai.gm.*" %>

<%
String basePath = request.getRealPath("/") + "\\";
InvoiceReportFill.createReport(basePath);
String redirectURL = "http://localhost:8080/gm/GMInvoiceReport.pdf";
%>

<html>

<% if(redirectURL.length() > 0) { %>
<head>
<meta http-equiv="Refresh" CONTENT="0;URL=<%= redirectURL %>"/>
</head>
<% } else { %>
<body>
 <h3>Failed to create the report</h3>
</body>
<% } %>
</html>