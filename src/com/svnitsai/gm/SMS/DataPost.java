package com.svnitsai.gm.SMS;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.svnitsai.gm.database.generated.SmsrequestResponse;
import com.svnitsai.gm.database.provider.SMSRequestResponseLogProvider;
import com.svnitsai.gm.util.log.LogUtil;

/* 
* DataPost.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Posts XML to a HTTP server using apache.http 4.3
* 
*/

public class DataPost {
	
	/*
	 * XML posting not used as of 15 Jan 2015
	 */
	public int XMLDataPost (String inStringURL, String inStringRequestXML, StringBuffer outStringBufferResponse) {
		int returnCode = 0; //initialize return code
		
		//Validate requestXML
		if ((inStringRequestXML == null) || (inStringRequestXML.equals(""))) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_EMPTY_REQUEST.getId() + ": " + SMS_Error.HTTP_EMPTY_REQUEST.getMessage() + "; Request is: " +  inStringRequestXML);
			return SMS_Error.HTTP_EMPTY_REQUEST.getId();
		}
		String inStringRequestXML_Validated = SMS_Utils.getFormattedXML(inStringRequestXML);
		if ((inStringRequestXML_Validated == null) || (inStringRequestXML_Validated.equals(""))) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_INVALID_REQUEST_XML.getId() + ": " + SMS_Error.HTTP_INVALID_REQUEST_XML.getMessage() + "; Request is: " +  inStringRequestXML);
			return SMS_Error.HTTP_INVALID_REQUEST_XML.getId();
		}
		
		//Create httpClient
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();

		//Create request Entity
		HttpPost httpPost = new HttpPost(inStringURL);
		InputStream inputStream = new ByteArrayInputStream(inStringRequestXML.getBytes(StandardCharsets.UTF_8));
		//InputStream inputStream = new ByteArrayInputStream(inStringRequestXML.getBytes(StandardCharsets.ISO_8859_1));
		InputStreamEntity requestEntity = new InputStreamEntity(inputStream);
		requestEntity.setChunked(false);
		
		httpPost.setEntity(requestEntity);
		String responseString = null;
		
		try {
			LogUtil.log(LogUtil.Message_Type.Information, "Executing request " + httpPost.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpPost,context);
			//TODO: Write request/response to database
			
			HttpEntity responseEntity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200) {
				LogUtil.log(LogUtil.Message_Type.Information, "Http Post is successful!");
				LogUtil.log(LogUtil.Message_Type.Information, response.getStatusLine().toString());
				InputStream responseIS = responseEntity.getContent();
				responseString = SMS_Utils.getStringFromInputStream(responseIS);
				
				if (responseString == "" ) {
					LogUtil.log(LogUtil.Message_Type.Error, " Input Stream to String conversion failed! ");
					returnCode = SMS_Error.HTTP_POST_FAILED.getId();
				} else {
					outStringBufferResponse.append(responseString);
					
//	                System.out.println("Request : " );
//	                System.out.println(SMS_Utils.getFormattedXML(inStringRequestXML));
//	                System.out.println("Response : " );
//	                System.out.println(SMS_Utils.getFormattedXML(responseString));
					LogUtil.log(LogUtil.Message_Type.Information, "Request : ");
					LogUtil.log(LogUtil.Message_Type.Information, SMS_Utils.getFormattedXML(inStringRequestXML));
					LogUtil.log(LogUtil.Message_Type.Information, "Response : ");
					LogUtil.log(LogUtil.Message_Type.Information, SMS_Utils.getFormattedXML(responseString));
				}

			} else {//Could be gateway/internet issue; try after some time
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.HTTP_POST_FAILED.getId() + ": " + SMS_Error.HTTP_POST_FAILED.getMessage() );
				LogUtil.log(LogUtil.Message_Type.Information, response.getStatusLine().toString());
				returnCode = SMS_Error.HTTP_POST_FAILED.getId();
			}
			
			//Log the call to SMS vendor
			//returnCode = logRequestResponse(inStringRequestXML, responseString, DateUtil.getCurrentTimestamp(), requestSubmittedTS); //TODO pass it from contact history
			//TODO: Check return code

			
			
		} catch (IOException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() );
			e.printStackTrace();
			returnCode = SMS_Error.HTTP_IO_EXCEPTION.getId();
		}
		finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() );
				e.printStackTrace();
				returnCode = SMS_Error.HTTP_IO_EXCEPTION.getId();
			}
		}
		
		return returnCode;
	}
	
	/*
	 * Log the request / response on to a table
	 */
	private int logRequestResponse (String requestString, String responseXML, String initiatedTS) {
		
		SmsrequestResponse smsRequestResponse = new SmsrequestResponse();
		smsRequestResponse.setRequestInitiatedTs(initiatedTS); 
		
		SMSRequestResponseLogProvider sMSRequestResponseLogProvider = new SMSRequestResponseLogProvider();
		smsRequestResponse.setRequest(requestString);
		smsRequestResponse.setResponse(responseXML);

		int returnCode = sMSRequestResponseLogProvider.createRequestResponseLog(smsRequestResponse);
		LogUtil.log(LogUtil.Message_Type.Information, " Inserted SMS request/response in DataPost; return code from provider is " + returnCode);
		
		return returnCode;
	}
	
	/*
	 * Not used as of 15 Jan 2015
	 */
	public int inlinePost() {
	    try {
	        URL url;
	        URLConnection urlConnection;
	        DataOutputStream outStream;
	        DataInputStream inStream;
	 
	        // Build request body user=username&password=demo1234
	        //String body =
	        //"user=" + URLEncoder.encode("apexdemo", "UTF-8") +
	        //"&password=" + URLEncoder.encode("demo1234", "UTF-8");
	 
	        String body = "";
	        // Create connection
	        url = new URL("http://sms.apexinfo.co.in/sendsms.jsp?user=username&password=demo1234");
	        urlConnection = url.openConnection();
	        ((HttpURLConnection)urlConnection).setRequestMethod("POST");
	        urlConnection.setDoInput(true);
	        urlConnection.setDoOutput(true);
	        urlConnection.setUseCaches(false);
	        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        urlConnection.setRequestProperty("Content-Length", ""+ body.length());
	 
	        // Create I/O streams
	        outStream = new DataOutputStream(urlConnection.getOutputStream());
	        inStream = new DataInputStream(urlConnection.getInputStream());
	 
	        // Send request
	        outStream.writeBytes(body);
	        outStream.flush();
	        outStream.close();
	 
	        // Get Response
	        // - For debugging purposes only!
	        String buffer;
	        while((buffer = inStream.readLine()) != null) {
	            System.out.println(buffer);
	        }
	 
	        // Close I/O streams
	        inStream.close();
	        outStream.close();
	    }
	    catch(Exception ex) {
	        System.out.println("Exception cought:\n"+ ex.toString());
	    }
		return 0;
		 
	}
	
	/*
	 * Invoke URL with parameters as part of the URL
	 */
	public int inlineHttpClientPost(String inStringURL, String inStringRequestParm
			, StringBuffer outStringBufferResponse, String insmsGrouping) {
		//Validate requestParm
		if ((inStringRequestParm == null) || (inStringRequestParm.equals(""))) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_EMPTY_REQUEST.getId() + ": " + SMS_Error.HTTP_EMPTY_REQUEST.getMessage() + "; Request is: " +  inStringRequestParm);
			return SMS_Error.HTTP_EMPTY_REQUEST.getId();
		}
        
        String httpPostURL = inStringURL + inStringRequestParm;

		//Create httpClient
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();

		//Create request Entity
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(httpPostURL);
		} catch (IllegalArgumentException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_INVALID_REQUEST_URL.getId() + ": " + SMS_Error.HTTP_INVALID_REQUEST_URL.getMessage() 
					+ "; Invalid character, like space, found in request url : " +  httpPostURL);
			e.printStackTrace(); //invalid character in sendRequest like space
			return SMS_Error.HTTP_INVALID_REQUEST_URL.getId();
		}

		String responseString = null;
		int returnCode = 0;
		
		try {
			LogUtil.log(LogUtil.Message_Type.Information, "Executing request " + httpPost.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpPost,context);
			
			HttpEntity responseEntity = response.getEntity();
			
			if (response.getStatusLine().getStatusCode() == 200) {
				LogUtil.log(LogUtil.Message_Type.Information, "Http Post is successful!");
				LogUtil.log(LogUtil.Message_Type.Information, response.getStatusLine().toString());
				InputStream responseIS = responseEntity.getContent();
				responseString = SMS_Utils.getStringFromInputStream(responseIS);
				
				if (responseString == "" ) {
					LogUtil.log(LogUtil.Message_Type.Error, " Input Stream to String conversion failed! ");
					returnCode = SMS_Error.HTTP_POST_FAILED.getId();
				} else {
					outStringBufferResponse.append(responseString);
					
//	                LogUtil.log(LogUtil.Message_Type.Information, "Request : ");
//	                System.out.println(httpPostURL);
//	                LogUtil.log(LogUtil.Message_Type.Information, "Response : ");
//	                System.out.println(SMS_Utils.getFormattedXML(responseString));
					LogUtil.log(LogUtil.Message_Type.Information, "Request : ");
					LogUtil.log(LogUtil.Message_Type.Information, httpPostURL);
					LogUtil.log(LogUtil.Message_Type.Information, "Response : ");
					LogUtil.log(LogUtil.Message_Type.Information, SMS_Utils.getFormattedXML(responseString));

				}

			} else {//Could be gateway/internet issue; try after some time
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.HTTP_POST_FAILED.getId() + ": " + SMS_Error.HTTP_POST_FAILED.getMessage() );
				LogUtil.log(LogUtil.Message_Type.Information, response.getStatusLine().toString());
				returnCode = SMS_Error.HTTP_POST_FAILED.getId();
			}
			
			//Log the call to SMS vendor
			returnCode = logRequestResponse(httpPostURL, responseString, insmsGrouping);
			if (returnCode != 0) {
				LogUtil.log(LogUtil.Message_Type.Error, " Writting Request/Response log to data base failed! ");
			}

		} catch (IOException e) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() );
			e.printStackTrace();
			returnCode = SMS_Error.HTTP_IO_EXCEPTION.getId();
		}
		finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				LogUtil.log(LogUtil.Message_Type.Error, 
						SMS_Error.HTTP_IO_EXCEPTION.getId() + ": " + SMS_Error.HTTP_IO_EXCEPTION.getMessage() );
				e.printStackTrace();
				returnCode = SMS_Error.HTTP_IO_EXCEPTION.getId();
			}
		}
		
		return returnCode;
	}
	
}
