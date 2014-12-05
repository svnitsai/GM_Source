package com.svnitsai.gm.database.provider;

import org.hibernate.ScrollableResults;

/*
 * IPostgresProvider.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Interface that defines database table access;
 * 
 */

public interface IPostgresProvider {
	
	public int Create(Object dataObject);

	public Object ReadById(int dataObjectId);
	public ScrollableResults ReadAll();
	
	public int Update(Object dataObject);
	public int Delete (Object dataObject);
	
	public int DeleteAll();

}
