package com.svnitsai.gm.database.provider;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.svnitsai.gm.Util;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * SMSRecipientProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Reads Database to get SMS recipient list
 * 
 */

public class SMSRecipientProvider {

	/*
	 * getSMSRecipient(String Due_type)
	 * 
	 * @purpose: Reads database to get outstanding RECEIVABLE amount - DailyPayC
	 * is driver table; Query uses AdjustedDueDate (which is DefferedDate if
	 * exists, else DueDate) Any record with ReceivableStatus other than OPEN
	 * are pulled in for display. Includes DailyPayC record even when
	 * corresponding "DailyPayCDetails not existing"
	 * 
	 * IMPORTANT: Assumes DeferredDate is NULL when not set
	 * 
	 * Returns a list of Object in a MAP
	 */
	public Object getSMSRecipient(String due_type) {
		/* List that returns result */
		List result = null;
		Session session = HibernateUtil.getSession();

		try {
			String SQL_QUERY = "Select Receivable.PayCReferenceNumber as ReferenceNumber, Receivable.InvoiceAmount as ReceivableAmount "
					+ " , Receivable.DeferredDate as DeferredDate, Receivable.PayCDueDate as DueDate "
					+ "  , Receivable.AdjustedDueDate as AdjustedDueDate, Received.PaidAmount as ReceivedAmount "
					+ "  , (Receivable.InvoiceAmount - ISNULL(Received.PaidAmount,0)) as BalanceDue "
					+ "  , Receivable.InvoiceNumber as InvoiceNumber "
					+ "  , Cust.SMSMobileNumber, Cust.SMSMobileOwnerName "
					+ " , Cust.MobileNumber, Cust.ResidencePhoneNumber, Cust.CustId, Cust.CustCode"
					+ "  , Cust.CustName as CustomerName " + " from " + " ( ";
			/* Get Merchant Receivable Information */
			SQL_QUERY = SQL_QUERY
					+ " Select DPC.PayCReferenceNumber, DPC.InvoiceAmount, DPC.PayCStatus "
					+ " , CONVERT(VARCHAR(10),DPC.PayCDueDate,111) as PayCDueDate  "
					+ " , CONVERT(VARCHAR(10),DPC.DeferredDate,111) as DeferredDate  "
					+ " , ISNULL(CONVERT(VARCHAR(10),DPC.DeferredDate,111),CONVERT(VARCHAR(10),DPC.PayCDueDate,111)) as AdjustedDueDate  "
					+ " , ISNULL(DPC.InvoiceReferenceNumber,0) as InvoiceNumber "
					+ " , DPC.CustCode  "
					+ " from dbo.DailyPayC DPC  "
					+ " where (    DPC.PayCStatus <> 'CLOSED'  "
					+ " 	 or DPC.PayCStatus IS NULL )  ";
			//Change query as per requested recipients
			switch (due_type) {
				case "PastDue": { //<= today
					SQL_QUERY = SQL_QUERY
							+ " and (ISNULL(CONVERT(VARCHAR(10),DPC.DeferredDate,111),CONVERT(VARCHAR(10),DPC.PayCDueDate,111))  "
							+ "   <= CONVERT(VARCHAR(10), GETDATE(),111))  ";
					break;
				}
				case "FutureDue": { // > today
					SQL_QUERY = SQL_QUERY
							+ " and (ISNULL(CONVERT(VARCHAR(10),DPC.DeferredDate,111),CONVERT(VARCHAR(10),DPC.PayCDueDate,111))  "
							+ "   > CONVERT(VARCHAR(10), GETDATE(),111))  ";
					break;
				}
				default: {//All dues
					break;
				}
			}
			/* Get Merchant Paid Information */
			SQL_QUERY = SQL_QUERY
					+ " ) as Receivable  " + " LEFT OUTER JOIN " + " ( "
					+ "	  Select DPCD.PayCReferenceNumber, SUM(ISNULL(DPCD.PaidAmount,0))as PaidAmount "
					+ "   from dbo.DailyPayCDetails DPCD  "
					+ "  where DPCD.Deleted != 1  "
					+ " group by DPCD.PayCReferenceNumber  "
					+ " ) as Received  "
					+ " ON Receivable.PayCReferenceNumber = Received.PayCReferenceNumber  "
					+ " LEFT OUTER JOIN ";
			/* Get Merchant Information */
			SQL_QUERY = SQL_QUERY
					+ " ( "
					+ "   Select C.CustCode, C.CustName + ', ' + C.CustCity as CustName "
					+ " , ISNULL(C.SMSMobileNumber,0) as SMSMobileNumber "
					+ " , ISNULL(C.SMSMobileOwnerName,'') as SMSMobileOwnerName "
					+ " , ISNULL(c.MobileNumber,0) as MobileNumber "
					+ " , ISNULL(c.ResidencePhoneNumber, 0) as ResidencePhoneNumber "
					+ " , c.CustId "
					+ " from dbo.Customer C "
					+ " where C.CustType = 'Merchant' " + " ) as Cust  "
					+ " on Receivable.CustCode = Cust.CustCode; ";

			/* Run as NATIVE SQL Query */
			Query query = session.createSQLQuery(SQL_QUERY);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records for SMS Recipient list @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) for SMS Recipient list @ "
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
			//System.out.println (" session close invoked in getSMSRecipient ");
			return result;
		}
	}
	
	public Object getSMSRecipientPastDue() {
		return getSMSRecipient("PastDue"); //Including today
	}
	
	public Object getSMSRecipientFutureDue() {
		return getSMSRecipient("FutureDue"); //Including today
	}

	public Object getSMSRecipientDue() {
		return getSMSRecipient("All"); //Including today
	}
	/*
	 * ------------------------------------------------------------------------
	 */

	public static void main(String[] args) { // TODO
		
	  System.out.println(" Recipient list - main. "); 
	  SMSRecipientProvider sMSRecipientProvider = new SMSRecipientProvider();
	  
	  List result = (List) sMSRecipientProvider.getSMSRecipientDue();
	  if ((result != null) && (result.size() > 0)) 
	  { for (Object object : result) { 
		  Map row = (Map) object; System.out.println(" ");
		  System.out.println ("    Receivable Amount: " + row.get("ReceivableAmount"));
		  System.out.println ("    DeferredDate: " + row.get("DeferredDate"));
		  System.out.println ("    DueDate: " + row.get("DueDate"));
		  System.out.println ("    AdjustedDueDate: " + row.get("AdjustedDueDate"));
		  System.out.println ("    ReceivedAmount: " + row.get("ReceivedAmount"));
		  System.out.println ("    BalanceDue: " + row.get("BalanceDue"));
		  System.out.println ("    InvoiceNumber: " + row.get("InvoiceNumber"));
		  System.out.println ("    CustomerName: " + row.get("CustomerName"));
		  System.out.println ("    SMSMobileNumber: " + row.get("SMSMobileNumber"));
		  System.out.println ("    SMSMobileOwnerName: " + row.get("SMSMobileOwnerName"));
		  System.out.println ("    MobileNumber: " + row.get("MobileNumber"));
		  System.out.println ("    ResidencePhoneNumber: " + row.get("ResidencePhoneNumber"));
		  System.out.println ("    CustId: " + row.get("CustId"));
	  	} 
	  }

	  result = (List) sMSRecipientProvider.getSMSRecipientPastDue();
	  if ((result != null) && (result.size() > 0)) 
	  { for (Object object : result) { 
		  Map row = (Map) object; System.out.println(" ");
		  System.out.println ("    Receivable Amount: " + row.get("ReceivableAmount"));
		  System.out.println ("    DeferredDate: " + row.get("DeferredDate"));
		  System.out.println ("    DueDate: " + row.get("DueDate"));
		  System.out.println ("    AdjustedDueDate: " + row.get("AdjustedDueDate"));
		  System.out.println ("    ReceivedAmount: " + row.get("ReceivedAmount"));
		  System.out.println ("    BalanceDue: " + row.get("BalanceDue"));
		  System.out.println ("    InvoiceNumber: " + row.get("InvoiceNumber"));
		  System.out.println ("    CustomerName: " + row.get("CustomerName"));
		  System.out.println ("    SMSMobileNumber: " + row.get("SMSMobileNumber"));
		  System.out.println ("    SMSMobileOwnerName: " + row.get("SMSMobileOwnerName"));
		  System.out.println ("    MobileNumber: " + row.get("MobileNumber"));
		  System.out.println ("    ResidencePhoneNumber: " + row.get("ResidencePhoneNumber"));
		  System.out.println ("    CustId: " + row.get("CustId"));
	  	} 
	  }

	  
	  result = (List) sMSRecipientProvider.getSMSRecipientFutureDue();
	  if ((result != null) && (result.size() > 0)) 
	  { for (Object object : result) { 
		  Map row = (Map) object; System.out.println(" ");
		  System.out.println ("    Receivable Amount: " + row.get("ReceivableAmount"));
		  System.out.println ("    DeferredDate: " + row.get("DeferredDate"));
		  System.out.println ("    DueDate: " + row.get("DueDate"));
		  System.out.println ("    AdjustedDueDate: " + row.get("AdjustedDueDate"));
		  System.out.println ("    ReceivedAmount: " + row.get("ReceivedAmount"));
		  System.out.println ("    BalanceDue: " + row.get("BalanceDue"));
		  System.out.println ("    InvoiceNumber: " + row.get("InvoiceNumber"));
		  System.out.println ("    CustomerName: " + row.get("CustomerName"));
		  System.out.println ("    SMSMobileNumber: " + row.get("SMSMobileNumber"));
		  System.out.println ("    SMSMobileOwnerName: " + row.get("SMSMobileOwnerName"));
		  System.out.println ("    MobileNumber: " + row.get("MobileNumber"));
		  System.out.println ("    ResidencePhoneNumber: " + row.get("ResidencePhoneNumber"));
		  System.out.println ("    CustId: " + row.get("CustId"));
	  	} 
	  }

	}
}
