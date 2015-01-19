package com.svnitsai.gm;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.SMS.SMSManager;
import com.svnitsai.gm.constants.SMSStatus;
import com.svnitsai.gm.database.provider.SMSContactHistoryCRUDProvider;
import com.svnitsai.gm.database.generated.SmscontactHistory;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.display.DisplayUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * SMSSendServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Invoked by smsSend.jsp to do "SMS Communication".
 * 
 */

public class SMSSendServlet extends HttpServlet {
	
	/*
	 * Check if variables are used?
	 */
	private boolean isTemplateUsed(String smsMessage) {
		StringBuffer var = new StringBuffer();
		if ( getTemplateVariable (smsMessage, var) > -1) return true;
		else return false;
	}

	//Get first variable if one exists
	private int getTemplateVariable (String smsMessage, StringBuffer outVariable) { 
		return getTemplateVariable (smsMessage, 0, outVariable); 
	}
	
	private int getTemplateVariable (String smsMessage, int startPos, StringBuffer outVariable) {
		
		outVariable.setLength(0); //erase contents
		int begPar = smsMessage.indexOf("[", startPos);
		int endPar = smsMessage.indexOf("]", begPar);
		if ((begPar != -1) && (endPar != -1) && (begPar < endPar)) { 
			outVariable.append(smsMessage.substring(begPar + 1, endPar));
//			System.out.println (" Variable is  " + outVariable.toString() + " beg is " + begPar);
			return begPar;
		}
		return -1;
	}
	
	
//	public static void main (String [] args) {
////		String smsMessage = "Dear Mr.[contact_NAME], with reference to your invoice.  Your [due_amount] payment which was due on [due_date] is not settled.  Please settle.";
////		StringBuffer sbVariable = new StringBuffer();
////		String inflatedSMSMessage = smsMessage;
////		System.out.println (smsMessage);
////		SMSSendServlet serv = new SMSSendServlet();
////		if (serv.isTemplateUsed(smsMessage)) {
//////			System.out.println (" Template is used ");
////			int begPos = serv.getTemplateVariable (smsMessage, -1, sbVariable);
////			while (begPos > -1) {
////				System.out.println ( "pos : " + begPos + " variable: "  + sbVariable.toString());
////				if (sbVariable.toString().equalsIgnoreCase("contact_name")) {
////					inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) + "Ramesh" 
////					   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length()+2); 
////				}
////				if (sbVariable.toString().equalsIgnoreCase("due_amount")) {
////					inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) + "Rs.1,00,000" 
////					   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length() + 2); 
////				}
////				if (sbVariable.toString().equalsIgnoreCase("due_date")) {
////					inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) + "12 Dec. 2014" 
////					   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length() + 2); 
////				}
////				begPos = serv.getTemplateVariable (inflatedSMSMessage, begPos + 1, sbVariable);
////			}
////		}
////		System.out.println (inflatedSMSMessage);
//		for (int i = 0; i < 1000; i++) {
//			Timestamp requestInitiatedTS = DateUtil.getCurrentTimestamp(); //SMS group id
//			Date requestInitiatedDate = DateUtil.getCurrentTimestamp(); //SMS group id
//			System.out.println ( " Req init ts " + requestInitiatedTS);
//			System.out.println ( "        date " + requestInitiatedDate);
//		}
//	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			//Get user from session to identify the user who updates CustomerBank info
			HttpSession session = request.getSession(false);
			String userName = (String) session.getAttribute("User");

			String smsAction = request.getParameter("smsAction");
			LogUtil.log(LogUtil.Message_Type.Information, " Value of SEND action request is : " + smsAction);

//			ArrayList<String> parameterNames = new ArrayList<String>();
//			 Enumeration enumeration = request.getParameterNames();
//			    while (enumeration.hasMoreElements()) {
//			        String parameterName = (String) enumeration.nextElement();
//			        System.out.println("Parameters passed " + parameterName);
//			        parameterNames.add(parameterName);
//			    }			
			int returnCode = 0;

			String inRecipientList = request.getParameter("jspRecipientList");
			String [] inCustIdArray = request.getParameterValues("jspCustIdArray[]");
			String [] inReferenceNumberArray = request.getParameterValues("jspReferenceNumberArray[]");
			String [] inCustNameArray = request.getParameterValues("jspCustNameArray[]");
			String [] inSMSMobileOwnerNameArray = request.getParameterValues("jspSMSMobileOwnerNameArray[]");
			String [] inSMSMobileNumberArray = request.getParameterValues("jspSMSMobileNumberArray[]");
			String [] inDueDateArray = request.getParameterValues("jspDueDateArray[]");
			String [] inDueAmountArray = request.getParameterValues("jspDueAmountArray[]");
			String [] inInvoiceNumberArray = request.getParameterValues("jspInvoiceNumberArray[]");
			String inSMSMessage = request.getParameter("jspSMSMessageArray");
			String [] inCustCodeArray = request.getParameterValues("jspCustCodeArray[]");
			String smsVendor = request.getParameter("jspsmsVendor");

			if ("getRecipients".equals(smsAction)) {
				session.setAttribute("recipientList", inRecipientList);
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 0);
			} else if ("sendSMS".equals(smsAction)) {
				if (inCustIdArray!=null) {
					LogUtil.log(LogUtil.Message_Type.Information, " Number of SMS messages to send : " + inCustIdArray.length);
					if (inCustIdArray.length > 0) {
						String requestInitiatedTS = DateUtil.getCurrentTimestamp().toString(); //SMS group id
						LogUtil.log(LogUtil.Message_Type.Information, " SMS Message grouping : " + requestInitiatedTS);

						for (int i = 0; i < inCustIdArray.length; i++) {
							String inflatedSMSMessage = inSMSMessage;
							//Check if template used
							StringBuffer sbVariable = new StringBuffer();
							if (isTemplateUsed(inSMSMessage)) {//Inflate template variables
								int begPos = getTemplateVariable (inSMSMessage, -1, sbVariable);
								while (begPos > -1) {
									//System.out.println ( "pos : " + begPos + " variable: "  + sbVariable.toString());
									if (sbVariable.toString().equalsIgnoreCase("contact_name")) {
										inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) + inSMSMobileOwnerNameArray [i] 
										   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length()+2); 
									}
									if (sbVariable.toString().equalsIgnoreCase("due_amount")) {
										inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) 
										   + "Rs." + DisplayUtil.getDisplayAmount(inDueAmountArray [i]) 
										   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length() + 2); 
									}
									if (sbVariable.toString().equalsIgnoreCase("due_date")) {
										inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) 
										   + DisplayUtil.getDisplayDate(inDueDateArray [i], new SimpleDateFormat("yyyy/MM/dd")) 
										   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length() + 2); 
									}
									if (sbVariable.toString().equalsIgnoreCase("invoice_number")) {
										inflatedSMSMessage = inflatedSMSMessage.substring(0, begPos) + inInvoiceNumberArray [i] 
										   + inflatedSMSMessage.substring(begPos + sbVariable.toString().length() + 2); 
									}
									begPos = getTemplateVariable (inflatedSMSMessage, begPos + 1, sbVariable);
								}
							}
//							System.out.println (inflatedSMSMessage);

							//Write to contact history table
							SmscontactHistory smsContactHistory = new SmscontactHistory();
							smsContactHistory.setRequestInitiatedTs(requestInitiatedTS);
							//smsContactHistory.setRequestVendorReferrence(inReferenceNumberArray);
							smsContactHistory.setCustId(Util.convertToLong(inCustIdArray[i]));
							smsContactHistory.setCustCode(Util.convertToInt(inCustCodeArray[i]));
							smsContactHistory.setCustName(inCustNameArray[i]);
							smsContactHistory.setSmsmobileNumber(Util.convertToLong(inSMSMobileNumberArray[i]));
							smsContactHistory.setSmsmobileOwnerName(inSMSMobileOwnerNameArray[i]);
							smsContactHistory.setSmsmessage(inflatedSMSMessage);
							smsContactHistory.setSmsvendor(smsVendor);
							smsContactHistory.setSentTimeStamp(null);
							smsContactHistory.setFailedReason(null);
							smsContactHistory.setPayCreferenceNumber(Util.convertToLong(inInvoiceNumberArray[i]));
							smsContactHistory.setStatus(SMSStatus.INITIAL.getStatusCode());
							smsContactHistory.setStatusUpdatedTs(DateUtil.getCurrentTimestamp());
							smsContactHistory.setSmsmessage(inflatedSMSMessage);

							SMSContactHistoryCRUDProvider contactHistoryCRUDProvider = new SMSContactHistoryCRUDProvider();
							returnCode = contactHistoryCRUDProvider.createSMSContactHistory(smsContactHistory);
							if (returnCode != 0) {
								LogUtil.log(LogUtil.Message_Type.Error, " SMSContactHistoryCRUD Provider failed " 
										+ returnCode);
							}
							//TODO: Check return code

						}//end for loop
						
						//Invoke SMS send
						SMSManager smsManager = new SMSManager();
						StringBuffer uiSB = new StringBuffer();
						returnCode = smsManager.submitSendSMS(smsVendor,requestInitiatedTS,uiSB);
//						System.out.println (" return code is " +  returnCode);
//						System.out.println (" UI Message: " + uiSB.toString());
						if (returnCode != 0) {
							session.setAttribute("templateName", "0");
					        response.setContentType("text/html;charset=UTF-8");
					        response.getWriter().write("ReturnCode_" + returnCode + "_" + uiSB.toString());
					        //response.getWriter().write(uiSB.toString());
						} else {
							session.setAttribute("templateName", "0");
					        response.setContentType("text/html;charset=UTF-8");
					        response.getWriter().write("ReturnCode_" + 0 + "_" + uiSB.toString());
					        //response.getWriter().write(uiSB.toString());
						}
						//Return to JSP
					}
				} else {//TODO: no sms message to send!
					session.setAttribute("templateName", "0");
			        response.setContentType("text/html;charset=UTF-8");
			        response.getWriter().write("ReturnCode_" + 0 + "_" + "No SMS message selected to send!");
				}
			} // SEND sms
			 else if ("templateAdd".equals(smsAction)) {
				session.setAttribute("templateName", "0");
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 0);
			} // used for template add first time
			else {
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + returnCode);
			} // used by add save, update save, delete
			
		} catch (Throwable theException) {
	        response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("ReturnCode_" + 99);
			System.out.println(theException);
		}
	}
}