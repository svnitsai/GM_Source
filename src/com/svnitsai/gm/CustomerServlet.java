package com.svnitsai.gm;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.util.log.LogUtil;

/*
 * CustomerServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Used by customer.jsp screen
 * 
 */

public class CustomerServlet extends HttpServlet {

public void doPost(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	
	String selectedCustType = request.getParameter("filterCustomerBy");
	LogUtil.log(LogUtil.Message_Type.Information," Value of Customer Filter by  is : " + selectedCustType);
	
	// save customerType picked in the session variable before forwarding to customer.jsp screen
	HttpSession session = request.getSession(false);	    
    session.setAttribute("customerTypeProc", selectedCustType); 
	
	RequestDispatcher dispatcher = request.getRequestDispatcher("/web/customer.jsp");
	dispatcher.forward(request, response);

}
catch (Throwable theException) 	    
{
     System.out.println(theException); 
}
       }
	}