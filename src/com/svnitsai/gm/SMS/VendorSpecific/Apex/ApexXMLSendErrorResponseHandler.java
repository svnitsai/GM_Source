package com.svnitsai.gm.SMS.VendorSpecific.Apex;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.svnitsai.gm.util.log.LogUtil;

/* 
* ApexXMLErrorResponseHandler.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Using SAX parser handle error XML response for SEND option
* 
* 				Handles following response
* 
*				Response : 
*				<?xml version="1.0" encoding="UTF-8"?><smslist>
*				    <error>
*				        <smsclientid>0</smsclientid>
*				        <error-code>-10021</error-code>
*				        <error-description>UserNameRequire</error-description>
*				        <error-action>1</error-action>
*				    </error>
*				</smslist>
*
*
*
*				<?xml version="1.0" encoding="UTF-8"?><smslist>
*				    <error>
*				        <smsclientid>0</smsclientid>
*				        <error-code>-10021</error-code>
*				        <error-description>UserNameRequire</error-description>
*						<mobile-no>9999</mobile-no>
*				        <error-action>1</error-action>
*				    </error>
*				</smslist>
* 
*/

public class ApexXMLSendErrorResponseHandler implements ContentHandler  {
	
	private XMLReader xmlReader;
	private String xmlStartElement = "";
	private String smsClientID = "";
	private String errorInfo = "";
	private String errorAction = "";
		
	public XMLReader getXmlReader() {
		return xmlReader;
	}

	public void setXmlReader(XMLReader xmlReader) {
		this.xmlReader = xmlReader;
	}

	public String getXmlStartElement() {
		return xmlStartElement;
	}

	public void setXmlStartElement(String xmlStartElement) {
		this.xmlStartElement = xmlStartElement;
	}

	public String getSmsClientID() {
		return smsClientID;
	}

	public void setSmsClientID(String smsClientID) {
		this.smsClientID = smsClientID;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getErrorAction() {
		return errorAction;
	}

	public void setErrorAction(String errorAction) {
		this.errorAction = errorAction;
	}

	public ApexXMLSendErrorResponseHandler(XMLReader xMLReader) {
		this.setXmlReader(xMLReader);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		switch (this.xmlStartElement) {
		case "smslist": {
			break;
		}
		case "error": {
			break;
		}
		case "smsclientid": {
			this.setSmsClientID(new String(ch, start, length));
			break;
		}
		case "error-code": {
			this.setErrorInfo(new String(ch, start, length));
			break;
		}
		case "mobile-no": {//do nothing
			break;
		}
		case "error-description": {
			this.setErrorInfo(getErrorInfo() + " " + new String(ch, start, length));
			break;
		}
		case "error-action": {
			this.setErrorAction(new String(ch, start, length));
			break;
		}
		case "": {
			
			break;
		}
		default: {
			LogUtil.log(LogUtil.Message_Type.Error, "Unhandled start element " + this.xmlStartElement + " in ApexXMLErrorResponseHandler! ");
			//TODO: handle error
		}
	}

	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//System.out.println("End Element: " + qName);
		this.setXmlStartElement("");
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.setXmlStartElement(qName);
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	//Check if parser able to find error data
	public boolean isDataFound() {
		if ((this.getErrorAction().isEmpty()) && (this.getErrorInfo().isEmpty()) && (this.getSmsClientID().isEmpty())) 
			return false;
		else return true;
	}

}
