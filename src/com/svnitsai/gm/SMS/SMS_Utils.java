package com.svnitsai.gm.SMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/* 
* SMS_Utils.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Util functions used by SMS & related packages
* 
*/

public class SMS_Utils {

	/*
	 *  convert InputStream to String
	 */
	public static String getStringFromInputStream(InputStream is) {
 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/*
	 * Validate and Create a Pretty formatted XML for user readability
	 */
	public static String getFormattedXML(String xmlString) {
		String formattedXMLString = "";
		// Instantiate transformer input
		xmlString = stripExtraChars(xmlString);
		Source xmlInput = new StreamSource(new StringReader(xmlString));
		StreamResult xmlOutput = new StreamResult(new StringWriter());

		// Configure transformer
		Transformer transformer;
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 4);
			transformer = transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(xmlInput, xmlOutput);
			formattedXMLString = xmlOutput.getWriter().toString();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // An identity transformer
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formattedXMLString;

	}

	/*
	 * Strip extra chars between > and < like tab and spaces for pretty formatting
	 */
	private static String stripExtraChars(String inStr) {
		char tabChar = '\t';
		char newLine = '\n';
		String outString = "";
		boolean gtFound = false, ltFound = false;
		for (int i=0; i < inStr.length() ;  i++ ) {
			if (inStr.charAt(i) == '>') {
				gtFound = true;
				ltFound = false;
			}
			if (inStr.charAt(i) == '<') {
				ltFound = true;
				gtFound = false;
			}
			if (gtFound && !ltFound) {
				if ((inStr.charAt(i) == ' ') || ( inStr.charAt(i) == tabChar)
						|| ( inStr.charAt(i) == newLine)) continue;
				else outString = outString + inStr.charAt(i);
			} else
			{
				outString = outString + inStr.charAt(i);
			}
		}
		return outString;
	}

}
