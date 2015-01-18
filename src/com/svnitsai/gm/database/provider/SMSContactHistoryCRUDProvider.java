package com.svnitsai.gm.database.provider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.svnitsai.gm.database.generated.*;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.display.DisplayUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * SMSContactHistoryCRUDProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Performs CRUD operation on SMSContactHistory table
 * 
 */

public class SMSContactHistoryCRUDProvider {
	
	/*
	 * Create SMSContactHistory Record
	 */
	public int createSMSContactHistory (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		SmscontactHistory smsContactHistory = (SmscontactHistory) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " Insert into SMSContactHistory "
					+ " ( RequestInitiatedTS "
					+ " , RequestVendorReferrence "
					+ " , CustId "
					+ " , CustCode "
					+ " , CustName "
					+ " , SMSMobileNumber "
					+ " , SMSMobileOwnerName "
					+ " , SMSMessage "
					+ " , SMSVendor "
					+ " , SentTimeStamp "
					+ " , FailedReason "
					+ " , PayCReferenceNumber "
					+ " , Status "
					+ " , StatusUpdatedTS ) "
					+ " values "
					+ " ( :hostRequestInitiatedTS "
					+ " , :hostRequestVendorReferrence "
					+ " , :hostCustId "
					+ " , :hostCustCode "
					+ " , :hostCustName "
					+ " , :hostSMSMobileNumber "
					+ " , :hostSMSMobileOwnerName "
					+ " , :hostSMSMessage "
					+ " , :hostSMSVendor "
					+ " , :hostSentTimeStamp "
					+ " , :hostFailedReason "
					+ " , :hostPayCReferenceNumber "
					+ " , :hostStatus "
					+ " , :hostStatusUpdatedTS ) ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostRequestInitiatedTS", smsContactHistory.getRequestInitiatedTs())
					.setParameter("hostRequestVendorReferrence", smsContactHistory.getRequestVendorReferrence())
					.setParameter("hostCustId", smsContactHistory.getCustId())
					.setParameter("hostCustCode", smsContactHistory.getCustCode())
					.setParameter("hostCustName", smsContactHistory.getCustName())
					.setParameter("hostSMSMobileNumber", smsContactHistory.getSmsmobileNumber())
					.setParameter("hostSMSMobileOwnerName", smsContactHistory.getSmsmobileOwnerName())
					.setParameter("hostSMSMessage", smsContactHistory.getSmsmessage())
					.setParameter("hostSMSVendor", smsContactHistory.getSmsvendor())
					.setParameter("hostSentTimeStamp", smsContactHistory.getSentTimeStamp())
					.setParameter("hostFailedReason", smsContactHistory.getFailedReason())
					.setParameter("hostPayCReferenceNumber", smsContactHistory.getPayCreferenceNumber())
					.setParameter("hostStatus", smsContactHistory.getStatus())
					.setParameter("hostStatusUpdatedTS", DateUtil.getCurrentTimestamp());
			
//			System.out.println (" Provider Req. initiated TS " + smsContactHistory.getRequestInitiatedTs());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! Insert into SMSContactHistory failed for RequestInitiatedTS: " + smsContactHistory.getRequestInitiatedTs()
						+ " ; SMSMobile Number: " + smsContactHistory.getSmsmobileNumber()
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Insert into SMSContactHistory succeeded for RequestInitiatedTS: " +smsContactHistory.getRequestInitiatedTs()  
						+ " ; SMSMobile Number: " + smsContactHistory.getSmsmobileNumber()
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			}
		} catch (JDBCException e) {
			returnCode = DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			returnCode = DBException.HandleHibernateException(e);
		} finally {
			try {
				transaction.commit();
				session.flush();
				session.clear();
				session.close();
			} catch (JDBCException e) {
				returnCode = DBException.HandleJDBCException(e);
			} catch (HibernateException e) {
				returnCode = DBException.HandleHibernateException(e);
			}
		}
		return returnCode;
	}
	
	/*
	 * Read operation on SMS Contact history table based on given RequestInitiatedTS
	 */
	
	public Object getSMSContactHistoryList (String inRequestInitiatedTS) {

		Session session = HibernateUtil.getSession();
		List result = null;

		try {
			String SQL_QUERY = " SELECT HistoryId , RequestInitiatedTS "
					+ " , ISNULL(RequestVendorReferrence,'') as RequestVendorReferrence , CustId "
					+ " , CustCode , CustName , SMSMobileNumber , SMSMobileOwnerName "
					+ " , SMSMessage , SMSVendor , SentTimeStamp , ISNULL(FailedReason,'') as FailedReason "
					+ " , PayCReferenceNumber "
					+ " , Status , StatusUpdatedTS FROM SMSContactHistory CH "
					+ " WHERE CH.RequestInitiatedTS = :hostRequestInitiatedTS ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY).setParameter("hostRequestInitiatedTS", inRequestInitiatedTS);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on SMSContactHistoryProvider; for RequestInitiatedTS: " + inRequestInitiatedTS  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on SMSContactHistoryProvider; for RequestInitiatedTS: " + inRequestInitiatedTS  + "   @ "
						+ DateUtil.getCurrentTimestamp().toString());
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			return result;
		}
	}

	/*
	 * Read operation on SMS Contact history table to get last one week requests
	 */
	
	public Object getSMSContactHistoryList_OneWeek () {
		String requestStartFrom = DateUtil.getTSWeekBefore(1).toString().substring(0, 11) + "00:00:00.000";//2015-01-17 14:48:24.959
		String requestStartTo = DateUtil.getCurrentTimestamp().toString();
		
//		System.out.println (" from " + requestStartFrom + " to " + requestStartTo);
		return getSMSContactHistoryList(requestStartFrom,requestStartTo);
	}

	/*
	 * Read operation on SMS Contact history table based on from & to requestInitiated TS
	 */
	public Object getSMSContactHistoryList (String inRequestInitiatedFrom, String inRequestInitiatedTo) {

		Session session = HibernateUtil.getSession();
		List result = null;
//		System.out.println (" Dates from history provider " + inRequestInitiatedFrom + " to " + inRequestInitiatedTo);
		try {
			String SQL_QUERY = " SELECT HistoryId , RequestInitiatedTS "
					+ " , ISNULL(RequestVendorReferrence,'') as RequestVendorReferrence , CustId "
					+ " , CustCode , CustName , SMSMobileNumber , SMSMobileOwnerName "
					+ " , SMSMessage , SMSVendor , SentTimeStamp , ISNULL(FailedReason,'') as FailedReason "
					+ " , PayCReferenceNumber "
					+ " , Status , StatusUpdatedTS FROM SMSContactHistory CH "
					+ " WHERE CH.RequestInitiatedTS >= :hostRequestInitiatedFrom "
					+ "   AND CH.RequestInitiatedTS <= :hostRequestInitiatedTo ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY).setParameter("hostRequestInitiatedFrom", inRequestInitiatedFrom)
									.setParameter("hostRequestInitiatedTo",inRequestInitiatedTo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on SMSContactHistoryProvider; from RequestInitiatedfrom: " + inRequestInitiatedFrom
						+ " to " + inRequestInitiatedTo
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on SMSContactHistoryProvider; from RequestInitiatedTS: " 
						+ " to " + inRequestInitiatedTo
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			return result;
		}
	}

	/*
	 * Read operation on SMS Contact history table based on customerid
	 */
	public Object getSMSContactHistoryByCustId (String inCustomerId) {

		Session session = HibernateUtil.getSession();
		List result = null;
		try {
			String SQL_QUERY = " SELECT HistoryId , RequestInitiatedTS "
					+ " , ISNULL(RequestVendorReferrence,'') as RequestVendorReferrence , CustId "
					+ " , CustCode , CustName , SMSMobileNumber , SMSMobileOwnerName "
					+ " , SMSMessage , SMSVendor , SentTimeStamp , ISNULL(FailedReason,'') as FailedReason "
					+ " , PayCReferenceNumber "
					+ " , Status , StatusUpdatedTS FROM SMSContactHistory CH "
					+ " WHERE CH.CustId = :hostCustomerId ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY).setParameter("hostCustomerId", inCustomerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on SMSContactHistoryProvider; for Customerid: " + inCustomerId
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on SMSContactHistoryProvider; from Customerid: " + inCustomerId
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			return result;
		}
	}
	/*
	 * Update operation on SMSContactHistory
	 */
	
	public int updateSMSContactHistory (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		SmscontactHistory smsContactHistory = (SmscontactHistory) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " UPDATE SMSContactHistory "
					+ " SET RequestVendorReferrence = :hostRequestVendorReferrence"
					+ "   , SentTimeStamp = :hostSentTimeStamp"
					+ "   , FailedReason = :hostFailedReason"
					+ "   , Status = :hostStatus"
					+ "   , StatusUpdatedTS = :hostStatusUpdatedTS"
					+ "   WHERE historyid = :hosthistoryid";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostRequestVendorReferrence", smsContactHistory.getRequestVendorReferrence())
					.setParameter("hostSentTimeStamp", smsContactHistory.getSentTimeStamp())
					.setParameter("hostFailedReason", smsContactHistory.getFailedReason())
					.setParameter("hostStatus", smsContactHistory.getStatus())
					.setParameter("hostStatusUpdatedTS", DateUtil.getCurrentTimestamp())
					.setParameter("hosthistoryid", smsContactHistory.getHistoryId());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! No update to SMSContactHistory for Historyid: " + smsContactHistory.getHistoryId()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Updated " + result + " record(s) in SMSContactHistory for Historyid: " + smsContactHistory.getHistoryId()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			}
		} catch (JDBCException e) {
			returnCode = DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			returnCode = DBException.HandleHibernateException(e);
		} finally {
			try {
				transaction.commit();
				session.flush();
				session.clear();
				session.close();
			} catch (JDBCException e) {
				returnCode = DBException.HandleJDBCException(e);
			} catch (HibernateException e) {
				returnCode = DBException.HandleHibernateException(e);
			}
		}
		return returnCode;
	}

	/*
	 * Delete operation on SMSContactHistory
	 */
	
	public int deleteSMSContactHistory (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
//		Transaction transaction = null;
//		Session session = null;
//		SMSContactHistory customerBank = (SMSContactHistory) dataObject;
//		try {
//			session = HibernateUtil.getSession();
//			transaction = session.beginTransaction();
//			
//			String SQL_QUERY = " DELETE from SMSContactHistory "
//					+ " where CustBankId = :hostCustBankId and CustCode = :hostCustCode";
//			
//			/* Run as native SQL query*/
//			Query query = session.createSQLQuery(SQL_QUERY)
//					.setParameter("hostCustBankId", customerBank.getId().getCustBankId())
//					.setParameter("hostCustCode", customerBank.getId().getCustCode());
//			
//			int result = query.executeUpdate(); //returns number of rows affected
//			
//			if (result == 0) {
//				LogUtil.log(LogUtil.Message_Type.Information,
//						" FAILED !!! No records to DELETE from Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
//								+ DateUtil.getCurrentTimestamp().toString());
//			} else {
//				LogUtil.log(LogUtil.Message_Type.Information,
//						" SUCCESS!!! Deleted " + result + " record(s) from Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
//								+ DateUtil.getCurrentTimestamp().toString());
//			}
//		} catch (JDBCException e) {
//			returnCode = DBException.HandleJDBCException(e);
//		} catch (HibernateException e) {
//			returnCode = DBException.HandleHibernateException(e);
//		} finally {
//			try {
//				transaction.commit();
//				session.flush();
//				session.clear();
//				session.close();
//			} catch (JDBCException e) {
//				returnCode = DBException.HandleJDBCException(e);
//			} catch (HibernateException e) {
//				returnCode = DBException.HandleHibernateException(e);
//			}
//		}
		return returnCode;
	}
	

//	public static void main(String[] args) {
//		
//		SMSContactHistoryCRUDProvider contactHistoryCRUDProvider = new SMSContactHistoryCRUDProvider();
//		
//		String oneWeekBefore = DateUtil.getDateWeeksBefore(1);
//		System.out.println (" One week before " + oneWeekBefore);
//		System.out.println (" Today " + DateUtil.getTodayDate());
//		
//		//String requestStartFrom = DateUtil.getTSWeekBefore(1).toString().substring(12) + "00:00:00.000" ; //2015-01-17 14:48:24.959

//		SmscontactHistory smsContactHistory = new SmscontactHistory();
//		smsContactHistory.setRequestInitiatedTs(DateUtil.getTimestampFromString("2015-01-10 07:50:00.000"));
//		smsContactHistory.setRequestVendorReferrence("12345");
//		smsContactHistory.setCustId(9876);
//		smsContactHistory.setCustCode(1234);
//		smsContactHistory.setCustName("Ram Textiles");
//		smsContactHistory.setSmsmobileNumber(1234509876);
//		smsContactHistory.setSmsmobileOwnerName("Rakesh");
//		smsContactHistory.setSmsmessage("This sms message to be sent");
//		smsContactHistory.setSmsvendor("Apex");
//		smsContactHistory.setSentTimeStamp(null);
//		smsContactHistory.setFailedReason(null);
//		smsContactHistory.setPayCreferenceNumber(1345);
//		smsContactHistory.setStatus("initial");
//		smsContactHistory.setStatusUpdatedTs(DateUtil.getCurrentTimestamp());
//	
//		int returnCode = contactHistoryCRUDProvider.createSMSContactHistory(smsContactHistory);


//		SmscontactHistory smsContactHistory = new SmscontactHistory();
//		smsContactHistory.setRequestVendorReferrence("12345678");
//		smsContactHistory.setSentTimeStamp(DateUtil.getTimestampFromString("2015-01-10 07:50:00.000"));
//		smsContactHistory.setFailedReason(null);
//		smsContactHistory.setStatus("success");
//		smsContactHistory.setStatusUpdatedTs(DateUtil.getCurrentTimestamp());
//		smsContactHistory.setHistoryId(7);
//	
//		int returnCode = contactHistoryCRUDProvider.updateSMSContactHistory(smsContactHistory);
//
//		
//		List result = (List) contactHistoryCRUDProvider.getSMSContactHistoryList("2015-01-10 07:50:00.000", "2015-01-20 07:50:00.000");
		//List result = (List) contactHistoryCRUDProvider.getSMSContactHistoryList_OneWeek();
//		List result = (List) contactHistoryCRUDProvider.getSMSContactHistoryByCustId("66691");
//		  if ((result != null) && (result.size() > 0)) { 
//			  for (Object object : result) { 
//				  Map row = (Map) object; System.out.println(" ");
//				  System.out.println(" HistoryId: " + row.get("HistoryId")
//						  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
//						  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
//						  + " CustId: " + row.get("CustId")
//						  + " CustCode: " + row.get("CustCode")
//						  + " CustName: " + row.get("CustName")
//						  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
//						  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
//						  + " SMSMessage: " + row.get("SMSMessage")
//						  + " SMSVendor: " + row.get("SMSVendor")
//						  + " SentTimeStamp: " + row.get("SentTimeStamp")
//						  + " FailedReason: " + row.get("FailedReason")
//						  + " PayCReferenceNumber: " + row.get("PayCReferenceNumber")
//						  + " Status: " + row.get("Status")
//						  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
//						  );
//				   String sortRequestInitDate =  row.get("RequestInitiatedTS").toString().substring(0,10);
//				   String dispRequestInitDate =  DisplayUtil.getDisplayDate(sortRequestInitDate, new SimpleDateFormat("yyyy-MM-dd"));
//				   System.out.println(" sort date " + sortRequestInitDate + " disp date " + dispRequestInitDate);
//
//				  } 
//			  }
	
//			SMSContactHistoryId SMSContactHistoryId = new SMSContactHistoryId();
//			SMSContactHistoryId.setCustBankId(67);
//			SMSContactHistoryId.setCustCode(1081);
//			SMSContactHistory customerBank = new SMSContactHistory();
//			customerBank.setId(SMSContactHistoryId);
//			customerBank.setCustBank("CanaraBank");
//			customerBank.setCustBankBranch("New Delhi");
//			customerBank.setCustBankAccountType("Savings");
//			customerBank.setCustBankAccountNumber("12345 12345");
//			customerBank.setCustBankAccountName("Mr.Samysosa");
//			customerBank.setCreatedDate(DateUtil.getCurrentTimestamp());
//			customerBank.setCreatedBy("Program");
//			customerBank.setUpdatedDate(DateUtil.getCurrentTimestamp());
//			customerBank.setUpdatedBy("UpdatePgm");
//		
//			SMSContactHistoryCRUDProvider customerBankInfoCrud = new SMSContactHistoryCRUDProvider();
//			int returnCode = customerBankInfoCrud.deleteCustomerBank(customerBank);
//	
//	
//	
//	}

}
