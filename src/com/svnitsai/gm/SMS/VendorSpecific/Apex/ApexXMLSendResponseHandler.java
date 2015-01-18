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
*				<?xml version="1.0" encoding="iso-8859-1" ?> 
*				<smslist>
*					<sms>
*						<smsclientid>1508095729</smsclientid>
*						<messageid>110298723</messageid>
*						<mobile-no>+919441123349</mobile-no>
*					</sms>
*				</smslist>
* 
* 				Failed one:
*				<?xml version="1.0" encoding="iso-8859-1" ?> 
*				<sms> 
*					<messageid>-1</messageid> 
*				</sms> 
* 
*/

public class ApexXMLSendResponseHandler implements ContentHandler  {
	
	private XMLReader xmlReader;
	private String xmlStartElement = "";
	private String messageID = "";
	private String smsClientID = "";
	private String mobileNo="";
	private String errorInfo = "";
		
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

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getSmsClientID() {
		return smsClientID;
	}

	public void setSmsClientID(String smsClientID) {
		this.smsClientID = smsClientID;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public ApexXMLSendResponseHandler(XMLReader xMLReader) {
		this.setXmlReader(xMLReader);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		switch (this.xmlStartElement) {
			case "smslist": {
				break;
			}
			case "sms": {
				break;
			}
			case "messageid": {
				this.setMessageID(new String(ch, start, length));
				if (this.getMessageID().equalsIgnoreCase("-1")) 
					this.setErrorInfo("SMS not sent successfully!");
				break;
			}
			case "smsclientid": {
				this.setSmsClientID(new String(ch, start, length));
				break;
			}
			case "mobile-no": {
				this.setMobileNo(new String(ch, start, length));
				break;
			}
			case "": {
				
				break;
			}
			default: {
				LogUtil.log(LogUtil.Message_Type.Error, "Unhandled start element " + this.xmlStartElement + " in ApexXMLSendResponseHandler! ");
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
		if ((this.getMessageID().isEmpty()) && (this.getErrorInfo().isEmpty())) 
			return false;
		else return true;
	}

}
