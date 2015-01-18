package com.svnitsai.gm.SMS.VendorSpecific.Apex;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.svnitsai.gm.SMS.SMS_Error;
import com.svnitsai.gm.util.log.LogUtil;

/* 
* ApexConfig.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Loads SMS properties related to vendor APEX
* 
*/

public class ApexConfig {
	
	private static String fileName = "SMS_Apex.properties";
	private static String user;
	private static String password;
	private static String SMS_Send_URL;
	private static String SMS_Get_Delivery_Report_URL;
	private static String SMS_Send_Delivery_Acknowledgement_URL;
	private static String SMS_Get_Undelivered_Details_URL;
	//optional fields
	private static String SenderId = null;
	private static String AccountUsageTypeId = null;
	private static String Unicode = null;
	
	private static boolean configLoaded = false; //Is properties file loaded?
	
	public static String getUser() {
		return user;
	}

	private static void setUser(String user) {
		ApexConfig.user = user;
	}

	public static String getPassword() {
		return password;
	}

	private static void setPassword(String password) {
		ApexConfig.password = password;
	}

	public static String getSMS_Send_URL() {
		return SMS_Send_URL;
	}

	private static void setSMS_Send_URL(String sMS_Send_URL) {
		SMS_Send_URL = sMS_Send_URL;
	}

	public static String getSMS_Get_Delivery_Report_URL() {
		return SMS_Get_Delivery_Report_URL;
	}

	private static void setSMS_Get_Delivery_Report_URL(String sMS_Get_Delivery_Report_URL) {
		SMS_Get_Delivery_Report_URL = sMS_Get_Delivery_Report_URL;
	}

	public static String getSMS_Send_Delivery_Acknowledgement_URL() {
		return SMS_Send_Delivery_Acknowledgement_URL;
	}

	private static void setSMS_Send_Delivery_Acknowledgement_URL(
			String sMS_Send_Delivery_Acknowledgement_URL) {
		SMS_Send_Delivery_Acknowledgement_URL = sMS_Send_Delivery_Acknowledgement_URL;
	}

	public static String getSMS_Get_Undelivered_Details_URL() {
		return SMS_Get_Undelivered_Details_URL;
	}

	private static void setSMS_Get_Undelivered_Details_URL(
			String sMS_Get_Undelivered_Details_URL) {
		SMS_Get_Undelivered_Details_URL = sMS_Get_Undelivered_Details_URL;
	}

	public static String getSenderId() {
		return SenderId;
	}

	public static void setSenderId(String senderId) {
		SenderId = senderId;
	}

	public static String getAccountUsageTypeId() {
		return AccountUsageTypeId;
	}

	public static void setAccountUsageTypeId(String accountUsageTypeId) {
		AccountUsageTypeId = accountUsageTypeId;
	}

	public static String getUnicode() {
		return Unicode;
	}

	public static void setUnicode(String unicode) {
		Unicode = unicode;
	}

	public static boolean isConfigLoaded() {
		return configLoaded;
	}

	private static void setConfigLoaded(boolean configLoaded) {
		ApexConfig.configLoaded = configLoaded;
	}
	
/*-----------------------------------------------------------------------------------------------*/	

	public static int getProperties() {
		int returnCode = 0;
		if (!configLoaded) {
			//load properties from file
	    	Properties prop = new Properties();
	    	InputStream input = null;
	 
	    	try {
	    		input = ApexConfig.class.getClassLoader().getResourceAsStream(fileName);
	    		if(input==null){
	    	        LogUtil.log(LogUtil.Message_Type.Error, "Unable to find properties file, " +  fileName + ", in classpath.");
	    		    return SMS_Error.PROPERTY_FILE_NOT_FOUND.getId();
	    		}
	 
	    		//load a properties file from class path
	    		prop.load(input);
	    		
	    		//set property values
	    		setProperties(prop);
	    		
	    		//ensure all required properties are set
	    		returnCode = validateProperties();
	    		
	    		//set property loaded to true, if validation is successful
	    		if (returnCode == 0) setConfigLoaded(true);
	    		
	    	} catch (IOException ex) {
    	        LogUtil.log(LogUtil.Message_Type.Error, "IOException reading properties file, " +  fileName + ", in classpath.");
	    		ex.printStackTrace();
    		    return SMS_Error.FILE_OPEN_EXCEPTION.getId();
	        } finally{
	        	if(input!=null){
	        		try { input.close();
	        		} catch (IOException e) {
	        	        LogUtil.log(LogUtil.Message_Type.Error, "IOException closing properties file, " +  fileName + ", in classpath.");
	        			e.printStackTrace();
	        		    return SMS_Error.FILE_OPEN_EXCEPTION.getId();
	        		}
	        	}
	        }
		}
		return returnCode;
	}
	
	private static void setProperties(Properties prop){
        //get property value and set
		setUser(prop.getProperty("user"));
		setPassword(prop.getProperty("password"));
		setSMS_Send_URL(prop.getProperty("SMS_Send_URL"));
		setSMS_Get_Delivery_Report_URL(prop.getProperty("SMS_Get_Delivery_Report_URL"));
		setSMS_Send_Delivery_Acknowledgement_URL(prop.getProperty("SMS_Send_Delivery_Acknowledgement_URL"));
		setSMS_Get_Undelivered_Details_URL(prop.getProperty("SMS_Get_Undelivered_Details_URL"));
		//optional
		setSenderId(prop.getProperty("SenderId"));
		setAccountUsageTypeId(prop.getProperty("AccountUsageTypeId"));
		setUnicode(prop.getProperty("Unicode"));
	}
	
	private static int validateProperties() {
		LogUtil.log(LogUtil.Message_Type.Information, "Properties being loaded from " + fileName);
		if (user == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - user - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		} else LogUtil.log(LogUtil.Message_Type.Information, " user: " + user);
		
		if (password == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - password - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		} else LogUtil.log(LogUtil.Message_Type.Information, " password: " + password);

		if (SMS_Send_URL == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - SMS_Send_URL - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		} else LogUtil.log(LogUtil.Message_Type.Information, " SMS_Send_URL: " + SMS_Send_URL);
				
		if (SMS_Get_Delivery_Report_URL == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - SMS_Get_Delivery_Report_URL - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		}  else LogUtil.log(LogUtil.Message_Type.Information, " SMS_Get_Delivery_Report_URL: " + SMS_Get_Delivery_Report_URL);
		
		if (SMS_Send_Delivery_Acknowledgement_URL == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - SMS_Send_Delivery_Acknowledgement_URL - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		}  else LogUtil.log(LogUtil.Message_Type.Information, " SMS_Send_Delivery_Acknowledgement_URL: " + SMS_Send_Delivery_Acknowledgement_URL);
		
		if (SMS_Get_Undelivered_Details_URL == null) {
	        LogUtil.log(LogUtil.Message_Type.Error, "Property - SMS_Get_Undelivered_Details_URL - not found in properties file; " + fileName);
	        return SMS_Error.PROPERTY_NOT_FOUND.getId();
		}  else LogUtil.log(LogUtil.Message_Type.Information, " SMS_Get_Undelivered_Details_URL: " + SMS_Get_Undelivered_Details_URL);
		
		LogUtil.log(LogUtil.Message_Type.Information, " SenderId: " + SenderId);
		LogUtil.log(LogUtil.Message_Type.Information, " AccountUsageTypeId: " + AccountUsageTypeId);
		LogUtil.log(LogUtil.Message_Type.Information, " Unicode: " + Unicode);
		
		return 0;
	}
	
//	public static void main(String [] arg) {
//		getProperties();
//	}

}
