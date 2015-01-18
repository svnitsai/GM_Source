package com.svnitsai.gm.SMS.VendorSpecific;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.svnitsai.gm.SMS.DataPost;
import com.svnitsai.gm.SMS.VendorSpecific.Apex.ApexConfig;
import com.svnitsai.gm.util.log.LogUtil;
 
 
public class TestApex {
 
    static Properties props;
 
    public static void main(String[] args)
    {
    	//Load Apex config properties
    	int returnCode = ApexConfig.getProperties();
    	if (returnCode != 0) {
    		LogUtil.log(LogUtil.Message_Type.Error, "Error loading Apex SMS config; Program Aborting...");
    		return;
    	}
    	
    	//Send SMS
    	String actionType = "Send SMS";
    	System.out.println ("Request type to be sent: " + actionType);
		String stringURL="http://sms.apexinfo.co.in/sendsms.jsp?";
		//String stringURL="http://sms.jothimailservers.com/sendsms.jsp";
		String stringRequestXML = "<?xml version=\"1.0\"?>"
				+ "<smslist>"
					+ "<sms>"
//						+ "<user>ravitup</user>"
//						+ "<password>ru2xxx</password>"
						+ "<user>apexdemo</user>"
						+ "<password>demo1234</password>"
						+ "<message>TestMessage</message>"
						+ "<mobiles>94xxxxxx69</mobiles>"
//						+ "<clientsmsid>1</clientsmsid>"
//						//optional parms
//						+ "<groupname>SPTEX</groupname>"
//						+ "<senderid>SAMMY</senderid>"
//						+ "<scheduletime>2015-01-01 00:00:00</scheduletime>"
//						//+ "			<cdmasenderid>9199999999999</cdmasenderid> "
//						//+ "<accountusagetypeid>1</accountusagetypeid>" //0-Service, 1-promotional
//						+ "<unicode>1</unicode>"
					+ "</sms>"
				+ "</smslist>";
        //boolean success = XMLDataPost(stringURL, stringRequestXML, "");
        //boolean success = XMLDataPost(ApexConfig.getSMS_Send_URL(), stringRequestXML, "");
		StringBuffer sbResponseXML = new StringBuffer();
		boolean success = false;
		DataPost dataPost = new DataPost();
        //returnCode = dataPost.XMLDataPost(ApexConfig.getSMS_Send_URL(), stringRequestXML, sbResponseXML);
        returnCode = dataPost.XMLDataPost(stringURL, stringRequestXML, sbResponseXML);
        if (returnCode != 0) System.out.println(" XML Data post failed with return code: " + returnCode);
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" ");
        /* Sample response xml
           You will receive below XML response if Message is Successfully Sent. 

			<?xml version="1.0" encoding="iso-8859-1" ?> 
				<sms> 
					<messageid>1480873</messageid> 
				</sms> 
		
		and if SMS not sent successfully you will receive the below response. 
		
			<sms> 
				<messageid>-1</messageid> 
			</sms> 
         */
//        
//        
//        
//
    	actionType = "Get SMS Delivery Report";
    	System.out.println ("Request type to be sent: " + actionType);
//		//String stringDeliveryReportURL="http://sms.apexinfo.co.in/getDLR.jsp";
		String stringDeliveryReportURL="http://sms.jothimailservers.com//getDLR.jsp";
		String stringDeliveryReportRequestXML = "<?xml version=\"1.0\"?>"
				+ "<smslist>"
					+ "<sms>"
//						+ "<user>" + ApexConfig.getUser() + "</user>"
//						+ "<password>" + ApexConfig.getPassword() + "</password>"
						+ "<user>ravitup</user>"
						+ "<password>vu2rcn</password>"
						//Optional parms
						+ "<messageid>110298723</messageid>"
//						+ "<externalid>0</externalid>"
//						+ "<drquantity>1000</drquantity>"
//						+ "<fromdate>2015-01-01 00:00:00</fromdate>"
//						+ "<todate>2015-01-31 00:00:00</todate>"
//						+ "<redownload>yes</redownload>"
						+ "<responcetype>xml</responcetype>"
					+ "</sms>"
				+ "</smslist>";
 
        success = XMLDataPost(stringDeliveryReportURL, stringDeliveryReportRequestXML, "");
        //success = XMLDataPost(ApexConfig.getSMS_Get_Delivery_Report_URL(), stringDeliveryReportRequestXML, "");
        System.out.println(" ");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(" ");
//        /* Sample Delivery report response xml
//          <response> 
//				<responsecode>0</responsecode> 
//				<resposedescription>Success</resposedescription> 
//				<dlristcount>1</dlristcount> 
//				<pendingdrcount>0</pendingdrcount> 
//				<drlist> 
//					<dr> 
//						<messageid>1</messageid> 
//						<externalid>0</externalid> 
//						<senderid>xxxx</senderid> 
//						<mobileno>xxxxxxxxxx</mobileno> 
//						<message>message</message> 
//						<submittime>yyyy-mm-dd hh:mm:ss</submittime> 
//						<senttime>yyyy-mm-dd hh:mm:ss</senttime> 
//						<deliverytime>yyyy-mm-dd hh:mm:ss</deliverytime> 
//						<status>status</status> 
//						<undeliveredreason>reason</undeliveredreason> 
//						<details>details</details> 
//					</dr> 
//				</drlist> 
//			</response> 
//        */
//
//    	actionType = "SMS Send Delivery Acknowledgement";
//    	System.out.println ("Request type to be sent: " + actionType);
//		//String stringSendDeliveryAckURL="http://sms.apexinfo.co.in/dracknowledgment.jsp";
//		String stringSendDeliveryAckRequestXML = "<?xml version=\"1.0\"?>"
//				+ "<smslist>"
//					+ "<sms>"
//						+ "<userid>" + ApexConfig.getUser() + "</userid>"
//						+ "<password>" + ApexConfig.getPassword() + "</password>"
//						+ "<messageid>0</messageid>"
//						+ "<externalid>0</externalid>"
//						//optional parms
//					+ "</sms>"
//				+ "</smslist>";
// 
//        //success = XMLDataPost(stringSendDeliveryAckURL, stringSendDeliveryAckRequestXML, "");
//        success = XMLDataPost(ApexConfig.getSMS_Send_Delivery_Acknowledgement_URL(), stringSendDeliveryAckRequestXML, "");
//        System.out.println(" ");
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println(" ");
//        /* Sample response
//         <acknowledge> 
//			<code>205</code> 
//			<description>Acknowledge get</description> 
//		 </acknowledge> 
//         */
//
//    
//    	actionType = "Description of Undelivered SMS";
//    	System.out.println ("Request type to be sent: " + actionType);
//		//String stringUndeliveredSMSURL="http://sms.apexinfo.co.in/getundeliveredreasonanddescription.jsp";
//		String stringUndeliveredSMSRequestXML = "<?xml version=\"1.0\"?>"
//				+ "<smslist>"
//					+ "<sms>"
//						+ "<userid>" + ApexConfig.getUser() + "</userid>"
//						+ "<password>" + ApexConfig.getPassword() + "</password>"
//						//Optional parms
//					+ "</sms>"
//				+ "</smslist>";
// 
//        //success = XMLDataPost(stringUndeliveredSMSURL, stringUndeliveredSMSRequestXML, "");
//        success = XMLDataPost(ApexConfig.getSMS_Get_Undelivered_Details_URL(), stringUndeliveredSMSRequestXML, "");
//        System.out.println(" ");
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println(" ");
//        /* Sample Response
//	        <undeliveredreasondescription> 
//				<record> 
//					<smsstatus>status</smsstatus> 
//					<reasonfordelivered>reason</reasonfordelivered> 
//					<descriptionofreasonfordelivered>description</descriptionofreasonfordelivered> 
//				</record> 
//			</undeliveredreasondescription> 
//        */
//        
}
 
    private static boolean  XMLDataPost(String stringURL, String stringXML, String propertiesFileName){
 
        boolean success = false;
        HttpClient httpclient = new DefaultHttpClient();
 
        try {
 
        	HttpPost httpPost = new HttpPost(stringURL);
 
        	InputStream inputStream = new ByteArrayInputStream(stringXML.getBytes(StandardCharsets.UTF_8));
        	InputStreamEntity reqEntity = new InputStreamEntity(inputStream);
            //reqEntity.setContentType("text/xml; charset=ISO-8859-1");
            //reqEntity.setChunked(true);
            reqEntity.setChunked(false);
            
            httpPost.setEntity(reqEntity);
 
            System.out.println("Executing request " + httpPost.getRequestLine());
            httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookieSpecs.IGNORE_COOKIES);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
 
            System.out.println(response.getStatusLine());
            if(response.getStatusLine().getStatusCode() == 200){
                success = true;
                System.out.println ("HTTP post returned successfully!");
            }
            if (resEntity != null) {
               // System.out.println("Response content length: " + resEntity.getContentLength());
                System.out.println("Chunked?: " + resEntity.isChunked());
                InputStream responseIn = resEntity.getContent();
                System.out.println("Request : " );
                System.out.println(getFormattedXML(stringXML));
                System.out.println("Response : " );
                System.out.println(getFormattedXML(getStringFromInputStream(responseIn)));
            }
            EntityUtils.consume(resEntity);
        } 
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            httpclient.getConnectionManager().shutdown();
        }
 
        return success;
 
    }

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {
 
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
	
	private static String getFormattedXML(String xmlString) {
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
    
	private static String stripExtraChars(String inStr) {
		char tabChar = '\t';
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
				//skip chars
				if ((inStr.charAt(i) == ' ') || ( inStr.charAt(i) == tabChar)) continue;
				else outString = outString + inStr.charAt(i);
			} else
			{
				outString = outString + inStr.charAt(i);
			}
		}
		
		//System.out.println ("in : " + inStr);
		//System.out.println ("out : " + outString);
		return outString;
	}
	
}






















