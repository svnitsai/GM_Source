package com.svnitsai.gm.util.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.svnitsai.gm.util.date.DateUtil;

/*
 * HibernateUtil.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Uses Singleton Pattern to create database factory
 * 
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;
	private static boolean sessionFactoryAvailable = false; //Indicates if SessionFactory is created

	/**
	 * @return the sessionFactoryAvailable
	 */
	private static boolean isSessionFactoryAvailable() {
		return sessionFactoryAvailable;
	}

	/**
	 * @param sessionFactoryAvailable the sessionFactoryAvailable to set
	 */
	private static void setSessionFactoryAvailable(boolean sessionFactoryAvailable) {
		HibernateUtil.sessionFactoryAvailable = sessionFactoryAvailable;
	}

	/*
	 * CreateSessionFactory if one is not available already
	 */
	public static void createSessionFactory() {
		
		if (!isSessionFactoryAvailable()) {
			System.out.println (" CREATESESSIONFACTORY Hibernat Util loading @ " + DateUtil.getCurrentTimestamp().toString());
			try {
				Configuration configuration = new Configuration()
						.configure("hibernate.cfg.xml");
				StandardServiceRegistryBuilder standardSRB = new StandardServiceRegistryBuilder();
				standardSRB.applySettings(configuration.getProperties());
				StandardServiceRegistry standardServiceRegistry = standardSRB
						.build();
				sessionFactory = configuration
						.buildSessionFactory(standardServiceRegistry);
			} catch (Throwable ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				throw new ExceptionInInitializerError(ex);
			}

			try {
				session = sessionFactory.openSession();
			} catch (HibernateException ex) {
				ex.printStackTrace();
			}
			
			setSessionFactoryAvailable(true);
		}
		
	}
	
	/*
	 * Get Session factory - create a new session factory if one is not available
	 */

	public static SessionFactory getSessionFactory() {
		// Alternatively, you could look up in JNDI here
		System.out.println(" inside Hibernate.Util.getSessionFactory ");
		if (isSessionFactoryAvailable()) createSessionFactory();
		return sessionFactory;
	}
	
	/*
	 * Get Session - open a session if not available
	 */

	public static Session getSession() {
		System.out.println(" inside Hibernate.Util.getSession ");
		createSessionFactory();
		if (!session.isOpen()) session = sessionFactory.openSession();
		return session;
	}
	
	/*
	 * Close open sessions and sessionfactory 
	 */

	public static void shutdown() {
		// Close caches and connection pools
		System.out.println(" inside Hibernate.Util.shutdown ");

		getSession().close();
		getSessionFactory().close();
		setSessionFactoryAvailable(false);
	}
}
