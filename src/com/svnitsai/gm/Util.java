package com.svnitsai.gm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util
{
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	private static SimpleDateFormat dateFormatterDB = new SimpleDateFormat("yyyyMMdd");
	
	public static int convertToInt(String str)
	{
		int i = 0;
		try
		{
			i = Integer.parseInt(str);
		}
		catch(Exception e){}
		return i;
	}
	
	
	public static long convertToLong(String str)
	{
		long i = 0;
		try
		{
			i = Long.parseLong(str);
		}
		catch(Exception e){}
		return i;
	}
	
	public static double convertToDouble(String str)
	{
		double i = 0;
		try
		{
			i = Double.parseDouble(str);
		}
		catch(Exception e){}
		return i;
	}
	
	public static String getFormattedDate(Date d)
	{
		if(d == null)
		{
			return "";
		}
		else
		{
			return dateFormatter.format(d);
		}
	}
	
	public static String getFormattedDateForDB(Date d)
	{
		return (d != null)
				? dateFormatterDB.format(d)
				: "";
	}
	
	public static String getFormattedDateForDB(String dateStr)
	{
		if(dateStr == null)
		{
			return "";
		}
		else
		{
			String[] dateParts = dateStr.split("/");
			System.out.println("Length: " + dateParts.length);
			if(dateParts.length == 3)
			{
				// Format it to be yyyymmdd
				String formattedDate = dateParts[2] + dateParts[1] + dateParts[0];
				return formattedDate;
			}
			else
			{
				return "";
			}
			
		}
	}
}