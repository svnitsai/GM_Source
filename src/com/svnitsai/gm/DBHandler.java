package com.svnitsai.gm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Date;


public class DBHandler 
{
	public static LinkedHashMap<Long, String> getMerchants() 
	{
		return getCustomerIdMap("Merchant");
	}
   
	public static LinkedHashMap<Long, String> getCustomerIdMap(String type) 
    {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Long, String> resultMap = new LinkedHashMap<Long, String>();
	   
	   try 
	   {
		   String sql = "select CustCode, CustName FROM Customer WHERE CustType='" + type + "' "  + 
				   		"ORDER BY CustName";
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   //Retrieve by column name
			   long id  = rs.getLong("CustCode");
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
   
   public static LinkedHashMap<Long, CustomerBean> getSuppliers() 
   {
	   return getCustomers("Supplier");
   }
   
   public static LinkedHashMap<Long, CustomerBean> getCustomers(String type) 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Long, CustomerBean> resultMap = new LinkedHashMap<Long, CustomerBean>();
	   
	   try 
	   {
		   // TODO: Change to read the bank data
		   String sql = "select C.CustCode, CustName, CustAddress1, CustAddress2, CustCity, CustState, CustCountry, CustContactNumber, "
				   		+ "CustBankId, CustBank, CustBankBranch, CustBankAccountType, CustBankAccountNumber " 
				   		+ "FROM Customer C, CustomerBanks CB "
				   		+ "WHERE CustType='" + type + "' AND C.CustCode = CB.CustCode "
				   		+ "ORDER BY CustName";
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);

		    
		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   
			   CustomerBean bean = null;
			   
			   long id = rs.getLong("CustCode");
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
			   bankBean.setBankId(rs.getLong("CustBankId"));
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
   
   public static CollectionBean getCollectionInfo(String invoiceNumber) 
   {
	   Collection<CollectionBean> list = getCollections(null, null, invoiceNumber, false);
	   if(list != null && list.size() > 0)
	   {
		   return (CollectionBean) list.iterator().next();
	   }
	   return null;
   }
   
   public static Collection<CollectionBean> getCollections(String collectionDate,
		   													String merchantId,
		   													String invoiceNumber,
		   													boolean showDeleted) 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   // Preserves the sorted order in which data was added to the map
	   LinkedHashMap<Long, CollectionBean> resultMap = new LinkedHashMap<Long, CollectionBean>();
	   
	   try 
	   {
		   // select and read collection data
		   String sql = "SELECT DC.PayCReferenceNumber, DC.PayCDueDate, DC.CustCode, DC.InvoiceAmount, "
		   		        +"DC.PayCStatus, DC.InvoiceReferenceNumber, DCD.PayCReferenceSubNumber, "
				        +"DC.DeferredDate, DCD.PayCDate, DCD.CustBankName, DCD.SupplierCode, DCD.SupplierBankId, "
				        +"DCD.PaidAmount, DCD.AccountLocationCode, DCD.LedgerPageNumber, "
				        +"C.CustName, C.CustContactNumber, C.CustCity, "
				        +"CO.CustName AS CompanyName, s.CustName AS SupplierName, CB1.CustBank AS SupplierBank, "
				        +"CB1.CustBankBranch AS SupplierBankBranch, CB1.CustBankAccountNumber AS SupplierAcctNum "
				        +"FROM DailyPayC DC "
						+"join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant' "
						+"left join DailyPayCDetails DCD ON DC.PayCReferenceNumber = DCD.PayCReferenceNumber ";
		   
		   if(!showDeleted)
		   {
			   sql+="AND DCD.Deleted!=1 ";
		   }
			        
		   sql+="left join Customer s on s.custcode = dcd.suppliercode and s.custtype = 'Supplier' "
			   +"left join CustomerBanks CB1 on CB1.CustBankId = DCD.SupplierBankId and s.custcode = cb1.custcode "
			   +"left join Customer CO on CO.custcode = dcd.accountlocationcode and CO.custtype = 'Company' ";
		   
		   boolean whereClauseAdded = false;
		   if(collectionDate != null && collectionDate.length() > 0)
		   {
			   // Date is in format dd/mm/yyyy
			   String[] dateParts = collectionDate.split("/");
			   if(dateParts.length == 3)
			   {
				   // Format it to be yyyymmdd
			   
				   String formattedDate = dateParts[2] + dateParts[1] + dateParts[0];
				   sql += "where (DC.DeferredDate is not null and DC.DeferredDate <= '" + formattedDate + "') OR "
					      + "(DC.DeferredDate is null and DC.PayCDueDate <= '" + formattedDate + "')";
				   whereClauseAdded = true;
			   }
		   }
		   
		   if(merchantId != null && merchantId.length() > 0)
		   {
			   if(whereClauseAdded)
			   {
				   sql += " AND ";
			   }
			   else
			   {
				   sql += " WHERE ";
			   }
			   sql += "c.CustCode = " + merchantId;
			   whereClauseAdded = true;
		   }
		   
		   if(invoiceNumber != null)
		   {
			   if(whereClauseAdded)
			   {
				   sql += " AND ";
			   }
			   else
			   {
				   sql += " WHERE ";
			   }
			   sql += "DC.InvoiceReferenceNumber = " +  invoiceNumber;
			   whereClauseAdded = true;
		   }
		   
		   
			
		   sql += " ORDER BY PayCDueDate";
		   
		   System.out.println(sql);
		   conn = getConnection();
           stmt = conn.createStatement();
		   rs = stmt.executeQuery(sql);
		   
		   //STEP 5: Extract data from result set
		   while(rs.next())
		   {
			   long id = rs.getLong("InvoiceReferenceNumber");
			   CollectionBean bean = null;
			   if(resultMap.containsKey(id) == false)
			   {
					bean = new CollectionBean();
				   	bean.setCollectionId(rs.getLong("PayCReferenceNumber"));
				   	bean.setDueDate(rs.getDate("PayCDueDate"));
				   	bean.setInvoiceAmount(rs.getDouble("InvoiceAmount"));
					bean.setInvoiceNumber(id);
					bean.setCustCity(rs.getString("CustCity"));
					bean.setCustPhoneNumber(rs.getString("CustContactNumber"));
					bean.setCustCode(rs.getLong("CustCode"));
					bean.setCustName(rs.getString("CustName"));
					bean.setStatus(rs.getString("PayCStatus"));
					resultMap.put(id, bean);
			   	}
			   	bean = resultMap.get(id);
			   	
			   	// Read the bank data
			   	long supplierCode = rs.getLong("SupplierCode");
			   	if(supplierCode > 0)
			   	{
				   	CollectionDetailBean detailBean = new CollectionDetailBean();
				   	detailBean.setCollectionDetailId(rs.getLong("PayCReferenceSubNumber"));
					detailBean.setCompanyCode(rs.getLong("AccountLocationCode"));
					detailBean.setCompanyName(rs.getString("CompanyName"));
					detailBean.setPaidAmount(rs.getDouble("PaidAmount"));
					detailBean.setCollectionDate(rs.getDate("PayCDate"));
					detailBean.setLedgerNumber(rs.getInt("LedgerPageNumber"));
					detailBean.setCustomerBankName(rs.getString("CustBankName"));
					detailBean.setSupplierCode(rs.getLong("SupplierCode"));
					detailBean.setSupplierName(rs.getString("SupplierName"));
					detailBean.setSupplierBankId(rs.getLong("SupplierBankId"));
					detailBean.setSupplierBankName(rs.getString("SupplierBank"));
					detailBean.setSupplierBankBranch(rs.getString("SupplierBankBranch"));
					detailBean.setSupplierAccountNumber(rs.getString("SupplierAcctNum"));
					bean.getDetailsList().add(detailBean);
			   	}
			   
			   	resultMap.put(bean.getInvoiceNumber(), bean);
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
	   
	   return resultMap.values();

   }
   
   public static void updateCollectionInfo(CollectionBean bean) 
   {
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try
	   {
		   	conn = getConnection();
           	stmt = conn.createStatement();
		   
		   	System.out.println("ID: " + bean.getCollectionId());
		   	double balanceAmount = bean.getInvoiceAmount();
		   	System.out.println("Balance: " + balanceAmount);
		   	
		   	// Get list of existing IDs to identify when user deletes any entry
		   	ArrayList<Long> existingIDs = new ArrayList<Long>();
		   	String getIdSql = "SELECT PayCReferenceSubNumber FROM DailyPayCDetails where PayCReferenceNumber=" + bean.getCollectionId();
		   	rs = stmt.executeQuery(getIdSql);
		   	while(rs.next())
		   	{
			   existingIDs.add(rs.getLong("PayCReferenceSubNumber"));
		   	}
		   	rs.close();
		   
		   	ArrayList<Long> modifiedIDs = new ArrayList<Long>();
			for(CollectionDetailBean detailBean : bean.getDetailsList())
			{
				long detailID = detailBean.getCollectionDetailId() ;
				System.out.println("Detail ID: " + detailID);
				System.out.println("Paid amt: " + detailBean.getPaidAmount());
				balanceAmount = balanceAmount - detailBean.getPaidAmount();
				if( detailID == 0)
				{
					// insert new entry
					String insertDetailSql = "INSERT INTO DailyPayCDetails (PayCReferenceNumber, PayCDate, CustBankName,  "
											+"SupplierCode, SupplierBankId, PaidAmount, AccountLocationCode, LedgerPageNumber, "
											+"CreatedDate, CreatedBy) values(" 
							                + bean.getCollectionId() + "," 
							                + "'" + detailBean.getCollectionDateStr() + "', "
							                + "'" + detailBean.getCustomerBankName() + "', "
							                + detailBean.getSupplierCode() + ", "
							                + detailBean.getSupplierBankId() + ", "
							                + detailBean.getPaidAmount() + ", "
							                + detailBean.getCompanyCode() + ", "
							                + detailBean.getLedgerNumber() + ", GETDATE(), 'UI Admin')";
					System.out.println(insertDetailSql);
					
					stmt.executeUpdate(insertDetailSql);
					
				}
				else
				{
					modifiedIDs.add(detailID);
					String updateDetailSql = "UPDATE DailyPayCDetails SET "
												+ "AccountLocationCode = " + detailBean.getCompanyCode()
												+ ", LedgerPageNumber=" + detailBean.getLedgerNumber()
												
												+ " WHERE PayCReferenceNumber=" + bean.getCollectionId()
												+ " AND PayCReferenceSubNumber=" + detailID;
					System.out.println(updateDetailSql);
					
					stmt.executeUpdate(updateDetailSql);
				}
			}
			
			// Check if entries need to be deleted
			for(long id : existingIDs)
			{
				if(!modifiedIDs.contains(id))
				{
					// entry not found in modified list. Must have been deleted on the UI
					String deleteDetailSql = "UPDATE DailyPayCDetails SET Deleted=1"
											+ " WHERE PayCReferenceNumber=" + bean.getCollectionId()
											+ " AND PayCReferenceSubNumber=" + id;
					System.out.println(deleteDetailSql);

					stmt.executeUpdate(deleteDetailSql);
				}
			}
			
			// Update parent entry
			
			String status = "OPEN";
			if(balanceAmount > 0)
			{
				if(!"".equals(bean.getDeferredDateStr()))
				{
					status = "DEFERRED";
				}
			}
			else
			{
				status = "CLOSED";
			}
			System.out.println("Setting status to " + status);
			String updateCollectionSql = "UPDATE DailyPayC SET PayCStatus='" + status + "' ";
			if(!"".equals(bean.getDeferredDateStr()))
			{
				updateCollectionSql += ", PayCDueDate='" + bean.getDeferredDateStr() + "' ";
			}
			updateCollectionSql += "WHERE PayCReferenceNumber=" + bean.getCollectionId();
			System.out.println(updateCollectionSql);
			stmt.executeUpdate(updateCollectionSql);
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
	   
	   
   }
   /*
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
			bean1.setPartyInfo("Trichy", "4567890123");
			bean1.setCustCode(31);
			bean1.setPartyName("Merchant 140000000");
			bean1.setStatus("Closed");
			
			CollectionDetailBean detailBean1 = new CollectionDetailBean();
			detailBean1.setCompanyCode(101);
			detailBean1.setCompanyName("Garment Mantra");
			detailBean1.setPaidAmount(150000);
			detailBean1.setCollectionDate(Calendar.getInstance().getTime());
			detailBean1.setLedgerNumber(1);
			detailBean1.setPartyBankInfo("IndusInd Bank", "Bangalore");
			detailBean1.setSupplierCode(200000000);
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
			detailBean2.setCompanyCode(102);
			detailBean2.setCompanyName("SP Tex");
			detailBean2.setPartyBankInfo("Bank of India", "Bangalore");
			detailBean2.setSupplierCode(200000000);
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
			detailBean3.setCompanyCode(101);
			detailBean3.setCompanyName("Garment Mantra");
			detailBean3.setPartyBankInfo("State Bank of India", "Bangalore");
			detailBean3.setSupplierCode(51);
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
   */
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
	   //Collection<CollectionBean> custList  = DBHandler.getCollectionsByDate(null);
	   //System.out.println("Cust list: " + custList.size());
   }

   
} 
