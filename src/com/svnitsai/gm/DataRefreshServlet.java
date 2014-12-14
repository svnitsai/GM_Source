package com.svnitsai.gm;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.database.generated.UserAccess;
import com.svnitsai.gm.database.provider.StoredProcedureProvider;
import com.svnitsai.gm.database.provider.UserAccessProviderImpl;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * DataRefreshServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Triggers a database Stored Procedure to load data
 * 
 */

public class DataRefreshServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		
		String action = request.getParameter("action");
		
		System.out.println ("Value of action is " + action);

		if ("creditSalesDataRefresh".equals(action)) {
		    // Invoke creditSalesDataRefresh job here.
			try {

				// Use StoreProcedureProvider to call SP
				StoredProcedureProvider SPCall = new StoredProcedureProvider();

				// Call SP
				List result = (List) SPCall.callCreditSalesDataRefresh();
				if ((result != null) && (result.size() > 0)) {

				}
				
				// Reload Data Extract value
		        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);

			} catch (Throwable theException) {
				System.out.println(theException);
			}
			
		} else if ("customerDataRefresh".equals(action)) {
		    // Invoke customerDataRefresh job here.
			try {

				// Use StoreProcedureProvider to call SP
				StoredProcedureProvider SPCall = new StoredProcedureProvider();

				// Call SP
				List result = (List) SPCall.callCustomerDataRefresh();
				if ((result != null) && (result.size() > 0)) {

				}
				
		        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);

			} catch (Throwable theException) {
				System.out.println(theException);
			}
			
		}
		

	}
}
