package com.svnitsai.gm.util.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	/*
	 * Get Current Date
	 */
	public static Date getCurrentDate() {
		   return new Date();
	}
	/* 
	 * Get Current Date in yyyy/MM/dd
	 */
	public static String getCurrentDate(SimpleDateFormat dateFormatter) {
	    Date now = new Date();
	    try {
		    String strDate = dateFormatter.format(now);
		    return strDate;
	    }
	    catch (Exception e) {
			 //TODO Exception handling
				e.printStackTrace();
	    }
		return "";
		
	}
	/*
	 * Get Date x weeks before...
	 */
	public static Timestamp getTSWeekBefore(int weeksBefore) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBefore);
		java.util.Date result = calendar.getTime();
		return new Timestamp(result.getTime());
	}
	/*
	 * Get Date x weeks before...
	 */
	public static String getDateWeeksBefore(int weeksBefore) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -weeksBefore);
		java.util.Date result = calendar.getTime();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    return dateFormat.format(result);
	}
	/*
	 * Get Date x weeks before...
	 */
	public static String getTodayDate() {
		Date now = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    return dateFormat.format(now);
	}
}
