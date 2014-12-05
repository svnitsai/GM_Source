package com.svnitsai.gm.util.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.svnitsai.gm.util.exception.DBException;

/**
 * @author Senthil
 *
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Session session;

	static {
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
	}

	public static SessionFactory getSessionFactory() {
		// Alternatively, you could look up in JNDI here
		return sessionFactory;
	}

	public static Session getSession() {
		return session;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSession().close();
		getSessionFactory().close();
	}
}
