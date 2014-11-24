package com.svnitsai.gm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class DBHandler 
{
   public static LinkedHashMap<Integer, String> getMerchants() 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Integer, String> resultMap = new LinkedHashMap<Integer, String>();
	   
	   try 
	   {
		   String sql = "SELECT PartyId, PartyName FROM OriginalBill ORDER BY PartyName";
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   //Retrieve by column name
			   int id  = rs.getInt("PartyId");
			   String name = rs.getString("PartyName");
			   resultMap.put(id, name);
		   }
	   } 
	   catch (Exception e) 
	   {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   } 
	   finally 
	   {
		   closeDBObjects(conn, stmt, rs);
	   }
	   
	   return resultMap;
   }
   
   public static LinkedHashMap<Integer, SupplierBean> getSuppliers() 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Integer, SupplierBean> resultMap = new LinkedHashMap<Integer, SupplierBean>();
	   
	   try 
	   {
		   // TODO: Change to read the bank data
		   String sql = "SELECT SupplierId, SupplierName, SupplierAddress1, SupplierAddress2, SupplierLocation, SupplierContactNumber " + 
				   		"FROM Supplier ORDER BY SupplierName";
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   SupplierBean bean = new SupplierBean();
			   
			   //Retrieve by column name
			   bean.setId(rs.getInt("SupplierId"));
			   bean.setName(rs.getString("SupplierName"));
			   bean.setAddress1(rs.getString("SupplierAddress1"));
			   bean.setAddress2(rs.getString("SupplierAddress2"));
			   bean.setLocation(rs.getString("SupplierLocation"));
			   bean.setPhoneNumber(rs.getInt("SupplierContactNumber"));
			   
			   // TODO: Read the bank data
			   
			   resultMap.put(bean.getId(), bean);
		   }
	   } 
	   catch (Exception e) 
	   {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   } 
	   finally 
	   {
		   closeDBObjects(conn, stmt, rs);
	   }
	   
	   return resultMap;
   }
   
   public static CollectionBean getCollectionInfo(String invoiceId) 
   {
	   // TODO: Replace it to read from DB
	   if("1".equals(invoiceId))
	   {
		   CollectionBean bean1 = new CollectionBean();
			bean1.setCollectionId(1);
			bean1.setDueDate(Calendar.getInstance().getTime());
			bean1.setInvoiceAmount(150000);
			bean1.setInvoiceNumber(1);
			bean1.setPartyContact("1111111111");
			bean1.setPartyId(1);
			bean1.setPartyName("Merchant 1");
			bean1.setStatus("Closed");
			
			CollectionDetailBean detailBean1 = new CollectionDetailBean();
			detailBean1.setCollectionAmount(150000);
			detailBean1.setCollectionDate(Calendar.getInstance().getTime());
			detailBean1.setLedgerNumber(1);
			detailBean1.setPartyBank("IndusInd Bank");
			detailBean1.setPartyBankBranch("Bangalore");
			detailBean1.setSupplierAccountNum("234234234234");
			detailBean1.setSupplierBank("State Bank of India");
			detailBean1.setSupplierName("Gomathi Textiles");
			bean1.getDetailsList().add(detailBean1);
			
			return bean1;
	   }
	   else if("2".equals(invoiceId))
	   {
		   CollectionBean bean2 = new CollectionBean();
			bean2.setCollectionId(2);
			bean2.setDueDate(Calendar.getInstance().getTime());
			bean2.setInvoiceAmount(500000);
			bean2.setInvoiceNumber(2);
			bean2.setPartyContact("222-222-2222");
			bean2.setPartyId(2);
			bean2.setPartyName("Merchant 2");
			bean2.setStatus("Payment Deferred");
			
			CollectionDetailBean detailBean2 = new CollectionDetailBean();
			detailBean2.setCollectionAmount(150000);
			detailBean2.setCollectionDate(Calendar.getInstance().getTime());
			detailBean2.setLedgerNumber(2);
			detailBean2.setPartyBank("CitiBank");
			detailBean2.setPartyBankBranch("Delhi");
			detailBean2.setSupplierAccountNum("4523223");
			detailBean2.setSupplierBank("Bank of India");
			detailBean2.setSupplierName("ABC Traders");
			bean2.getDetailsList().add(detailBean2);
			
			CollectionDetailBean detailBean3 = new CollectionDetailBean();
			detailBean3.setCollectionAmount(150000);
			detailBean3.setCollectionDate(Calendar.getInstance().getTime());
			detailBean3.setLedgerNumber(2);
			detailBean3.setPartyBank("CitiBank");
			detailBean3.setPartyBankBranch("Delhi");
			detailBean3.setSupplierAccountNum("234234234234");
			detailBean3.setSupplierBank("State Bank of India");
			detailBean3.setSupplierName("Gomathi Textiles");
			bean2.getDetailsList().add(detailBean3);
			
			return bean2;
	   }
	   else if("3".equals(invoiceId))
	   {
		   CollectionBean bean3 = new CollectionBean();
			bean3.setCollectionId(3);
			bean3.setDueDate(Calendar.getInstance().getTime());
			bean3.setInvoiceAmount(50000);
			bean3.setInvoiceNumber(3);
			bean3.setPartyContact("33333333");
			bean3.setPartyId(3);
			bean3.setPartyName("Merchant 3");
			bean3.setStatus("Open");
			return bean3;
	   }
		   
	   
	   return null;
   }
 
   public static Connection getConnection()
   {
	   Connection conn = null;
       
	   try 
	   {
		   String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		   String username="admin";
		   String password="svnadmin";
		   Class.forName(driver).newInstance();
		   String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=Local1";
		   conn = DriverManager.getConnection(dbURL, username, password);
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }
	   
	   return conn;
   }
   
   public static void closeDBObjects(Connection conn,
		   							 Statement stmt,
		   							 ResultSet rs)
   {
	   try 
	   {
		   	if(rs != null && !rs.isClosed())
		   	{
			   rs.close();
		   	}
		   	
		   	if(stmt != null && !stmt.isClosed())
		   	{
		   		stmt.close();
		   	}
		   	
       		if (conn != null && !conn.isClosed()) 
       	 	{
                conn.close();
            }
       	 	
       	 	
        } 
	    catch (SQLException ex) {
            ex.printStackTrace();
        }
   }
} 
