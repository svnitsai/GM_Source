package com.svnitsai.gm.SMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.svnitsai.gm.SMS.VendorSpecific.SMSCommunicator;
import com.svnitsai.gm.SMS.VendorSpecific.Apex.ApexSMSCommunicator;
import com.svnitsai.gm.constants.SMSStatus;
import com.svnitsai.gm.constants.SMSUIMessage;
import com.svnitsai.gm.database.generated.SmscontactHistory;
import com.svnitsai.gm.database.provider.SMSContactHistoryCRUDProvider;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/* 
* SMSManager.java
* 
* @author: 		SVN Systems and Innovations
* 
* @purpose: 	Called for performing SMS communication outside.
* 
*/

public class SMSManager {

	/*
	 * This is a generic method that calls vendor specific packages to get SMS sent;
	 * 			vendorName: Name of the vendor to be used to send the message
	 * 			requestGroup: Group identifier for SMS to be sent from contact history
	 * 			uiDisplayString: String returned to be shown to user
	 */
	public int submitSendSMS (String vendorName, String requestGroup, StringBuffer uiDisplayString) {
		
		//Set Vendor context for SMS Communication
		SMSManager smsManager = new SMSManager();
		SMSCommunicator smsCommunicator = smsManager.getSMSCommunicator(vendorName);
		if (smsCommunicator == null) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.GENERIC_SYSTEM_ERROR.getId() + ": " + SMS_Error.GENERIC_SYSTEM_ERROR.getMessage() 
					+ "; Error creating SMSCommunicator for vendor: " +  vendorName);
			uiDisplayString.append(getUIMessage(SMS_Error.GENERIC_SYSTEM_ERROR));
			return SMS_Error.GENERIC_SYSTEM_ERROR.getId();
		} 
		
		//Build Request Object - from database
		boolean atleastOneSMSSent = false;

//		System.out.println (" SMSManager Req. initiated TS " + requestGroup);

		SMSSendRequest sendRequest = new SMSSendRequest();
		SMSContactHistoryCRUDProvider contactHistoryCRUDProvider = new SMSContactHistoryCRUDProvider();
		int returnCodeSMSCommunicator = 0;
		List result = (List) contactHistoryCRUDProvider.getSMSContactHistoryList(requestGroup);
		if ((result != null) && (result.size() > 0)) { 
			for (Object object : result) { 
				Map row = (Map) object; 
				//System.out.println(" ");
				
				//TODO: Delme
//				System.out.println(" HistoryId: " + row.get("HistoryId")
//						  + " RequestInitiatedTS: " + row.get("RequestInitiatedTS")
//						  + " RequestVendorReferrence: " + row.get("RequestVendorReferrence")
//						  + " CustId: " + row.get("CustId")
//						  + " CustCode: " + row.get("CustCode")
//						  + " CustName: " + row.get("CustName")
//						  + " SMSMobileNumber: " + row.get("SMSMobileNumber")
//						  + " SMSMobileOwnerName: " + row.get("SMSMobileOwnerName")
//						  + " SMSMessage: " + row.get("SMSMessage")
//						  + " SMSVendor: " + row.get("SMSVendor")
//						  + " SentTimeStamp: " + row.get("SentTimeStamp")
//						  + " FailedReason: " + row.get("FailedReason")
//						  + " Status: " + row.get("Status")
//						  + " StatusUpdatedTS: " + row.get("StatusUpdatedTS")
//						  );
				
				//Send SMS for each selected customer		
				sendRequest.setSMSMessage(row.get("SMSMessage").toString());
				sendRequest.setMobileNumber(row.get("SMSMobileNumber").toString());
				sendRequest.setSMSMessageGrouping(requestGroup); // used to write SMSRequestResponse log
				
				//Create dummy response object
				SMSSendResponse sendResponse = new SMSSendResponse();
				
				//Call SMSCommunicator Send method
				returnCodeSMSCommunicator = smsCommunicator.submitSendSMS(sendRequest, sendResponse);
				SmscontactHistory smsCHUpdate = new SmscontactHistory();

				smsCHUpdate.setHistoryId(Integer.parseInt(row.get("HistoryId").toString()));
				smsCHUpdate.setSentTimeStamp(DateUtil.getCurrentTimestamp());
				smsCHUpdate.setStatusUpdatedTs(DateUtil.getCurrentTimestamp());
				
				//Use Response Object and update contact history
				if (returnCodeSMSCommunicator != 0) {
					smsCHUpdate.setStatus(SMSStatus.FAILED.getStatusCode());
					smsCHUpdate.setRequestVendorReferrence(null);
					smsCHUpdate.setFailedReason(sendResponse.getFailureReason());
					LogUtil.log(LogUtil.Message_Type.Error, " Call to smsCommunicator for vendor " 
							+ smsCommunicator.getName() + " failed! The error message is " + sendResponse.getFailureReason());
				} else {
					atleastOneSMSSent = true;
					smsCHUpdate.setStatus(SMSStatus.SENT.getStatusCode());
					smsCHUpdate.setRequestVendorReferrence(sendResponse.getSuccessReferenceId());
					smsCHUpdate.setFailedReason(null);
				}
				int returnCHProviderCode = contactHistoryCRUDProvider.updateSMSContactHistory(smsCHUpdate);
				if (returnCHProviderCode != 0) {
					LogUtil.log(LogUtil.Message_Type.Error, "Writting to LOG table failed! Return code: " 
							+ returnCHProviderCode);
				}

				if (sendResponse.getSmsUIMessage() == SMSUIMessage.SYSTEM_ERROR) { //First priority
					uiDisplayString.delete(0,uiDisplayString.length());
					uiDisplayString.append(sendResponse.getSmsUIMessage().getMessage());
					return returnCodeSMSCommunicator; //Stop further SMS sending process; property file error...
				}
				
				if (sendResponse.getSmsUIMessage() == SMSUIMessage.TRYLATER_INTERNET_ISSUE) {//Last priority
					if (uiDisplayString.length() == 0)
						uiDisplayString.append(sendResponse.getSmsUIMessage().getMessage()); 
					//Send this message to user if no SMS is sent successfully; and user_actional mesg not already created
				}

				if (   (sendResponse.getSmsUIMessage() == SMSUIMessage.USER_ACTIONABLE_ID_ISSUE) 
				    || (sendResponse.getSmsUIMessage() == SMSUIMessage.SMS_VENDOR_REJECTED)) {//Third priority
					uiDisplayString.delete(0,uiDisplayString.length());
					uiDisplayString.append(sendResponse.getSmsUIMessage().getMessage()); 
					//Send this message to user if no SMS is sent successfully;
				}
			} //for loop

			if (atleastOneSMSSent) { //Second priority
				//Send generic message to look at history
				uiDisplayString.delete(0,uiDisplayString.length());
				uiDisplayString.append(SMSUIMessage.SENT_SUCCESSFULLY.getMessage());
			}
		}
		if (uiDisplayString.indexOf(SMSUIMessage.SENT_SUCCESSFULLY.getMessage()) > -1) {
			return 0;
		} else return returnCodeSMSCommunicator;
	}

	/*
	 * submitSendBulkSMS (String vendorName) - Not supported by Apex.  Apex does not accept XMLs!!!
	 */
	public int submitSendBulkSMS (String vendorName) {
		//TODO: Other parms: groupid/ts, return stringBuffer
		
		//Set Vendor context for SMS Communication
		SMSManager smsManager = new SMSManager();
		SMSCommunicator smsCommunicator = smsManager.getSMSCommunicator(vendorName);
		if (smsCommunicator == null) {
			LogUtil.log(LogUtil.Message_Type.Error, 
					SMS_Error.GENERIC_SYSTEM_ERROR.getId() + ": " + SMS_Error.GENERIC_SYSTEM_ERROR.getMessage() 
					+ "; Error creating SMSCommunicator for vendor: " +  vendorName);
			return SMS_Error.GENERIC_SYSTEM_ERROR.getId();
		} 
		
		//Build Request Object - from database TODO:
		List <SMSSendRequest> sendRequestList = new ArrayList <SMSSendRequest>();
		SMSSendRequest sendRequest = new SMSSendRequest();
		sendRequest.setSMSMessage("Message Content");
		sendRequest.setMobileNumber("91XXXXXXXX");
		
		sendRequestList.add(sendRequest);
		
		sendRequest = new SMSSendRequest();
		sendRequest.setSMSMessage("Message Content");
		sendRequest.setMobileNumber("91XXXXXX12");
		
		sendRequestList.add(sendRequest);
		//Create dummy response object
		SMSSendResponse sendResponse = new SMSSendResponse();
		
		//Call SMSCommunicator Send method
		int returnCode = smsCommunicator.submitSendSMS(sendRequestList, sendResponse);
		if (returnCode != 0) {
			LogUtil.log(LogUtil.Message_Type.Error, " Call to smsCommunicator for vendor " + smsCommunicator.getName() + " failed!");
			LogUtil.log(LogUtil.Message_Type.Error, " Return Code:  " + sendResponse.getReturnCode());
			LogUtil.log(LogUtil.Message_Type.Error, " Failed Reason:  " + sendResponse.getFailureReason());
			return returnCode;
		}
		
		//Use Response Object
//		System.out.println (" from SMS Manager : " + sendResponse.getFailureReason());
//		System.out.println ("      return code : " + sendResponse.getReturnCode());
//		System.out.println ("      message id  : " + sendResponse.getSuccessReferenceId());
		//Return
		
		return 0;
	}
	
	/*
	 * Not used at this time; goes with submitSendBulkSMS
	 */
	public int getDeliveryReportSMS () {
		
		return 0;
	}

	/*
	 * Get appropriate Vendor SMS Communicator based on the vendor
	 *   Vendors supported as of Jan 15, 2015:
	 *   		Apex
	 */
	private SMSCommunicator getSMSCommunicator(String vendorName){
		if (vendorName.equalsIgnoreCase("Apex")) {
			return new ApexSMSCommunicator(vendorName);
		}
		return null;
	}
	
	/*
	 * Get UI message for display
	 */
	private String getUIMessage (SMS_Error errorCode) {
		String outUIMessage = "";
		switch (errorCode) {
			case GENERIC_SYSTEM_ERROR: {
				outUIMessage = SMSUIMessage.SYSTEM_ERROR.getMessage();
				break;
			}
			default:
				break;
		}
		return outUIMessage;
	}
	
//	public static void main (String [] arg) {
//		SMSManager smsManager = new SMSManager();
//		StringBuffer uiSB = new StringBuffer();
//		int returnCode = smsManager.submitSendSMS("Apex","2015-01-10 07:54:00.000",uiSB);
//		System.out.println (" UI Message: " + uiSB.toString());
//		//8002 - may be internet connection issue
//		//8503 - may be SMS user id password issue
//		//2 - SMS request format changed11
//		if (returnCode != 0) {
//			System.out.println(" SMSManager submitSend failed with return code " + returnCode);
//			if (returnCode == SMS_Error.HTTP_POST_FAILED.getId()) {
//				System.out.println (" Internet issue! Please try after some time. ");
//			}
//		}
//	}
}
