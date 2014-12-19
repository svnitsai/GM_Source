package com.svnitsai.gm;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.database.provider.UserAccessProviderImpl;
import com.svnitsai.gm.database.generated.UserAccess;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * LoginServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Reads database to validate user login;
 * 				Creates session to remember user and user's role;
 * 
 */

public class LoginServlet extends HttpServlet {


public void doPost(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	
	//User authenticated?
	boolean isValidUser = true;
	
	// Use providerImpl to validate user
	UserAccessProviderImpl userAPI = new UserAccessProviderImpl();

	//Retrieve username and password from the request sent from JSP
	String userName = request.getParameter("jspUserName");
	String userPassword = request.getParameter("jspPassword");
	
	//Read database by the user name
	UserAccess userAccess= (UserAccess) userAPI.ReadByUsername(userName);
	if (userAccess != null) {
		if (userAccess.getPassword().compareTo(userPassword) == 0) {
			isValidUser = true;
			LogUtil.log(LogUtil.Message_Type.Information, "User " + userAccess.getUserName() + " has logged in @ " + DateUtil.getCurrentTimestamp().toString());
		} else LogUtil.log(LogUtil.Message_Type.Information," Invalid password entered for user: " + userName + " !!!");
	}
	else LogUtil.log(LogUtil.Message_Type.Information," Invalid User " + userName + " login !!!");

	//If validUser create Session
	if (isValidUser) {
		HttpSession session = request.getSession(true);	    
        session.setAttribute("User",userName); 
        if("admin".equalsIgnoreCase(userName))
        {
        	session.setAttribute("Role", "ADMIN");
        }
        else
        {
        	session.setAttribute("Role", "USER");
        }
        session.setAttribute("Role", userAccess.getRole().toUpperCase());

        request.setAttribute("currentUser",userAccess); 
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	} else {
        request.setAttribute("userName", userName);
		request.setAttribute("passWord", userPassword);
        request.setAttribute("errorMessage","Invalid user / password!"); 
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/login.jsp");
		dispatcher.forward(request, response);
	}
}
catch (Throwable theException) 	    
{
     System.out.println(theException); 
}
       }
	}