package com.svnitsai.gm.database.provider;

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
 * SMSTemplateCRUDProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Performs CRUD operation on SMSTemplate table
 * 
 */

public class SMSTemplateCRUDProvider {
	
	/*
	 * Create SMSTemplate Record
	 */
	public int createSMSTemplate (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		Smstemplate SMSTemplate = (Smstemplate) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " Insert into SMSTemplate "
					+ " ( TemplateName "
					+ " , Template "
					+ " , CreatedDate "
					+ " , CreatedBy  "
					+ " , UpdatedDate  "
					+ " , UpdatedBy ) "
					+ " values "
					+ " ( :hostTemplateName "
					+ " , :hostTemplate "
					+ " , :hostCreatedDate "
					+ " , :hostCreatedBy "
					+ " , :hostUpdatedDate "
					+ " , :hostUpdatedBy )";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostTemplateName", SMSTemplate.getTemplateName())
					.setParameter("hostTemplate", SMSTemplate.getTemplate())
					.setParameter("hostCreatedDate", SMSTemplate.getCreatedDate())
					.setParameter("hostCreatedBy", SMSTemplate.getCreatedBy())
					.setParameter("hostUpdatedDate", SMSTemplate.getUpdatedDate())
					.setParameter("hostUpdatedBy", SMSTemplate.getUpdatedBy());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! Insert into SMSTemplate failed for TemplateId: " + SMSTemplate.getTemplateId()
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Insert into SMSTemplate was successful for TemplateId: " + SMSTemplate.getTemplateId()  
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
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
	 * Read ALL records on SMSTemplate table
	 */
	
	public Object getAllSMSTemplateList () {
		return getSMSTemplateList (0);
	}
	/*
	 * Read operation on SMSTemplate table based on given TemplateId
	 */
	
	public Object getSMSTemplateList (long templateId) {

		Session session = HibernateUtil.getSession();
		List result = null;
		try {
			Query query = null;
			if (templateId == 0) { //get all templates
				String SQL_QUERY = " Select TemplateId "
						+ " , TemplateName "
						+ " , Template "
						+ " , CreatedDate "
						+ " , CreatedBy  "
						+ " , UpdatedDate  "
						+ " , UpdatedBy  "
						+ " from SMSTemplate ";
				query = session.createSQLQuery(SQL_QUERY);
			} else {
				String SQL_QUERY = " Select TemplateId "
						+ " , TemplateName "
						+ " , Template "
						+ " , CreatedDate "
						+ " , CreatedBy  "
						+ " , UpdatedDate  "
						+ " , UpdatedBy  "
						+ " from SMSTemplate "
						+ " where TemplateId = :hostTemplateId ";
				query = session.createSQLQuery(SQL_QUERY).setParameter("hostTemplateId", templateId);
			}
			/* Run as native SQL query*/
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on SMSTemplate; for TemplateId: " + templateId  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on SMSTemplate; for TemplateId: " + templateId  + "   @ "
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
	 * Update operation on SMSTemplate
	 */
	
	public int updateSMSTemplate (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		Smstemplate SMSTemplate = (Smstemplate) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " UPDATE SMSTemplate "
					+ "  set Template = :hostTemplate "
					+ ", UpdatedDate = :hostUpdatedDate "
					+ ", UpdatedBy = :hostUpdatedBy "
					+ " where TemplateId = :hostTemplateId";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostTemplateId", SMSTemplate.getTemplateId())
					.setParameter("hostTemplate", SMSTemplate.getTemplate())
					.setParameter("hostUpdatedDate", SMSTemplate.getUpdatedDate())
					.setParameter("hostUpdatedBy", SMSTemplate.getUpdatedBy());

			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! No update to SMSTemplate for TemplateId: " + SMSTemplate.getTemplateId()
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Updated " + result + " record(s) in SMSTemplate for TemplateId: " + SMSTemplate.getTemplateId()
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
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
	 * Delete operation on SMSTemplate
	 */
	
	public int deleteSMSTemplate (Object dataObject) {
		
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		Smstemplate SMSTemplate = (Smstemplate) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			
			String SQL_QUERY = " DELETE from SMSTemplate "
					+ " where TemplateId = :hostTemplateId";
			
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY)
					.setParameter("hostTemplateId", SMSTemplate.getTemplateId());
			
			int result = query.executeUpdate(); //returns number of rows affected
			
			if (result == 0) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" FAILED !!! No records to DELETE from SMSTemplate for TemplateId: " + SMSTemplate.getTemplateId()  
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
						+ "   @ " + DateUtil.getCurrentTimestamp().toString());
			} else {
				LogUtil.log(LogUtil.Message_Type.Information,
						" SUCCESS!!! Deleted " + result + " record(s) from SMSTemplate for TemplateId: " + SMSTemplate.getTemplateId()  
						+ "; SMS Template name: " + SMSTemplate.getTemplateName()
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
	

//	public static void main(String[] args) {
//
//
//		SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();

//		Smstemplate SMSTemplate = new Smstemplate();
//		//SMSTemplate.setTemplateName("Past Due");
//		//SMSTemplate.setTemplate("Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment which was due on [due_date] is not settled. Pls. settle.");
//		SMSTemplate.setTemplateName("Immediate Due");
//		SMSTemplate.setTemplate("Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment which will be due on [due_date] is not settled. Pls. settle.");
//		SMSTemplate.setCreatedDate(DateUtil.getCurrentTimestamp());
//		SMSTemplate.setCreatedBy("Admin");
//		SMSTemplate.setUpdatedDate(DateUtil.getCurrentTimestamp());
//		SMSTemplate.setUpdatedBy("Admin");
//
//		int returnCode = SMSTemplateInfoCrud.createSMSTemplate(SMSTemplate);
			  
//		  Smstemplate SMSTemplate = new Smstemplate(); // set template name too
//		  SMSTemplate.setTemplateId(2);
//		  SMSTemplate.setTemplate("Dear Mr.[contact_name], with reference to invoice number [invoice_number].  Your [due_amount] payment is due on [due_date]. This is a reminder. Thank you.");
//		  SMSTemplate.setUpdatedDate(DateUtil.getCurrentTimestamp());
//		  SMSTemplate.setUpdatedBy("Admin");
//
//		  int returnCode = SMSTemplateInfoCrud.updateSMSTemplate(SMSTemplate);
//			  
//	        List result = (List) SMSTemplateInfoCrud.getSMSTemplateList(2);
//			  if ((result != null) && (result.size() > 0)) { 
//				  for (Object object : result) { 
//					  Map row = (Map) object; System.out.println(" ");
//					  System.out.println(" Templateid: " + row.get("TemplateId")
//							  + " TemplateName: " + row.get("TemplateName")
//							  + " Template: " + row.get("Template")
//							  + " CreatedDate: " + row.get("CreatedDate")
//							  + " CreatedBy: " + row.get("CreatedBy")
//							  + " UpdatedDate: " + row.get("UpdatedDate")
//							  + " UpdatedBy: " + row.get("UpdatedBy")
//							  ); 
//					  } 
//				  }
				
//			SMSTemplateId SMSTemplateId = new SMSTemplateId();
//			SMSTemplateId.setCustBankId(67);
//			SMSTemplateId.setCustCode(1081);
//			SMSTemplate SMSTemplate = new SMSTemplate();
//			SMSTemplate.setId(SMSTemplateId);
//			SMSTemplate.setCustBank("CanaraBank");
//			SMSTemplate.setCustBankBranch("New Delhi");
//			SMSTemplate.setCustBankAccountType("Savings");
//			SMSTemplate.setCustBankAccountNumber("12345 12345");
//			SMSTemplate.setCustBankAccountName("Mr.Samysosa");
//			SMSTemplate.setCreatedDate(DateUtil.getCurrentTimestamp());
//			SMSTemplate.setCreatedBy("Program");
//			SMSTemplate.setUpdatedDate(DateUtil.getCurrentTimestamp());
//			SMSTemplate.setUpdatedBy("UpdatePgm");
//		
//			SMSTemplateInfoCRUDProvider SMSTemplateInfoCrud = new SMSTemplateInfoCRUDProvider();
//			int returnCode = SMSTemplateInfoCrud.deleteSMSTemplate(SMSTemplate);
//	
//	
//	
//	}

}
