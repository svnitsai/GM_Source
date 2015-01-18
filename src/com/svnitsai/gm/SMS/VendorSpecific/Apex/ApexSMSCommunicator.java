package com.svnitsai.gm.SMS.VendorSpecific.Apex;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.svnitsai.gm.SMS.DataPost;
import com.svnitsai.gm.SMS.SMSSendRequest;
import com.svnitsai.gm.SMS.SMSSendResponse;
import com.svnitsai.gm.SMS.SMS_Error;
import com.svnitsai.gm.SMS.URLParamEncoder;
import com.svnitsai.gm.SMS.VendorSpecific.SMSCommunicator;
import com.svnitsai.gm.constants.SMSUIMessage;
import com.svnitsai.gm.util.log.LogUtil;

/* 
* ApexSMSCommunicator.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Apex specific communicator.
* 
*/

public class ApexSMSCommunicator extends SMSCommunicator{
	
	public ApexSMSCommunicator(String name){
		super.vendorName = name;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.svnitsai.gm.SMS.VendorSpecific.SMSCommunicator#submitSendSMS(com.svnitsai.gm.SMS.SMSSendRequest, com.svnitsai.gm.SMS.SMSSendResponse)
	 * 	Sending single SMS at a time
	 */
	public int submitSendSMS(SMSSendRequest sendRequest, SMSSendResponse sendResponse) {
		// Load Apex config from properties file
		int returnCode = loadConfig(sendResponse);
		if (returnCode != 0) return returnCode;
		
    	//Create send request Parm
		StringBuffer sbRequestParm = new StringBuffer(); 
		returnCode = createSendSMSRequest(sendRequest, sbRequestParm);
		if (returnCode != 0) return returnCode;
    	
    	//Post request to Apex
		StringBuffer sbResponseXML = new StringBuffer();
		String smsGrouping = sendRequest.getSMSMessageGrouping();
		DataPost dataPost = new DataPost();
        returnCode = dataPost.inlineHttpClientPost(ApexConfig.getSMS_Send_URL(), sbRequestParm.toString()
        		, sbResponseXML, smsGrouping);
        if (returnCode != 0) { 
    		sendResponse.setSmsUIMessage(SMSUIMessage.TRYLATER_INTERNET_ISSUE); //UI on SEND
    		sendResponse.setFailureReason("SMS submission to Vendor failed"); //Data base & UI History
			sendResponse.setReturnCode(returnCode);
        	return returnCode;
        }
    	
        //TODO: For testing only
        //Overwrite SMS response file - ApexSendFailureResponse.xml; ApexSendSuccessResponse.xml
		//String filename = "ApexSendFailureResponse.xml";
//		String filename = "ApexSendSuccessResponse.xml";
//		String workingDirectory = System.getProperty("user.dir") + "\\src\\com\\svnitsai\\gm\\SMS\\VendorSpecific\\Apex";
//		workingDirectory = "C:\\apache-tomcat-8.0.15\\apache-tomcat-8.0.15\\webapps\\gm\\web\\Files";
//		String absoluteFilePath = "";
//		absoluteFilePath = workingDirectory + File.separator + filename;
//		System.out.println (" file path " + absoluteFilePath);
//        String fileContent = readFile(absoluteFilePath);
//        System.out.println ("File content is : " + fileContent);
//        sbResponseXML.delete(0, sbResponseXML.length() + 1);
//        sbResponseXML.append(fileContent);
        
        //TODO: For testing only code - ends
        
    	//Process send response
        returnCode = processSendSMSResponse(sbResponseXML, sendResponse);
        if (returnCode != 0)  {
        	if (sendResponse.getSmsUIMessage() != SMSUIMessage.USER_ACTIONABLE_ID_ISSUE)
        		sendResponse.setSmsUIMessage(SMSUIMessage.SMS_VENDOR_REJECTED); //UI on SEND

        	return returnCode;
        }
    	
		return 0;
	}
	
	@Override
	public int submitSendSMS(List <SMSSendRequest> sendRequestList,
			SMSSendResponse sendResponse) {
		// Load Apex config from properties file
		int returnCode = loadConfig();
		if (returnCode != 0) return returnCode;
		
    	//Create send request XML
		StringBuffer sbRequestXML = new StringBuffer(); 
		returnCode = createSendSMSRequest(sendRequestList, sbRequestXML);
		if (returnCode != 0) return returnCode;
    	
    	//Post XML to Apex
		StringBuffer sbResponseXML = new StringBuffer();
		DataPost dataPost = new DataPost();
        returnCode = dataPost.XMLDataPost(ApexConfig.getSMS_Send_URL(), sbRequestXML.toString(), sbResponseXML);
        if (returnCode != 0) { 
//        	System.out.println(" XML Data post failed with return code: " + returnCode);
        	return returnCode;
        }
    	
        //TODO: For testing only
        //Overwrite SMS response file - ApexSendFailureResponse.xml; ApexSendSuccessResponse.xml
		//String filename = "ApexSendFailureResponse.xml";
		String filename = "ApexSendSuccessResponse.xml";
		String workingDirectory = System.getProperty("user.dir") + "\\src\\com\\svnitsai\\gm\\SMS\\VendorSpecific\\Apex";
		String absoluteFilePath = "";
		absoluteFilePath = workingDirectory + File.separator + filename;
		System.out.println (" file path " + absoluteFilePath);
        String fileContent = readFile(absoluteFilePath);
        System.out.println ("File content is : " + fileContent);
        sbResponseXML.delete(0, sbResponseXML.length() + 1);
        sbResponseXML.append(fileContent);
        
        //TODO: For testing only code - ends
        
    	//Process send response
        returnCode = processSendSMSResponse(sbResponseXML, sendResponse);
        if (returnCode != 0) return returnCode;
    	
		return 0;
	}

	//TODO: delete me
	private String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	/*
	 * Load Apex configuration
	 */
	private int loadConfig() {
    	//Load Apex config properties
    	int returnCode = ApexConfig.getProperties();
    	if (returnCode != 0) {
    		LogUtil.log(LogUtil.Message_Type.Error, "Error loading Apex SMS config; Program Aborting...");
    		return returnCode;
    	}
		return 0;
	}

	/*
	 * Load Apex configuration
	 */
	private int loadConfig(SMSSendResponse sendResponse) {
    	//Load Apex config properties
    	int returnCode = ApexConfig.getProperties();
    	if (returnCode != 0) { //Serious file read error; can not continue
    		LogUtil.log(LogUtil.Message_Type.Error, "Error loading Apex SMS config; Program Aborting...");
    		sendResponse.setSmsUIMessage(SMSUIMessage.SYSTEM_ERROR); //UI on SEND
    		sendResponse.setFailureReason("System Error in sending SMS"); //Data base & UI History
			sendResponse.setReturnCode(returnCode);
    		return returnCode;
    	}
		return 0;
	}
	
	/*
	 * Send One SMS message at a time
	 */
	private int createSendSMSRequest(SMSSendRequest sendRequest, StringBuffer sbRequestParm) {
		
		sbRequestParm.append("?user=").append(ApexConfig.getUser());
		sbRequestParm.append("&password=").append(ApexConfig.getPassword());
		sbRequestParm.append("&mobiles=").append(sendRequest.getMobileNumber());
		sbRequestParm.append("&sms=").append(URLParamEncoder.encode(sendRequest.getSMSMessage())); // encode message spaces & other chars
		
		//optional
		String senderIDVal = ApexConfig.getSenderId();
		if (senderIDVal==null) senderIDVal = sendRequest.getSenderID(); //Get SenderId from request if not in properties file
		if ((senderIDVal!=null) && !senderIDVal.isEmpty()) {
			sbRequestParm.append("&senderid=").append(URLParamEncoder.encode(senderIDVal));
		}
		
		return 0;
	}
	
	/*
	  Sample SEND SMS Request
			String stringRequestXML = "<?xml version=\"1.0\"?>"
				+ "<smslist>"
					+ "<sms>"
						+ "<user>" + ApexConfig.getUser() + "</user>"
						+ "<password>" + ApexConfig.getPassword() + "</password>"
						+ "<message>message content</message>"
						+ "<mobiles>919XXXXXXXXX</mobiles>"
						+ "<clientsmsid>1</clientsmsid>"
						//optional parms
						+ "<groupname>SPTEX</groupname>"
						+ "<senderid>SAMMY</senderid>"
						+ "<scheduletime>2015-01-01 00:00:00</scheduletime>"
						//+ "			<cdmasenderid>9199999999999</cdmasenderid> "
						//+ "<accountusagetypeid>1</accountusagetypeid>" //0-Service, 1-promotional
						+ "<unicode>1</unicode>"
					+ "</sms>"
				+ "</smslist>";
	*/
	//Not used
	private int createSendSMSRequest(List <SMSSendRequest> sendRequestList, StringBuffer sbRequestXML) {
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("smslist");
			doc.appendChild(rootElement);
	 
			for (SMSSendRequest sendRequest : sendRequestList) {
				// staff element
				Element sms = doc.createElement("sms");
				rootElement.appendChild(sms);
		 
				// user element
				Element user = doc.createElement("user");
				user.appendChild(doc.createTextNode(ApexConfig.getUser()));
				sms.appendChild(user);

				// password element
				Element password = doc.createElement("password");
				password.appendChild(doc.createTextNode(ApexConfig.getPassword()));
				sms.appendChild(password);

				// message element
				Element message = doc.createElement("message");
				message.appendChild(doc.createTextNode(sendRequest.getSMSMessage()));
				sms.appendChild(message);
				
				// mobile element
				Element mobiles = doc.createElement("mobiles");
				mobiles.appendChild(doc.createTextNode(sendRequest.getMobileNumber()));
				sms.appendChild(mobiles);
				
				//Optional elements start
				// clientsmsid element
				String clientsmsidVal = sendRequest.getClientSMSId();
				if ((clientsmsidVal != null) && (!clientsmsidVal.equalsIgnoreCase(""))) {
					Element clientsmsid = doc.createElement("clientsmsid");
					clientsmsid.appendChild(doc.createTextNode(clientsmsidVal));
					sms.appendChild(clientsmsid);
				}
				
				String SMSBulkMessageGrouping = sendRequest.getSMSMessageGrouping().toString();
				if ((SMSBulkMessageGrouping != null) && (!SMSBulkMessageGrouping.equalsIgnoreCase(""))) {
					Element groupname = doc.createElement("groupname");
					groupname.appendChild(doc.createTextNode(SMSBulkMessageGrouping));
					sms.appendChild(groupname);
				}
				
				String senderIDVal = ApexConfig.getSenderId(); //Get from properties file
				if (senderIDVal == null) {//if null, use the one sent in request
					senderIDVal = sendRequest.getSenderID();
				}
				if ((senderIDVal != null) && (!senderIDVal.equalsIgnoreCase(""))) {
					Element senderID = doc.createElement("senderid");
					senderID.appendChild(doc.createTextNode(senderIDVal));
					sms.appendChild(senderID);
				}
				
				//TODO: format time to 2015:01:01 00:00:00
				if (sendRequest.getSMSScheduleTime() != null) {
					String scheduleTimeVal = sendRequest.getSMSScheduleTime().toString();
					if ((scheduleTimeVal != null) && (!scheduleTimeVal.equalsIgnoreCase(""))) {
						Element scheduleTime = doc.createElement("scheduletime");
						scheduleTime.appendChild(doc.createTextNode(scheduleTimeVal));
						sms.appendChild(scheduleTime);
					}
				}
				
				String accountUsageTypeIDVal = ApexConfig.getAccountUsageTypeId(); //Get from properties file
				if (accountUsageTypeIDVal == null) {//if null, use the one sent in request
					accountUsageTypeIDVal = sendRequest.getAccountUsageTypeID();
				}
				if ((accountUsageTypeIDVal != null) && (!accountUsageTypeIDVal.equalsIgnoreCase(""))) {
					Element accountUsageTypeID = doc.createElement("accountUsageTypeID");
					accountUsageTypeID.appendChild(doc.createTextNode(accountUsageTypeIDVal));
					sms.appendChild(accountUsageTypeID);
				}
				
				boolean unicodeVal = false;
				String unicodeStringVal = ApexConfig.getUnicode();
				if (unicodeStringVal == null) unicodeVal = sendRequest.isUniCode();
				if (unicodeStringVal.equalsIgnoreCase("1")) unicodeVal = true;
				if (unicodeStringVal.equalsIgnoreCase("0")) unicodeVal = false;
				Element unicode = doc.createElement("unicode");
				if (unicodeVal) unicode.appendChild(doc.createTextNode("1"));
				else unicode.appendChild(doc.createTextNode("0"));
				sms.appendChild(unicode);
			}
			
			// XML creation complete			
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
	 
			transformer.transform(source, result);
			//System.out.println("XML IN String format is: \n" + writer.toString());
			sbRequestXML.append(writer.toString());
	 
		  } catch (ParserConfigurationException pce) {
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.SMS_XML_REQUEST_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_REQUEST_PARSE_ERROR.getMessage() 
						+ "; Parser configuration exception; Request is: " +  sbRequestXML.toString());
				pce.printStackTrace();
				return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		  } catch (TransformerException tfe) {
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.SMS_XML_REQUEST_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_REQUEST_PARSE_ERROR.getMessage() 
						+ "; Transformer exception; Request is: " +  sbRequestXML.toString());
				return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		  }
		return 0;
	}

    /* Sample response xml :You will receive below XML response if Message is Successfully Sent. 
		<?xml version="1.0" encoding="iso-8859-1" ?> 
			<sms> 
				<messageid>1480873</messageid> 
			</sms> 
	
		and if SMS not sent successfully you will receive the below response. 
	
		<sms> 
			<messageid>-1</messageid> 
		</sms>
		
		 SMS account issues: Handled by ApexXMLErrorResponseHandler
	 	  Response : 
		<?xml version="1.0" encoding="UTF-8"?><smslist>
		    <error>
		        <smsclientid>0</smsclientid>
		        <error-code>-10021</error-code>
		        <error-description>UserNameRequire</error-description>
		        <error-action>1</error-action>
		    </error>
		</smslist>
		 
     */
	private int processSendSMSResponse(StringBuffer sbResponseXML, SMSSendResponse sendResponse) {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		XMLReader xmlReader = null;
		try {
			saxParser = factory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
		} catch (ParserConfigurationException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getMessage() 
					+ "; Parser configuration exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId());
			return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		} catch (SAXException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getMessage() 
					+ "; SAX exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId());
			return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		}

		//Handle error return XML parsing only if response contains word error
		if (sbResponseXML.toString().indexOf("error", 0) > -1) {
			int returnCode = handleErrorReturnXML(xmlReader, sbResponseXML, sendResponse);
			if (returnCode > -1) return returnCode;
		}
		
		//Handle other return XMLs
		int returnCode = handleSendResponseXML(xmlReader, sbResponseXML, sendResponse);
		if (returnCode > -1) return returnCode;
		
		return 0;
	}
	
	private int handleErrorReturnXML(XMLReader xmlReader, StringBuffer sbResponseXML, SMSSendResponse sendResponse) {

		ApexXMLSendErrorResponseHandler aXMLERHandler = new ApexXMLSendErrorResponseHandler(xmlReader);
		xmlReader.setContentHandler(aXMLERHandler);
		try {
			 StringReader sr = new StringReader(sbResponseXML.toString());
             InputSource is = new InputSource(sr);
	         is.setEncoding("ISO-8859-1");
			xmlReader.parse(is);
		} catch (IOException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() 
					+ "; IO exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.HTTP_IO_EXCEPTION.getId());
			return SMS_Error.HTTP_IO_EXCEPTION.getId();
		} catch (SAXException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getMessage() 
					+ "; SAX exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId());
			return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		}
		
		if (aXMLERHandler.isDataFound()) {
			sendResponse.setFailureReason(aXMLERHandler.getErrorInfo());
			sendResponse.setSuccessReferenceId(null);
			sendResponse.setReturnCode(SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId());
			if (sendResponse.getFailureReason().indexOf("-10002", 0) > -1) { //Invalid id /password
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.SMS_INVALID_USERID_PASSWORD.getId() + ": " + SMS_Error.SMS_INVALID_USERID_PASSWORD.getMessage() 
						+ "; SMS failed reason: " +  sendResponse.getFailureReason());
	    		sendResponse.setSmsUIMessage(SMSUIMessage.USER_ACTIONABLE_ID_ISSUE); //UI on SEND
				return SMS_Error.SMS_INVALID_USERID_PASSWORD.getId();
			} else {
				LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId() + ": " + SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getMessage() 
					+ "; SMS failed reason: " +  sendResponse.getFailureReason());
				return SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId();
			}
		}
		
		return -1; //No data found - response is not a ERROR XML
	}

	private int handleSendResponseXML(XMLReader xmlReader, StringBuffer sbResponseXML, SMSSendResponse sendResponse) {

		ApexXMLSendResponseHandler aXMLSendRespHandler = new ApexXMLSendResponseHandler(xmlReader);
		xmlReader.setContentHandler(aXMLSendRespHandler);
		try {
			 StringReader sr = new StringReader(sbResponseXML.toString());
             InputSource is = new InputSource(sr);
	         is.setEncoding("ISO-8859-1");
			xmlReader.parse(is);
		} catch (IOException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() 
					+ "; IO exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.HTTP_IO_EXCEPTION.getId());
			return SMS_Error.HTTP_IO_EXCEPTION.getId();
		} catch (SAXException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId() + ": " + SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getMessage() 
					+ "; SAX exception; Response is: " +  sbResponseXML.toString());
			e.printStackTrace();
    		sendResponse.setFailureReason("SMS Vendor returned bad data"); //Data base & UI History
			sendResponse.setReturnCode(SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId());
			return SMS_Error.SMS_XML_RESPONSE_PARSE_ERROR.getId();
		}
		
		if (aXMLSendRespHandler.isDataFound()) {
			if (aXMLSendRespHandler.getMessageID().equalsIgnoreCase("-1")) { //Error in processing
				sendResponse.setFailureReason(aXMLSendRespHandler.getErrorInfo());
				sendResponse.setSuccessReferenceId(null);
				sendResponse.setReturnCode(SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId());
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId() + ": " + SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getMessage() 
						+ "; SMS failed reason: " +  sendResponse.getFailureReason());
				return SMS_Error.HTTP_SEND_SMS_REQUEST_FAILED.getId();
			} else {
				sendResponse.setFailureReason(aXMLSendRespHandler.getErrorInfo());
				sendResponse.setSuccessReferenceId(aXMLSendRespHandler.getMessageID());
				sendResponse.setReturnCode(0);
				return 0;
			}
		}
		
		return -1; //No data found - response is not an expected response XML
	}

}
