package com.svnitsai.gm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.database.provider.SMSTemplateCRUDProvider;
import com.svnitsai.gm.database.generated.Smstemplate;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * SMSTemplateServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Invoked by smsTemplate.jsp to do CRU operation on SMSTemplate table.
 * 
 */

public class SMSTemplateServlet extends HttpServlet {

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
			System.out.println(" Inside SMSTemplateServlet...");

			String templateAction = request.getParameter("templateAction");
			LogUtil.log(LogUtil.Message_Type.Information, " Value of TEMPLATE action request is : " + templateAction);

			int returnCode = 0;

			String inTemplateName = request.getParameter("jsptemplateName");
			String inTemplateMessage = request.getParameter("jsptemplateMessage");
			String inTemplateId = request.getParameter("jsptemplateId");

			//Add
			if (templateAction.equalsIgnoreCase("add")) {
				SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();

				Smstemplate SMSTemplate = new Smstemplate();
				SMSTemplate.setTemplateName(inTemplateName);
				SMSTemplate.setTemplate(inTemplateMessage);
				SMSTemplate.setCreatedDate(DateUtil.getCurrentTimestamp());
				SMSTemplate.setCreatedBy(userName);
				SMSTemplate.setUpdatedDate(DateUtil.getCurrentTimestamp());
				SMSTemplate.setUpdatedBy(userName);

				returnCode = SMSTemplateInfoCrud.createSMSTemplate(SMSTemplate);
				LogUtil.log(LogUtil.Message_Type.Information, " Add done in SMSTemplate table; return code from CRUD provider is " + returnCode);

			}

			//Update
			if (templateAction.equalsIgnoreCase("update")) {
				SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();

				Smstemplate SMSTemplate = new Smstemplate();
				SMSTemplate.setTemplateId(Util.convertToInt(inTemplateId));
				SMSTemplate.setTemplateName(inTemplateName);
				SMSTemplate.setTemplate(inTemplateMessage);
				SMSTemplate.setUpdatedDate(DateUtil.getCurrentTimestamp());
				SMSTemplate.setUpdatedBy(userName);

				returnCode = SMSTemplateInfoCrud.updateSMSTemplate(SMSTemplate);
				LogUtil.log(LogUtil.Message_Type.Information, " Update done in SMSTemplate table for template " + inTemplateName
						+ "; return code from CRUD provider is " + returnCode);
			}

			//Handle delete record
			if (templateAction.equalsIgnoreCase("delete")) {
				SMSTemplateCRUDProvider SMSTemplateInfoCrud = new SMSTemplateCRUDProvider();

				Smstemplate SMSTemplate = new Smstemplate();
				SMSTemplate.setTemplateId(Util.convertToInt(inTemplateId));
				SMSTemplate.setTemplateName(inTemplateName);
				SMSTemplate.setTemplate(inTemplateMessage);

				returnCode = SMSTemplateInfoCrud.deleteSMSTemplate(SMSTemplate);
				LogUtil.log(LogUtil.Message_Type.Information, " Delete done in SMSTemplate table for template " + inTemplateName
						+ "; return code from CRUD provider is " + returnCode);
			}

			if ("templateAdd".equals(templateAction)) {
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