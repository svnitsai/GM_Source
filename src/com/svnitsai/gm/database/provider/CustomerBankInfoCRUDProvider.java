package com.svnitsai.gm.database.provider;

import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.svnitsai.gm.database.generated.*;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * CustomerBankInfoCRUDProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Performs CRUD operation on CustomerBanks table
 * 
 */

public class CustomerBankInfoCRUDProvider {
	
	/*
	 * Create CustomerBanks Record
	 */
	public int createCustomerBank (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		CustomerBanks customerBank = (CustomerBanks) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " Insert into CustomerBanks ( CustCode,  CustBank"
					+ " , CustBankBranch, CustBankAccountType "
					+ " , CustBankAccountNumber, CustBankAccountName "
					+ " , CreatedDate, CreatedBy, UpdatedDate, UpdatedBy ) "
					+ " values (:hostCustCode, :hostCustBank "
					+ " , :hostCustBankBranch, :hostCustBankAccountType "
					+ " , :hostCustBankAccountNumber, :hostCustBankAccountName "
					+ " , :hostCreatedDate, :hostCreatedBy "
					+ " , :hostUpdatedDate, :hostUpdatedBy "
					+ " ) ";
					
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostCustCode", customerBank.getId().getCustCode())
					.setParameter("hostCustBank", customerBank.getCustBank())
					.setParameter("hostCustBankBranch", customerBank.getCustBankBranch())
					.setParameter("hostCustBankAccountType", customerBank.getCustBankAccountType())
					.setParameter("hostCustBankAccountNumber", customerBank.getCustBankAccountNumber())
					.setParameter("hostCustBankAccountName", customerBank.getCustBankAccountName())
					.setParameter("hostCreatedDate", customerBank.getCreatedDate())
					.setParameter("hostCreatedBy", customerBank.getCreatedBy())
					.setParameter("hostUpdatedDate", customerBank.getUpdatedDate())
					.setParameter("hostUpdatedBy", customerBank.getUpdatedBy());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! Insert into Customer Bank failed for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Insert into Customer Bank was successful for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
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
	 * Read operation on CustomerBanks table based on given customerCode
	 */
	
	public Object getCustomerBankList (long customerCode) {

		Session session = HibernateUtil.getSession();
		List result = null;

		try {
			String SQL_QUERY = " Select CB.CustBankId as CustBankId, CB.CustCode as CustCode, ISNULL(CB.CustBank,'') as CustBank "
					+ " , ISNULL(CB.CustBankBranch,'') as CustBankBranch, ISNULL(CB.CustBankAccountType,'') as CustBankAccountType "
					+ " , ISNULL(CB.CustBankAccountNumber,'') as CustBankAccountNumber, ISNULL(CB.CustBankAccountName,'') as CustBankAccountName "
					+ " , CB.UpdatedDate as UpdatedDate from CustomerBanks CB "
					+ " where CB.CustCode = :hostCustCode order by CB.CustBankId asc ";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY).setParameter("hostCustCode", customerCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on customerInfoProvider; for custCode: " + customerCode  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on customerInfoProvider; for custCode: " + customerCode  + "   @ "
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
			System.out.println(" System session closed in getCustomerBankInfo (SELECT) ");
			session.close();
			return result;
		}
	}
	
	/*
	 * Update operation on CustomerBanks
	 */
	
	public int updateCustomerBank (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		CustomerBanks customerBank = (CustomerBanks) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " UPDATE CustomerBanks "
					+ "  set CustBank = :hostCustBank "
					+ ", CustBankBranch = :hostCustBankBranch "
					+ ", CustBankAccountType = :hostCustBankAccountType "
					+ ", CustBankAccountNumber = :hostCustBankAccountNumber "
					+ ", CustBankAccountName = :hostCustBankAccountName "
					+ ", UpdatedDate = :hostUpdatedDate "
					+ ", UpdatedBy = :hostUpdatedBy "
					+ " where CustBankId = :hostCustBankId and CustCode = :hostCustCode";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostCustBankId", customerBank.getId().getCustBankId())
					.setParameter("hostCustCode", customerBank.getId().getCustCode())
					.setParameter("hostCustBank", customerBank.getCustBank())
					.setParameter("hostCustBankBranch", customerBank.getCustBankBranch())
					.setParameter("hostCustBankAccountType", customerBank.getCustBankAccountType())
					.setParameter("hostCustBankAccountNumber", customerBank.getCustBankAccountNumber())
					.setParameter("hostCustBankAccountName", customerBank.getCustBankAccountName())
					.setParameter("hostUpdatedDate", customerBank.getUpdatedDate())
					.setParameter("hostUpdatedBy", customerBank.getUpdatedBy());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! No update to Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Updated " + result + " record(s) in Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
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
	 * Delete operation on CustomerBanks
	 */
	
	public int deleteCustomerBank (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		CustomerBanks customerBank = (CustomerBanks) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " DELETE from CustomerBanks "
					+ " where CustBankId = :hostCustBankId and CustCode = :hostCustCode";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostCustBankId", customerBank.getId().getCustBankId())
					.setParameter("hostCustCode", customerBank.getId().getCustCode());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! No records to DELETE from Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Deleted " + result + " record(s) from Customer Bank for CustomerCode: " + customerBank.getId().getCustCode()  + "   @ "
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
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		HashMap <Integer, CustomerBanks> customerBanksHashMap = getCustomerList();
////	    Iterator iterator = customerBanksHashMap.entrySet().iterator();
////	    while (iterator.hasNext()) {
////	        Map.Entry pairs = (Map.Entry)iterator.next();
////	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
////	        //Can remove items
////	        //iterator.remove();
////	    }
//	    
//		//Iterate through the map
//	    Iterator<Map.Entry<Integer, CustomerBanks>> iterator = customerBanksHashMap.entrySet().iterator() ;
//	    System.out.println (" size of map received " + customerBanksHashMap.size());
//        while(iterator.hasNext()){
//            Map.Entry<Integer, CustomerBanks> customerBankEntry = iterator.next();
//            System.out.println(customerBankEntry.getKey() +" :: " 
//                     + " Customer Bank: " + customerBankEntry.getValue().getCustBank()
//                     + " Customer Branch: " + customerBankEntry.getValue().getCustBankBranch()
//                     + " Customer Account Type: " + customerBankEntry.getValue().getCustBankAccountType()
//                     + " Customer Account Name: " + customerBankEntry.getValue().getCustBankAccountName()
//                     + " Customer Account Number: " + customerBankEntry.getValue().getCustBankAccountNumber()
//                     );
//            //You can remove elements while iterating.
//            //iterator.remove();
//        }
//        
//
//		CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
//        List result = (List) customerBankInfoCrud.getCustomerBankList(1081);
//		  if ((result != null) && (result.size() > 0)) { 
//			  for (Object object : result) { 
//				  Map row = (Map) object; System.out.println(" ");
//				  System.out.println(" CustBank: " + row.get("CustBank")
//						  + " CustBankBranch: " + row.get("CustBankBranch")
//						  + " CustBankAccountType: " + row.get("CustBankAccountType")
//						  + " CustBankAccountNumber: " + row.get("CustBankAccountNumber")
//						  + " CustBankAccountName: " + row.get("CustBankAccountName")
//						  ); 
//				  } 
//			  }
	
//			CustomerBanksId customerBanksId = new CustomerBanksId();
//			customerBanksId.setCustCode(1081);
//			CustomerBanks customerBank = new CustomerBanks();
//			customerBank.setId(customerBanksId);
//			customerBank.setCustBank("CanaraBank");
//			customerBank.setCustBankBranch("New Delhi");
//			customerBank.setCustBankAccountType("Savings");
//			customerBank.setCustBankAccountNumber("12345 12345");
//			customerBank.setCustBankAccountName("Mr.Samysosa");
//			customerBank.setCreatedDate(DateUtil.getCurrentTimestamp());
//			customerBank.setCreatedBy("Program");
//			customerBank.setUpdatedDate(DateUtil.getCurrentTimestamp());
//			customerBank.setUpdatedBy("Program");
//		
//			CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
//			int returnCode = customerBankInfoCrud.createCustomerBank(customerBank);
				  
//			CustomerBanksId customerBanksId = new CustomerBanksId();
//			customerBanksId.setCustBankId(67);
//			customerBanksId.setCustCode(1081);
//			CustomerBanks customerBank = new CustomerBanks();
//			customerBank.setId(customerBanksId);
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
//			CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
//			int returnCode = customerBankInfoCrud.updateCustomerBank(customerBank);
				  
//			CustomerBanksId customerBanksId = new CustomerBanksId();
//			customerBanksId.setCustBankId(67);
//			customerBanksId.setCustCode(1081);
//			CustomerBanks customerBank = new CustomerBanks();
//			customerBank.setId(customerBanksId);
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
//			CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
//			int returnCode = customerBankInfoCrud.deleteCustomerBank(customerBank);
//	
//	
//	
	}

}
