package com.svnitsai.gm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class InvoiceReportFill 
{
   public static void createReport(String basePath) 
   {
	   String sourceFileName = basePath + "jrxml\\GMInvoice.jasper";
	   Connection conn = null;
         
	   try 
	   {
		   String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		   String username="admin";
		   String password="svnadmin";
		   Class.forName(driver).newInstance();
    	  String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=Local1";
          conn = DriverManager.getConnection(dbURL, username, password);
          
          Map parameters = new HashMap();
          String printFileName = JasperFillManager.fillReportToFile(sourceFileName,
        		  					parameters, conn);
          if (printFileName != null) 
          {
            /**
             * 1- export to PDF
             */
            JasperExportManager.exportReportToPdfFile(printFileName,
                  basePath + "GMInvoiceReport.pdf");
          }
	   } 
	   catch (JRException e) 
	   {
         e.printStackTrace();
	   }  
	   catch (SQLException ex) 
	   {
          ex.printStackTrace();
	   } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally 
	   {
          try {
         	 if (conn != null && !conn.isClosed()) {
                  conn.close();
              }
          } catch (SQLException ex) {
              ex.printStackTrace();
          }
	   }
   }
 
   public static void main(String[] args)
   {
	   InvoiceReportFill.createReport("");
   }
} 
