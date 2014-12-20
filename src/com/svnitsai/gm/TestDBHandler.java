package com.svnitsai.gm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TestDBHandler {
	public static LinkedHashMap<Long, String> getMerchants() {
		return getCustomerIdMap("Merchant");
	}

	public static LinkedHashMap<Long, String> getCustomerIdMap(String type) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		// Preserves the sorted order in which data was added to the map
		LinkedHashMap<Long, String> resultMap = new LinkedHashMap<Long, String>();
		// TODO: HibernateME
		try {
			String sql = "select CustCode, CustName FROM Customer WHERE CustType='"
					+ type + "' " + "ORDER BY CustName";
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				long id = rs.getLong("CustCode");
				String name = rs.getString("CustName");
				resultMap.put(id, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDBObjects(conn, stmt, rs);
		}

		return resultMap;
	}

	public static LinkedHashMap<Long, CustomerBean> getSuppliers() {
		return getCustomers("Supplier");
	}

	public static LinkedHashMap<Long, CustomerBean> getCustomers(String type) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		// Preserves the sorted order in which data was added to the map
		LinkedHashMap<Long, CustomerBean> resultMap = new LinkedHashMap<Long, CustomerBean>();
		// TODO: HibernateME
		try {
			// TODO: Change to read the bank data
			String sql = "select C.CustCode, CustName, CustAddress1, CustAddress2, CustCity, CustState, CustCountry, CustContactNumber, "
					+ "CustBankId, CustBank, CustBankBranch, CustBankAccountType, CustBankAccountNumber "
					+ "FROM Customer C, CustomerBanks CB "
					+ "WHERE CustType='"
					+ type
					+ "' AND C.CustCode = CB.CustCode "
					+ "ORDER BY CustName";
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {

				CustomerBean bean = null;

				long id = rs.getLong("CustCode");
				if (resultMap.containsKey(id) == false) {
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
					if (location.length() > 0 && state.length() > 0) {
						location += ", " + state;
					} else if (state.length() > 0) {
						location = state;
					}

					if (location.length() > 0 && country.length() > 0) {
						location += ", " + country;
					} else if (country.length() > 0) {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDBObjects(conn, stmt, rs);
		}

		return resultMap;
	}

	public static CollectionBean getCollectionInfo(String invoiceNumber) {
		Collection<CollectionBean> list = getCollections(null, null,
				invoiceNumber, false);
		if (list != null && list.size() > 0) {
			return (CollectionBean) list.iterator().next();
		}
		return null;
	}

	public static Collection<CollectionBean> getCollections(
			String collectionDate, String merchantId, String invoiceNumber,
			boolean showDeleted) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		// TODO: HibernateME
		// Preserves the sorted order in which data was added to the map
		LinkedHashMap<Long, CollectionBean> resultMap = new LinkedHashMap<Long, CollectionBean>();

		try {
			// select and read collection data
			String sql = "SELECT DC.PayCReferenceNumber, DC.PayCDueDate, DC.CustCode, DC.InvoiceAmount, "
					+ "DC.PayCStatus, DC.InvoiceReferenceNumber, DCD.PayCReferenceSubNumber, "
					+ "DC.DeferredDate, DCD.PayCDate, DCD.SupplierCode, DCD.SupplierBankId, "
					+ "DCD.PaidAmount, DCD.AccountLocationCode, DCD.LedgerPageNumber, "
					+ "C.CustName, C.CustContactNumber, C.CustCity, "
					+ "CO.CustName AS CompanyName, s.CustName AS SupplierName, CB1.CustBank AS SupplierBank, "
					+ "CB1.CustBankBranch AS SupplierBankBranch, CB1.CustBankAccountNumber AS SupplierAcctNum "
					+ "FROM DailyPayC DC "
					+ "join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant' "
					+ "left join DailyPayCDetails DCD ON DC.PayCReferenceNumber = DCD.PayCReferenceNumber ";

			if (!showDeleted) {
				sql += "AND DCD.Deleted!=1 ";
			}

			sql += "left join Customer s on s.custcode = dcd.suppliercode and s.custtype = 'Supplier' "
					+ "left join CustomerBanks CB1 on CB1.CustBankId = DCD.SupplierBankId and s.custcode = cb1.custcode "
					+ "left join Customer CO on CO.custcode = dcd.accountlocationcode and CO.custtype = 'Company' ";

			boolean whereClauseAdded = false;
			if (collectionDate != null && collectionDate.length() > 0) {
				// Date is in format dd/mm/yyyy
				String[] dateParts = collectionDate.split("/");
				if (dateParts.length == 3) {
					// Format it to be yyyymmdd

					String formattedDate = dateParts[2] + dateParts[1]
							+ dateParts[0];
					sql += "where (DC.DeferredDate is not null and DC.DeferredDate <= '"
							+ formattedDate
							+ "') OR "
							+ "(DC.DeferredDate is null and DC.PayCDueDate <= '"
							+ formattedDate + "')";
					whereClauseAdded = true;
				}
			}

			if (merchantId != null && merchantId.length() > 0) {
				if (whereClauseAdded) {
					sql += " AND ";
				} else {
					sql += " WHERE ";
				}
				sql += "c.CustCode = " + merchantId;
				whereClauseAdded = true;
			}

			if (invoiceNumber != null) {
				if (whereClauseAdded) {
					sql += " AND ";
				} else {
					sql += " WHERE ";
				}
				sql += "DC.InvoiceReferenceNumber = " + invoiceNumber;
				whereClauseAdded = true;
			}

			sql += " ORDER BY PayCDueDate";

			System.out.println(sql);
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				long id = rs.getLong("InvoiceReferenceNumber");
				CollectionBean bean = null;
				if (resultMap.containsKey(id) == false) {
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

				// Read the payment data
				double paidAmt = rs.getDouble("PaidAmount");
				if (paidAmt > 0) {
					CollectionDetailBean detailBean = new CollectionDetailBean();
					detailBean.setCollectionDetailId(rs
							.getLong("PayCReferenceSubNumber"));
					detailBean
							.setCompanyCode(rs.getLong("AccountLocationCode"));
					detailBean.setCompanyName(rs.getString("CompanyName"));
					detailBean.setPaidAmount(paidAmt);
					detailBean.setCollectionDate(rs.getDate("PayCDate"));
					detailBean.setLedgerNumber(rs.getInt("LedgerPageNumber"));
					//detailBean
					//		.setCustomerBankName(rs.getString("CustBankName"));
					detailBean.setSupplierCode(rs.getLong("SupplierCode"));
					detailBean.setSupplierName(rs.getString("SupplierName"));
					detailBean.setSupplierBankId(rs.getLong("SupplierBankId"));
					detailBean
							.setSupplierBankName(rs.getString("SupplierBank"));
					detailBean.setSupplierBankBranch(rs
							.getString("SupplierBankBranch"));
					detailBean.setSupplierAccountNumber(rs
							.getString("SupplierAcctNum"));
					bean.getDetailsList().add(detailBean);
				}

				resultMap.put(bean.getInvoiceNumber(), bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDBObjects(conn, stmt, rs);
		}

		return resultMap.values();

	}

	public static void updateCollectionInfo(CollectionBean bean) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			// TODO: HibernateME
			System.out.println("ID: " + bean.getCollectionId());
			double balanceAmount = bean.getInvoiceAmount();
			System.out.println("Balance: " + balanceAmount);

			// Get list of existing IDs to identify when user deletes any entry
			ArrayList<Long> existingIDs = new ArrayList<Long>();
			String getIdSql = "SELECT PayCReferenceSubNumber FROM DailyPayCDetails where PayCReferenceNumber="
					+ bean.getCollectionId();
			rs = stmt.executeQuery(getIdSql);
			while (rs.next()) {
				existingIDs.add(rs.getLong("PayCReferenceSubNumber"));
			}
			rs.close();

			ArrayList<Long> modifiedIDs = new ArrayList<Long>();
			for (CollectionDetailBean detailBean : bean.getDetailsList()) {
				long detailID = detailBean.getCollectionDetailId();
				System.out.println("Detail ID: " + detailID);
				System.out.println("Paid amt: " + detailBean.getPaidAmount());
				balanceAmount = balanceAmount - detailBean.getPaidAmount();
				if (detailID == 0) {
					// insert new entry
					String insertDetailSql = "INSERT INTO DailyPayCDetails (PayCReferenceNumber, PayCDate, "
							+ "SupplierCode, SupplierBankId, PaidAmount, AccountLocationCode, LedgerPageNumber, "
							+ "CreatedDate, CreatedBy) values("
							+ bean.getCollectionId()
							+ ","
							+ "'"
							+ Util.getFormattedDateForDB(detailBean
									.getCollectionDateStr())
							+ "', "
							//+ "'"
							//+ detailBean.getCustomerBankName()
							//+ "', "
							+ detailBean.getSupplierCode()
							+ ", "
							+ detailBean.getSupplierBankId()
							+ ", "
							+ detailBean.getPaidAmount()
							+ ", "
							+ detailBean.getCompanyCode()
							+ ", "
							+ detailBean.getLedgerNumber()
							+ ", GETDATE(), 'UI Admin')";
					System.out.println(insertDetailSql);

					stmt.executeUpdate(insertDetailSql);

				} else {
					modifiedIDs.add(detailID);
					String updateDetailSql = "UPDATE DailyPayCDetails SET "
							+ "AccountLocationCode = "
							+ detailBean.getCompanyCode()
							+ ", LedgerPageNumber="
							+ detailBean.getLedgerNumber()

							+ " WHERE PayCReferenceNumber="
							+ bean.getCollectionId()
							+ " AND PayCReferenceSubNumber=" + detailID;
					System.out.println(updateDetailSql);

					stmt.executeUpdate(updateDetailSql);
				}
			}

			// Check if entries need to be deleted
			for (long id : existingIDs) {
				if (!modifiedIDs.contains(id)) {
					// entry not found in modified list. Must have been deleted
					// on the UI
					String deleteDetailSql = "UPDATE DailyPayCDetails SET Deleted=1"
							+ " WHERE PayCReferenceNumber="
							+ bean.getCollectionId()
							+ " AND PayCReferenceSubNumber=" + id;
					System.out.println(deleteDetailSql);

					stmt.executeUpdate(deleteDetailSql);
				}
			}

			// Update parent entry

			String status = "OPEN";
			if (balanceAmount > 0) {
				if (!"".equals(bean.getDeferredDateStr())) {
					status = "DEFERRED";
				}
			} else {
				status = "CLOSED";
			}
			System.out.println("Setting status to " + status);
			String updateCollectionSql = "UPDATE DailyPayC SET PayCStatus='"
					+ status + "' ";
			if (!"".equals(bean.getDeferredDateStr())) {
				updateCollectionSql += ", PayCDueDate='"
						+ Util.getFormattedDateForDB(bean.getDeferredDateStr())
						+ "' ";
			}
			updateCollectionSql += "WHERE PayCReferenceNumber="
					+ bean.getCollectionId();
			System.out.println(updateCollectionSql);
			stmt.executeUpdate(updateCollectionSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDBObjects(conn, stmt, rs);
		}
	}

	public static LinkedList<DailyPayableBean> getDailyPayables(Date payableDate)
	   {
		   Connection conn = null;
		   Statement stmt = null;
		   ResultSet rs = null;
		   
		   // Preserves the sorted order in which data was added to the map
		   LinkedList<DailyPayableBean> payableList = new LinkedList<DailyPayableBean>();
		   
		   try 
		   {
			   // TODO: Change to read the bank data
			   String sql = "SELECT PayableReferenceNumber, PayableDate, PayableAmount, PaidAmount, PayableStatus, SupplierCode, Instructions "
					        + "FROM DailyPayable "
					   		+ "WHERE PayableDate>='" + Util.getFormattedDateForDB(payableDate) + "' "
					   		+ "ORDER BY PayableDate";
			   conn = getConnection();
	           stmt = conn.createStatement();
			   rs = stmt.executeQuery(sql);
			    
			   //STEP 5: Extract data from result set
			   while(rs.next())
			   {
				   
				   DailyPayableBean bean = new DailyPayableBean();
				   bean.setPayableId(rs.getLong("PayableReferenceNumber"));
				   bean.setPayableDate(rs.getDate("PayableDate"));
				   bean.setPayableAmount(rs.getDouble("PayableAmount"));
				   bean.setPaidAmount(rs.getDouble("PaidAmount"));
				   bean.setStatus(rs.getString("PayableStatus"));
				   bean.setSupplierCode(rs.getLong("SupplierCode"));
				   bean.setInstructions(rs.getString("Instructions"));
				   payableList.add(bean);
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
		   
		   return payableList;
	   }
	   
	   public static void savePayable(ArrayList<DailyPayableBean> payableList) 
	   {
		   Connection conn = null;
		   Statement stmt = null;
		   ResultSet rs = null;
		   
		   try
		   {
			   	conn = getConnection();
	           	stmt = conn.createStatement();
			   
	           	String deleteSql = "DELETE FROM DailyPayable WHERE PayableDate >= '" + Util.getFormattedDateForDB(Calendar.getInstance().getTime()) +"'";
	           	System.out.println(deleteSql);
				stmt.executeUpdate(deleteSql);
				
	           	for(DailyPayableBean bean : payableList)
				{
			   		if(bean.getSupplierCode() > 0)
			   		{
						// insert new entry
						String insertDetailSql = "INSERT INTO DailyPayable (PayableDate, PayableAmount, SupplierCode, "
												+"Instructions, CreatedDate, CreatedBy) values("
												+ "'" + Util.getFormattedDateForDB(bean.getPayableDateStr()) + "', "
								                + bean.getPayableAmount() + ", "
								                + bean.getSupplierCode() + ", "
								                + "'" + bean.getInstructions() + "', "
								                + "GETDATE(), 'UI Admin')";
						System.out.println(insertDetailSql);
						
						stmt.executeUpdate(insertDetailSql);
			   		}
			   		/*else
			   		{
			   			// update existing entry
						String updateDetailSql = "UPDATE DailyPayable SET "
												+ "PayableDate='" + Util.getFormattedDateForDB(bean.getPayableDateStr()) + "', "
												+ "PayableAmount=" +  bean.getPayableAmount() + ", "
												+ "SupplierCode=" + bean.getSupplierCode() + ", "
												+ "Instructions='" + bean.getInstructions() + "' " 
												+ "WHERE PayableReferenceNumber=" + bean.getPayableId();
						System.out.println(updateDetailSql);
						
						stmt.executeUpdate(updateDetailSql);
			   		}*/
						
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
	   }

	// TODO: REad from DB connection pool!!!
	public static Connection getConnection() {
		Connection conn = null;

		try {
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			String username = "admin";
			String password = "svnadmin";
			Class.forName(driver).newInstance();
			String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=ProdPayC";
			conn = DriverManager.getConnection(dbURL, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static void closeDBObjects(Connection conn, Statement stmt,
			ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}

			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}

			if (conn != null && !conn.isClosed()) {
				conn.close();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * For testing only public static void main(String[] args) {
	 * //Collection<CollectionBean> custList =
	 * DBHandler.getCollectionsByDate(null); //System.out.println("Cust list: "
	 * + custList.size()); System.out.println ("Get Connection..."); Connection
	 * conn = getConnection(); System.out.println ("Out of get connection"); }
	 */

}
