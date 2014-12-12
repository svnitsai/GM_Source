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
 * LandingPageWindow.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Reads Database to get different window details for Landing page
 * 
 */

public class LandingPageWindows {

	/*
	 * getBalancePayableWindow()
	 * 
	 * @purpose: Reads database to get outstanding payable amount; Query finds
	 * the maximum date before today, on which Payable record exists (say
	 * BalanceDueDate); For that BalanceDueDate, it computes the Paid amount to
	 * Supplier (excluding deleted records) If that paid amount is less than
	 * Payable Amount its picked up for display
	 * 
	 * Returns a list of Object in a MAP
	 */
	public Object getBalancePayableWindow() {
		/* List that returns result */
		List result = null;
		try {
			Session session = HibernateUtil.getSession();

			String SQL_QUERY = " Select Cust.CustName as SupplierName, SupplierPayable.PayableAmount as PayableAmount "
					+ " , ISNULL(SupplierPaid.PaidAmount,0) as PaidAmount, SupplierPayable.PayableDate as BalanceDueDate "
					+ " , (SupplierPayable.PayableAmount - ISNULL(SupplierPaid.PaidAmount,0)) as BalanceDue, SupplierPayable.SupplierCode as SupplierCode "
					+ " from ( ";

			/* Get Supplier Payable Information */
			SQL_QUERY = SQL_QUERY
					+ " Select DP.SupplierCode, DP.PayableAmount, CONVERT(VARCHAR(10),DP.PayableDate,111) as PayableDate "
					+ " 	  from dbo.DailyPayable DP ";

			/* Subquery gets MAX date before today with Payable Information */
			SQL_QUERY = SQL_QUERY
					+ "	where CONVERT(VARCHAR(10),DP.PayableDate,111) = (Select CONVERT(VARCHAR(10), MAX(DPMAX.PayableDate),111) from dbo.DailyPayable DPMAX WHERE CONVERT(VARCHAR(10), DPMAX.PayableDate,111) < CONVERT(VARCHAR(10), GETDATE(),111)) "
					+ ") as SupplierPayable " + "  LEFT OUTER JOIN ";
			/* Get Supplier Paid Information */
			SQL_QUERY = SQL_QUERY
					+ "  ( "
					+ "  Select sum(DPC.PaidAmount) as PaidAmount, DPC.SupplierCode "
					+ "   from dbo.DailyPayCDetails DPC "
					+ "	where CONVERT(VARCHAR(10),DPC.PayCDate,111) = (Select CONVERT(VARCHAR(10), MAX(DPMAX.PayableDate),111) from dbo.DailyPayable DPMAX WHERE CONVERT(VARCHAR(10), DPMAX.PayableDate,111) < CONVERT(VARCHAR(10), GETDATE(),111)) "
					+ "	and DPC.Deleted != 1 "
					+ "	group by DPC.SupplierCode "
					+ "  ) as SupplierPaid "
					+ "  on SupplierPayable.SupplierCode = SupplierPaid.SupplierCode "
					+ "  LEFT OUTER JOIN ";
			/* Get Supplier Information */
			SQL_QUERY = SQL_QUERY + "  ( " + "  Select C.CustCode, C.CustName "
					+ "    from dbo.Customer C "
					+ "   where C.CustType = 'Supplier' " + "  ) as Cust "
					+ "  on SupplierPayable.SupplierCode = Cust.CustCode ";
			SQL_QUERY = SQL_QUERY
					/* Filtered Unpaid Payable records */
					+ "  where SupplierPayable.PayableAmount <> SupplierPaid.PaidAmount "
					+ " or SupplierPaid.PaidAmount IS NULL;";

			/* Run as NATIVE SQL Query */
			Query query = session.createSQLQuery(SQL_QUERY);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on BalancePayableWindow @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on BalancePayableWindow @ "
						+ DateUtil.getCurrentTimestamp().toString());
				for (Object object : result) {
					Map row = (Map) object;
					LogUtil.log(
							LogUtil.Message_Type.Information,
							" Supplier Name: " + row.get("SupplierName")
									+ ", Payable Amount: "
									+ row.get("PayableAmount")
									+ ", Paid Amount: " + row.get("PaidAmount")
									+ ", Balance Due: " + row.get("BalanceDue")
									+ ", BalanceDueDate: "
									+ row.get("BalanceDueDate"));
				}
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	/*
	 * getCreditSalesWindow()
	 * 
	 * @purpose: Reads database to get outstanding RECEIVABLE amount - DailyPayC
	 * is driver table; Query uses AdjustedDueDate (which is DefferedDate if
	 * exists, else DueDate) Any record with ReceivableStatus other than OPEN
	 * are pulled in for display. Includes DailyPayC record even when
	 * corresponding "DailyPayCDetails not existing"
	 * 
	 * Returns a list of Object in a MAP
	 */
	public Object getCreditSalesWindow() {
		/* List that returns result */
		List result = null;
		try {
			Session session = HibernateUtil.getSession();
			
			String SQL_QUERY = "Select Receivable.PayCReferenceNumber as ReferenceNumber, Receivable.InvoiceAmount as ReceivableAmount "
						     + " , Receivable.DeferredDate as DeferredDate, Receivable.PayCDueDate as DueDate "
                             + "  , Receivable.AdjustedDueDate as AdjustedDueDate, Received.PaidAmount as ReceivedAmount "
                             + "  , (Receivable.InvoiceAmount - ISNULL(Received.PaidAmount,0)) as BalanceDue "
                             + "  , Cust.CustName as CustomerName "
                             + " from " 
                             + " ( ";
			/* Get Merchant Receivable Information */
			SQL_QUERY = SQL_QUERY + " Select DPC.PayCReferenceNumber, DPC.InvoiceAmount, DPC.PayCStatus "
					+ " , CONVERT(VARCHAR(10),DPC.PayCDueDate,111) as PayCDueDate  "
					+ " , CONVERT(VARCHAR(10),DPC.DeferredDate,111) as DeferredDate  "
					+ " , ISNULL(CONVERT(VARCHAR(10),DPC.DeferredDate,111),CONVERT(VARCHAR(10),DPC.PayCDueDate,111)) as AdjustedDueDate  "
					+ " , DPC.CustCode  "
					+ " from dbo.DailyPayC DPC  "
					+ " where (    DPC.PayCStatus <> 'CLOSED'  "
					+ " 	 or DPC.PayCStatus IS NULL )  "
					+ " and (ISNULL(CONVERT(VARCHAR(10),DPC.DeferredDate,111),CONVERT(VARCHAR(10),DPC.PayCDueDate,111))  "
					+ "   <= CONVERT(VARCHAR(10), GETDATE(),111))  " 
					+ " ) as Receivable  "
					+ " LEFT OUTER JOIN "
					+ " ( ";
            /* Get Merchant Paid Information */
			SQL_QUERY = SQL_QUERY +  "	  Select DPCD.PayCReferenceNumber, SUM(ISNULL(DPCD.PaidAmount,0))as PaidAmount "
					+ "   from dbo.DailyPayCDetails DPCD  "
					+ "  where DPCD.Deleted != 1  "
					+ " group by DPCD.PayCReferenceNumber  " 
					+ " ) as Received  "
					+ " ON Receivable.PayCReferenceNumber = Received.PayCReferenceNumber  "
					+ " LEFT OUTER JOIN ";
			/* Get Merchant Information */    
			SQL_QUERY = SQL_QUERY +  " ( "
					+ "   Select C.CustCode, C.CustName + ', ' + C.CustCity as CustName "
					+ " from dbo.Customer C "
					+ " where C.CustType = 'Merchant' " 
					+ " ) as Cust  "
					+ " on Receivable.CustCode = Cust.CustCode; ";

			/* Run as NATIVE SQL Query */
			Query query = session.createSQLQuery(SQL_QUERY);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on CreditSalesWindow @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on CreditSalesWindow @ "
						+ DateUtil.getCurrentTimestamp().toString());
				for (Object object : result) {
					Map row = (Map) object;
					LogUtil.log(
							LogUtil.Message_Type.Information,
							" Merchant Name: " + row.get("CustomerName")
									+ ", Receivable Amount: "
									+ row.get("ReceivableAmount")
									+ ", Received Amount: "
									+ row.get("ReceivedAmount")
									+ ", Balance Due: "
									+ row.get("BalanceDue")
									+ ", Due Date: "
									+ row.get("AdjustedDueDate"));
				}
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(" Balance Due Window - main. ");
		LandingPageWindows landingPageWindow = new LandingPageWindows();
	
		List result = (List) landingPageWindow.getBalancePayableWindow();
		if ((result != null) && (result.size() > 0)) {
			for (Object object : result) {
				Map row = (Map) object;
				System.out.println(" ");
				System.out.print("Supplier Name: " + row.get("SupplierName"));
				System.out.print(", Payable Amount: "
						+ Util.convertToDouble((String) row
								.get("PayableAmount")));
				System.out.print(", Paid Amount: " + row.get("PaidAmount"));
			}
		}

		List result2 = (List) landingPageWindow.getCreditSalesWindow();
		if ((result2 != null) && (result2.size() > 0)) {
			for (Object object : result2) {
				Map row = (Map) object;
				System.out.println(" ");
				System.out.print(" Merchant Name: " + row.get("CustomerName"));
				System.out.print(", Payable Amount: " + row.get("InvoiceAmount"));
				System.out.print(", Due Date: " + row.get("AdjustedDueDate"));
				System.out.print(", Balance Due: " + row.get("BalanceDue"));
			}
		}

	
	}

}
