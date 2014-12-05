package com.svnitsai.gm.util.log;

/*
 * LogUtil.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Write logs of certain message type;
 * 
 */

public class LogUtil {

	public static Message_Type log_Level = Message_Type.Information;
	
	public enum Message_Type { Information, Warning, Error	}

	/**
	 * @return the log_level
	 */
	public static Message_Type getLog_level() {
		return log_Level;
	}

	/**
	 * @param log_level the log_level to set
	 */
	public static void setLog_level(Message_Type log_level) {
		log_Level = log_level;
	}

	public static void log(Message_Type mesgType, String mesg) {
		switch (getLog_level()) {
		case Error: {
			if (mesgType == Message_Type.Error)	System.out.println (mesgType + " : " + mesg);
			break;
		}
		case Warning: {
			if ((mesgType == Message_Type.Error) || (mesgType == Message_Type.Warning))
				System.out.println (mesgType + " : " + mesg);
			break;
		}
		case Information: {
			System.out.println (mesgType + " : " + mesg);
			break;
		}
		}
	}
}