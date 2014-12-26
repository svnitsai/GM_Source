package com.svnitsai.gm.database.provider;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;
import com.svnitsai.gm.util.date.DateUtil;

/*
 * StoredProcedureProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Call Database Stored Procedures
 * 
 */

public class StoredProcedureProvider {

	/*
	 * callCreditSalesDataRefresh()
	 * 
	 * @purpose: Call CS_CSALESEXTRACT stored procedure to refresh the data for
	 * CreditSales
	 * 
	 * Returns a list of Object in a MAP
	 */
	public Object callCreditSalesDataRefresh() {
		/* List that returns result */
		List result = null;
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		try {
			LogUtil.log(LogUtil.Message_Type.Information,
					" Credit Sales Data Refresh Started @ "
							+ DateUtil.getCurrentTimestamp().toString());

			Query query = session.createSQLQuery("EXEC CS_CSALESEXTRACT ");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" Credit Sales Data - No return value from Stored Procedure @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				for (Object object : result) {
					Map row = (Map) object;
					LogUtil.log(LogUtil.Message_Type.Information,
							" Credit Sales Data - Stored Procedure returned @ "
									+ DateUtil.getCurrentTimestamp().toString()
									+ " Return Code: " + row.get("ReturnValue"));
				}
			}

			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			transaction.rollback();
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			transaction.rollback();
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			return result;
		}
	}

	/*
	 * callCreditSalesDataRefresh()
	 * 
	 * @purpose: Call CS_CSALESEXTRACT stored procedure to refresh the data for
	 * CreditSales
	 * 
	 * Returns a list of Object in a MAP
	 */
	public Object callCustomerDataRefresh() {
		/* List that returns result */
		List result = null;
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		try {
			LogUtil.log(LogUtil.Message_Type.Information,
					" Customer Data Refresh Started @ "
							+ DateUtil.getCurrentTimestamp().toString());

			Query query = session.createSQLQuery("EXEC CS_PARTYEXTRACT ");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" Customer Data - No return value from Stored Procedure @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				for (Object object : result) {
					Map row = (Map) object;
					LogUtil.log(LogUtil.Message_Type.Information,
							" Customer Data - Stored Procedure returned @ "
									+ DateUtil.getCurrentTimestamp().toString()
									+ " Return Code: " + row.get("ReturnValue"));
				}
			}
			
			transaction.commit();
			
		} catch (JDBCException e) {
			transaction.rollback();
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			transaction.rollback();
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			transaction.rollback();
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			return result;
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 */

	/*public static void main(String[] args) {
		// TODO:

		System.out.println(" Call Stored procedure. ");
		/*
		 * LandingPageWindows landingPageWindow = new LandingPageWindows();
		 * 
		 * List result = (List) landingPageWindow.getBalancePayableWindow(); if
		 * ((result != null) && (result.size() > 0)) { for (Object object :
		 * result) { Map row = (Map) object; System.out.println(" ");
		 * System.out.print("Supplier Name: " + row.get("SupplierName"));
		 * System.out.print(", Payable Amount: " + Util.convertToDouble((String)
		 * row .get("PayableAmount"))); System.out.print(", Paid Amount: " +
		 * row.get("PaidAmount")); } }
		 */

		//StoredProcedureProvider SPCall = new StoredProcedureProvider();
		//SPCall.callCreditSalesDataRefresh();

		//SPCall.callCustomerDataRefresh();
	//}

}
