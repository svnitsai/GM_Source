package com.svnitsai.gm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.util.log.LogUtil;

/*
 * SMSHistoryServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Invoked by smsHistory.jsp to do date range action
 * 
 */

public class SMSHistoryServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			//Get user from session to identify the user 
			HttpSession session = request.getSession(false);
			System.out.println(" Inside SMSHistoryServlet...");

			String historyAction = request.getParameter("historyAction");
			LogUtil.log(LogUtil.Message_Type.Information, " Value of HISTORY action request is : " + historyAction);

			String inCHFromDate = request.getParameter("jspCHFromDate");
			String inCHToDate = request.getParameter("jspCHToDate");

			if ("getHistory".equals(historyAction)) {
				session.setAttribute("jspCHFromDate", inCHFromDate);
				session.setAttribute("jspCHToDate", inCHToDate);
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 0);
			} // used for date range changes
			else {
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 99);
			} // Other options
			
		} catch (Throwable theException) {
	        response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write("ReturnCode_" + 99);
			System.out.println(theException);
		}
	}
}