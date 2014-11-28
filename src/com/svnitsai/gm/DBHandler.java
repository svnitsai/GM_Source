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
		   String sql = "select CustCode, CustName FROM Customer WHERE CustType='Merchant' "  + 
				   		"ORDER BY CustName";
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   //Retrieve by column name
			   int id  = rs.getInt("CustCode");
			   String name = rs.getString("CustName");
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
   
   public static LinkedHashMap<Integer, CustomerBean> getSuppliers() 
   {
	   return getCustomers("Supplier");
   }
   
   public static LinkedHashMap<Integer, CustomerBean> getCustomers(String type) 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Integer, CustomerBean> resultMap = new LinkedHashMap<Integer, CustomerBean>();
	   
	   try 
	   {
		   // TODO: Change to read the bank data
		   String sql = "select C.CustCode, CustName, CustAddress1, CustAddress2, CustCity, CustState, CustCountry, CustContactNumber, "
				   		+ "CustBankId, CustBank, CustBankBranch, CustBankAccountType, CustBankAccountNumber " 
				   		+ "FROM Customer C, CustomerBanks CB "
				   		+ "WHERE CustType='" + type + "' AND C.CustCode = CB.CustCode "
				   		+ "ORDER BY CustName";
		   System.out.println(sql);
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   
		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   
			   CustomerBean bean = null;
			   
			   int id = rs.getInt("CustCode");
			   System.out.println("ID: " + id);
			   if(resultMap.containsKey(id) == false)
			   {
				   bean = new CustomerBean();
				   bean.setId(id);
				   bean.setName(rs.getString("CustName"));
				   bean.setAddress1(rs.getString("CustAddress1"));
				   bean.setAddress2(rs.getString("CustAddress2"));
				   bean.setPhoneNumber(rs.getInt("CustContactNumber"));
				   
				   
				   String city = rs.getString("CustCity");
				   String state = rs.getString("CustState");
				   String country = rs.getString("CustCountry");
				   String location = city;
				   if(location.length() > 0 && state.length() > 0)
				   {
					   location += ", " + state;
				   }
				   else if(state.length() > 0)
				   {
					   location = state;
				   }
				   
				   if(location.length() > 0 && country.length() > 0)
				   {
					   location += ", " + country;
				   }
				   else if(country.length() > 0)
				   {
					   location = country;
				   }
				   bean.setLocation(location);
				   resultMap.put(id, bean);
			   }
			   bean = resultMap.get(id);
			   
			   // Read the bank data
			   CustomerBankBean bankBean = new CustomerBankBean();
			   bankBean.setBankId(rs.getInt("CustBankId"));
			   bankBean.setBankName(rs.getString("CustBank"));
			   bankBean.setBranchName(rs.getString("CustBankBranch"));
			   bankBean.setAccountType(rs.getString("CustBankAccountType"));
			   bankBean.setAccountNumber(rs.getString("CustBankAccountNumber"));
			   bean.getBankAccountList().add(bankBean);
			   
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
   
   public static CollectionBean getCollectionInfo(Int invoiceId) 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Integer, CustomerBean> resultMap = new LinkedHashMap<Integer, CustomerBean>();
	   
	   
	   try 
	   {
		   // select and read collection data
		   String sql = "SELECT DC.PayCDueDate, DC.CustCode, DC.InvoiceAmount, DC.PayCStatus, DC.InvoiceReferenceNumber,"
				        +"DC.DeferredDate, DCD.PayCDate, DCD.CustBankId, DCD.SupplierCode, DCD.SupplierBankId,'"
				        +"DCD.PaidAmount, DCD.AccountLocationCode, DCD.LedgerPageNumber, c.CustName, C.CustContactNumber,"
				        +"CB.CustBank, SupplierBank = CB1.CustBank "
				        +"FROM DailyPayC DC join DailyPayCDetails DCD ON DC.PayCReferenceNumber = DCD.PayCReferenceNumber "
    			        +"join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant' "
				        +"left join CustomerBanks CB on CB.CustBankId = DCD.CustBankId and c.CustCode = cb.CustCode "
				        +"left join Customer s on s.custcode = dcd.suppliercode and s.custtype = 'Supplier' "
				        +"left join CustomerBanks CB1 on CB1.CustBankId = DCD.SupplierBankId and s.custcode = cb1.custcode "
				        +"left join Customer CO on CO.custcode = dcd.accountlocationcode and CO.custtype = 'Company' " 
				        +"where (DC.DeferredDate is not null and DC.DeferredDate <= '" + date + "'"
				        +"(DC.DeferredDate is null and DC.PayCDueDate <= '" + date + "' "
				   		+ "ORDER BY PayCDueDate";
		   
		   
		   System.out.println(sql);
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   
			   CollectionBean bean = null;
			   
			   int id = rs.getInt("InvoiceReferenceNumber");
			   System.out.println("ID: " + id);
			   if(resultMap.containsKey(id) == false)
			   {
				   CollectionBean DailyCollection = new CollectionBean();
				   DailyCollection.setId(id);
				   DailyCollection.setName(rs.getString("CustName"));
				   DailyCollection.setAddress1(rs.getString("CustAddress1"));
				   DailyCollection.setAddress2(rs.getString("CustAddress2"));
				   DailyCollection.setPhoneNumber(rs.getInt("CustContactNumber"));
				   
				   
				   String city = rs.getString("CustCity");
				   String state = rs.getString("CustState");
				   String country = rs.getString("CustCountry");
				   String location = city;
				   if(location.length() > 0 && state.length() > 0)
				   {
					   location += ", " + state;
				   }
				   else if(state.length() > 0)
				   {
					   location = state;
				   }
				   
				   if(location.length() > 0 && country.length() > 0)
				   {
					   location += ", " + country;
				   }
				   else if(country.length() > 0)
				   {
					   location = country;
				   }
				   bean.setLocation(location);
				   resultMap.put(id, bean);
			   }
			   bean = resultMap.get(id);
			   
			   // Read the bank data
			   CustomerBankBean bankBean = new CustomerBankBean();
			   bankBean.setBankId(rs.getInt("CustBankId"));
			   bankBean.setBankName(rs.getString("CustBank"));
			   bankBean.setBranchName(rs.getString("CustBankBranch"));
			   bankBean.setAccountType(rs.getString("CustBankAccountType"));
			   bankBean.setAccountNumber(rs.getString("CustBankAccountNumber"));
			   bean.getBankAccountList().add(bankBean);
			   
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

	   // TODO: Replace it to read from DB
	   if("1".equals(invoiceId))
	   {
		   CollectionBean bean1 = new CollectionBean();
			bean1.setCollectionId(1);
			bean1.setDueDate(Calendar.getInstance().getTime());
			bean1.setInvoiceAmount(150000);
			bean1.setInvoiceNumber(1);
			bean1.setPartyInfo("Trichy", "4567890123");
			bean1.setCustCode(31);
			bean1.setPartyName("Merchant 140000000");
			bean1.setStatus("Closed");
			
			CollectionDetailBean detailBean1 = new CollectionDetailBean();
			detailBean1.setCompanyId(101);
			detailBean1.setCompanyName("Garment Mantra");
			detailBean1.setPaidAmount(150000);
			detailBean1.setCollectionDate(Calendar.getInstance().getTime());
			detailBean1.setLedgerNumber(1);
			detailBean1.setPartyBankInfo("IndusInd Bank", "Bangalore");
			detailBean1.setSupplierId(200000000);
			detailBean1.setSupplierName("Supplier 200000000");
			detailBean1.setSupplierBankId(29);
			detailBean1.setSupplierBankName("State Bank of India");
			detailBean1.setSupplierBankBranch("Tiruppur Main");
			detailBean1.setSupplierAccountNumber("1111222233334444");
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
			bean2.setPartyInfo("Bangalore", "080-22752345");
			bean2.setCustCode(41);
			bean2.setPartyName("Merchant 150000000");
			bean2.setStatus("Payment Deferred");
			
			CollectionDetailBean detailBean2 = new CollectionDetailBean();
			detailBean2.setPaidAmount(150000);
			detailBean2.setCollectionDate(Calendar.getInstance().getTime());
			detailBean2.setLedgerNumber(2);
			detailBean2.setCompanyId(102);
			detailBean2.setCompanyName("SP Tex");
			detailBean2.setPartyBankInfo("Bank of India", "Bangalore");
			detailBean2.setSupplierId(200000000);
			detailBean2.setSupplierName("Supplier 200000000");
			detailBean2.setSupplierBankId(30);
			detailBean2.setSupplierBankName("HDFC Bank");
			detailBean2.setSupplierBankBranch("Tiruppur Main");
			detailBean2.setSupplierAccountNumber("1111222233334444");
			bean2.getDetailsList().add(detailBean2);
			
			CollectionDetailBean detailBean3 = new CollectionDetailBean();
			detailBean3.setPaidAmount(150000);
			detailBean3.setCollectionDate(Calendar.getInstance().getTime());
			detailBean3.setLedgerNumber(2);
			detailBean3.setCompanyId(101);
			detailBean3.setCompanyName("Garment Mantra");
			detailBean3.setPartyBankInfo("State Bank of India", "Bangalore");
			detailBean3.setSupplierId(51);
			detailBean3.setSupplierName("Supplier 200000000");
			detailBean3.setSupplierBankId(29);
			detailBean3.setSupplierBankName("State Bank of India");
			detailBean3.setSupplierBankBranch("Tiruppur Main");
			detailBean3.setSupplierAccountNumber("1111222233334444");
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
			bean3.setPartyInfo("Chennai", "044-456444544");
			bean3.setCustCode(11);
			bean3.setPartyName("Merchant 120000000");
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
		   String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=PayC";
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
   
   public static void main(String[] args)
   {
	   DBHandler.getSuppliers();
   }
} 
