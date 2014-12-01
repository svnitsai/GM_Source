package com.svnitsai.gm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util
{
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	
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
}