package com.svnitsai.gm.database.provider;

import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.database.generated.Useraccess;

/*
 * UserAccessProviderImpl.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	useraccess table access;
 * 
 */

public class UserAccessProviderImpl implements IPostgresProvider {

	@Override
	public int Create(Object dataObject) {
		return Create(dataObject, false);
	}

	@SuppressWarnings("finally")
	@Override
	public Object ReadById(int dataObjectId) {
		Useraccess userAccess = new Useraccess();
		try {
			System.out.println("Inside ReadById");
			Session session = HibernateUtil.getSession();
			String SQL_QUERY = "Select userid, username, password, role, createddate, createdby, updateddate, updatedby from Useraccess where userid = :hostUserId";
			Query query = session.createQuery(SQL_QUERY).setParameter(
					"hostUserId", dataObjectId);
			Iterator it = query.iterate();
			if (!(it.hasNext())) {
				userAccess = null;
			} else {
				Object[] row = (Object[]) it.next();
				userAccess = getUseraccessFromBean(row);
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			return userAccess;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public ScrollableResults ReadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int Update(Object dataObject) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int Delete(Object dataObject) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int DeleteAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	/***
	 * ------------------------------------------------------------------------
	 * ----------
	 ***/

	public int Create(Object dataObject, boolean saveOrUpdate) {
		int returnCode = DBException.DB_SUCCESSFUL;
		Transaction transaction = null;
		Session session = null;
		Useraccess userAccess = (Useraccess) dataObject;
		try {
			session = HibernateUtil.getSession();
			transaction = session.beginTransaction();
			if (saveOrUpdate)
				session.saveOrUpdate(userAccess);
			else
				session.save(userAccess);
		} catch (JDBCException e) {
			returnCode = DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			returnCode = DBException.HandleHibernateException(e);
		} finally {
			try {
				transaction.commit();
				session.flush();
				session.clear();
			} catch (JDBCException e) {
				returnCode = DBException.HandleJDBCException(e);
			} catch (HibernateException e) {
				returnCode = DBException.HandleHibernateException(e);
			}
		}
		return returnCode;
	}

	/*
	 * Retrieve Useraccess data by username - case insensitive retrieval
	 */
	public Object ReadByUsername(String userName) {
		Useraccess userAccess = new Useraccess();
		try {
			Session session = HibernateUtil.getSession();
			String SQL_QUERY = "Select userid, username, password, role, createddate, createdby, updateddate, updatedby from Useraccess as UA where upper(username) = upper(:hostUserName)";
			Query query = session.createQuery(SQL_QUERY).setParameter(
					"hostUserName", userName);
			Iterator it = query.iterate();
			if (!(it.hasNext())) {
				userAccess = null;
			} else {
				Object[] row = (Object[]) it.next();
				userAccess = getUseraccessFromBean(row);
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			return userAccess;
		}
	}

	/*
	 * Create a useraccess object from object
	 */
	private Useraccess getUseraccessFromBean(Object[] row) {
		Useraccess userAccess = new Useraccess();

		/*
		 * userid, username, password, role, createddate, createdby,
		 * updateddate, updatedby
		 */

		userAccess.setUserid(Integer.parseInt(row[0].toString()));
		userAccess.setUsername(row[1].toString());
		userAccess.setPassword(row[2].toString());
		userAccess.setRole(row[3].toString());
		userAccess.setCreateddate(DateUtil.getTimestampFromString(row[4]
				.toString()));
		userAccess.setCreatedby(row[5].toString());
		userAccess.setUpdateddate(DateUtil.getTimestampFromString(row[6]
				.toString()));
		userAccess.setUpdatedby(row[7].toString());

		return userAccess;
	}

	
}
