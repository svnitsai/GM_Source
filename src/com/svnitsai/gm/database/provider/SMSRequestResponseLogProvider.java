package com.svnitsai.gm.database.provider;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.svnitsai.gm.database.generated.SmsrequestResponse;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;
/*
 * SMSRequestResponseLogProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Performs CRUD operation on SMSRequestResponse table aka log table
 * 
 */
public class SMSRequestResponseLogProvider {

	/*
	 * Create SMS Request-Response log Record
	 */
	public int createRequestResponseLog (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		SmsrequestResponse smsrequestResponse = (SmsrequestResponse) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " INSERT INTO SMSRequestResponse (RequestInitiatedTS "
					+ " , Request, Response ) "
					+ " values ( :hostRequestInitiatedTS , :hostRequest, :hostResponse "
					+ " ) ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostRequestInitiatedTS", smsrequestResponse.getRequestInitiatedTs())
					.setParameter("hostRequest", smsrequestResponse.getRequest())
					.setParameter("hostResponse", smsrequestResponse.getResponse())
					;
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! Insert into Request Response log failed for RequestInitiatedTS : " 
								+ smsrequestResponse.getRequestInitiatedTs().toString()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Insert into Request Response log was successful for RequestInitiatedTS: " 
								+ smsrequestResponse.getRequestInitiatedTs().toString()  + "   @ "
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
	
	public void splitString(String request, int splitSize, String [] requests) {
		int requestNumber = 0;
		while ((request.length() > splitSize) && (requestNumber < requests.length)) {
			requests [requestNumber] = request.substring(0, splitSize);
			requestNumber++;
			request = request.substring(splitSize);
		}
		if (requestNumber < requests.length) requests [requestNumber] = request;
	}

//	public static void main(String[] args) {
//
//        
//		SmsrequestResponse smsRequestResponse = new SmsrequestResponse();
//		smsRequestResponse.setRequestInitiatedTs(DateUtil.getCurrentTimestamp());
//		smsRequestResponse.setRequest("Request string");
//		smsRequestResponse.setResponse("Response string");
//	
//		SMSRequestResponseLogProvider sMSRequestResponseLogProvider = new SMSRequestResponseLogProvider();
//		int returnCode = sMSRequestResponseLogProvider.createRequestResponseLog(smsRequestResponse);
//		
//		//Shutdown Hibernate SessionFactory
//		HibernateUtil.shutdown();
//		String inString = "12345678901234567890123456789012345678901234567890";
//		String [] outString = new String [6];
//		splitString (inString, 150, outString);
//		System.out.println(" In string " + inString);
//		for (int loop=0; loop < 6; loop++) {
//			System.out.println(" Out string " + loop + " " + outString[loop]);
//		}

//	}

}
