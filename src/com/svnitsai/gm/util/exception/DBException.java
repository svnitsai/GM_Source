package com.svnitsai.gm.util.exception;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

import com.svnitsai.gm.util.log.LogUtil;
import com.svnitsai.gm.util.log.LogUtil.Message_Type;

public class DBException {

	public static final int DB_SUCCESSFUL = 0;
	public static final int DB_SQL_EXCEPTION = 1;
	public static final int DB_OTHER_HIBERNATE_EXCEPTION = 2;
	
	public static int HandleJDBCException(JDBCException e) {
		LogUtil.log(Message_Type.Error, " SQL State is " + e.getSQLState() + " SQL error code is " + e.getErrorCode());
		//LogUtil.log(Message_Type.Error, e.getSQLException().getNextException().getMessage());
		//LogUtil.log(Message_Type.Error, e.getCause().getMessage());
		e.printStackTrace();
		return DB_SQL_EXCEPTION;
	}
	
	public static int HandleHibernateException (HibernateException e) {
		e.printStackTrace();
		return DB_OTHER_HIBERNATE_EXCEPTION;
	}
}