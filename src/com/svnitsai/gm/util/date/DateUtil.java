package com.svnitsai.gm.util.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * DateUtil.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Date manipulation routines;
 * 
 */

public class DateUtil {
	/*
	 * Get Current timestamp
	 */
	public static Timestamp getCurrentTimestamp () {
		 java.util.Date date= new java.util.Date();
		 return new Timestamp(date.getTime());
	}

	/*
	 * Create a timestamp from a string passed in
	 */
	public static Timestamp getTimestampFromString(String inString){
		Timestamp timestamp = null;
		try{
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		    Date parsedDate = dateFormat.parse(inString);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		}catch(Exception e){//this generic but you csan control another types of exception
		 //TODO Exception handling
			e.printStackTrace();
		}
		return timestamp;
	}
}
