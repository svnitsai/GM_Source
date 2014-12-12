package com.svnitsai.gm.util.display;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.svnitsai.gm.util.date.DateUtil;

/*
 * DisplayUtil.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Display Related routines;
 * 
 */

public class DisplayUtil {
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
	private static SimpleDateFormat dateFormatterDB = new SimpleDateFormat("yyyyMMdd");

	/*
	 * Get displayable date from Date
	 */
	public static String getDisplayDate (Date date) {
		if(date == null) return "";
		else return dateFormatter.format(date);
	}

	/*
	 * Get displayable date from String with default dateFormatter yyyyMMdd
	 */
	public static String getDisplayDate (String stringDate) {
		return getDisplayDate(stringDate, dateFormatterDB);
	}

	/*
	 * Get displayable date from String
	 */
	public static String getDisplayDate (String stringDate, SimpleDateFormat dateFormatter) {
		if(stringDate == null) return "";
		try {
			Date date = dateFormatter.parse(stringDate);
			return getDisplayDate(date);
		}
		catch (ParseException e){
			System.out.println(e.getMessage());
			return "";
		}
		
	}

	/*
	 * Get displayable amount as 1,20,000 (no decimals)
	 */
	public static String getDisplayAmount (String stringDouble) {
		double doubleAmount = 0;
		try
		{
			doubleAmount = Double.parseDouble(stringDouble);
		}
		catch(Exception e){}
		return getDisplayAmount(doubleAmount);
	}

	/*
	 * Formatt Double in 1,23,45,678 format
	 */
	public static String getDisplayAmount(double value) {
	    if(value < 1000) {
	        return format("##0", value);
	    } else {
	        double hundreds = value % 1000;
	        int other = (int) (value / 1000);
	        return format(",##", other) + ',' + format("000", hundreds);
	    }
	}

	private static String format(String pattern, Object value) {
	    return new DecimalFormat(pattern).format(value);
	}
	
	/*
	 * Get Camel case string for display 
	 */
	public static String getDisplayCamelCase(String inString) {
        String[] parts = inString.split(" ");
        String camelCaseString = "";
        for (String part : parts){
            if(part!=null && part.trim().length()>0)
           camelCaseString = camelCaseString + toProperCase(part);
            else
                camelCaseString=camelCaseString+part+" ";   
        }
        return camelCaseString;
	}
	
	private static String toProperCase(String inString) {
        String temp=inString.trim();
        String spaces="";
        if(temp.length()!=inString.length())
        {
        int startCharIndex=inString.charAt(temp.indexOf(0));
        spaces=inString.substring(0,startCharIndex);
        }
        temp=temp.substring(0, 1).toUpperCase() +
        spaces+temp.substring(1).toLowerCase()+" ";
        return temp;

    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(" Date now is : " + 		getDisplayDate(DateUtil.getCurrentDate()));

		System.out.println(" Date now is : " + 		getDisplayDate("20141204"));
		System.out.println(" Date now is : " + 		getDisplayDate("2014/12/04",new SimpleDateFormat("yyyy/MM/dd") ));
		
		
		
		System.out.println(DecimalFormat.getCurrencyInstance(Locale.GERMANY).format( 123.45)); 
		
		//DecimalFormat dF = new DecimalFormat("\u20B9 000");
		DecimalFormat dF = new DecimalFormat("\u00A4 000");
		System.out.println ("INR  " + dF.format(123456789.123));
		
		 float amount = 100000;
		 NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		 String moneyString = formatter.format(amount);
		 System.out.println(moneyString);
		 
		 System.out.println("IN locate " + DecimalFormat.getCurrencyInstance(new Locale("en","IN")).format(123456.78));
		 
		 String pattern = "##,##,##,##0";
		 DecimalFormat decimalFormat = new DecimalFormat(pattern);
		 String format = decimalFormat.format(123456789.123);
		 System.out.println(format);
		 
		 System.out.println("function call : " + getDisplayAmount(123456789.00));
		 System.out.println("function call : " + getDisplayAmount(89.00));
		 System.out.println("function call : " + getDisplayAmount(0.00));
		 System.out.println("function call : " + getDisplayAmount("123456.78"));
		 System.out.println("function call : " + getDisplayAmount("123,456.78"));
		 
	}

}
