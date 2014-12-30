package com.svnitsai.gm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.svnitsai.gm.database.provider.CustomerBankInfoCRUDProvider;
import com.svnitsai.gm.database.generated.CustomerBanks;
import com.svnitsai.gm.database.generated.CustomerBanksId;
import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.log.LogUtil;

/*
 * CustomerBankInfoServlet.java
 * 
 * @author: 	SVN Systems and Innovations
 * 
 * @purpose: 	Invoked by editCustomerBankInfo.jsp to do CRUD operation on CustomerBanks table.
 * 
 */

public class CustomerBankInfoServlet extends HttpServlet {

	private String getAction(String inString) {
		int underScorePosition = inString.indexOf("_");
		String action = "";
		if (underScorePosition < 0)
			action = inString;
		else
			action = inString.substring(0, underScorePosition);
		return action.trim();
	}

	private String getKey(String inString) {
		int underScorePosition = inString.indexOf("_");
		String dispKey = "";
		if (underScorePosition < 0) {
			dispKey = "0";
			if (   getAction(inString).equalsIgnoreCase("add")
				|| getAction(inString).equalsIgnoreCase("update")	
				|| getAction(inString).equalsIgnoreCase("delete")	
				) {
				dispKey = "";
			}
		} else
			dispKey = inString.substring(underScorePosition + 1,
					inString.length());
		return dispKey.trim();
	}

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

			String rowAction = request.getParameter("rowAction");
			LogUtil.log(LogUtil.Message_Type.Information, " Value of Customer bank info request is : " + rowAction);

			String action = getAction(rowAction);
			String dispKey = getKey(rowAction);
			int returnCode = 0;
			
			//Handle create new record
			if (rowAction.equalsIgnoreCase("add")) {
				String inCustBank = request.getParameter("jspCustBankName");
				String inCustBranch = request.getParameter("jspcustBankBranch");
				String inCustAccountName = request.getParameter("jspcustAccountName");
				String inCustAccountType = request.getParameter("jspcustAccountType");
				String inCustAccountNumber = request.getParameter("jspcustAccountNumber");
				String inCustCode = request.getParameter("jspcustIdFromCustomerScreen");
				
				CustomerBanks customerBank = new CustomerBanks();
				CustomerBanksId customerBankId = new CustomerBanksId();

				customerBankId.setCustCode(Util.convertToInt(inCustCode));
				customerBank.setId(customerBankId);

				customerBank.setCustBank(inCustBank);
				customerBank.setCustBankBranch(inCustBranch);
				customerBank.setCustBankAccountName(inCustAccountName);
				customerBank.setCustBankAccountType(inCustAccountType);
				customerBank.setCustBankAccountNumber(inCustAccountNumber);
				customerBank.setCustBankAccountName(inCustAccountName);
				customerBank.setCreatedDate(DateUtil.getCurrentTimestamp());
				customerBank.setCreatedBy(userName);
				customerBank.setUpdatedDate(DateUtil.getCurrentTimestamp());
				customerBank.setUpdatedBy(userName);

				CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
				returnCode = customerBankInfoCrud.createCustomerBank(customerBank);
				LogUtil.log(LogUtil.Message_Type.Information, " Add done in CustomerBankInfoServlet; return code from CRUD provider is " + returnCode);
			}

			//Handle update existing record
			if (rowAction.equalsIgnoreCase("update")) {
				String inCustBank = request.getParameter("jspCustBankName");
				String inCustBranch = request.getParameter("jspcustBankBranch");
				String inCustAccountName = request.getParameter("jspcustAccountName");
				String inCustAccountType = request.getParameter("jspcustAccountType");
				String inCustAccountNumber = request.getParameter("jspcustAccountNumber");
				String inCustCode = request.getParameter("jspcustIdFromCustomerScreen");
				String inCustBankId = request.getParameter("jspcustBankId");

				CustomerBanks customerBank = new CustomerBanks();
				CustomerBanksId customerBankId = new CustomerBanksId();

				customerBankId.setCustCode(Util.convertToInt(inCustCode));
				customerBankId.setCustBankId(Util.convertToInt(inCustBankId));
				customerBank.setId(customerBankId);

				customerBank.setCustBank(inCustBank);
				customerBank.setCustBankBranch(inCustBranch);
				customerBank.setCustBankAccountName(inCustAccountName);
				customerBank.setCustBankAccountType(inCustAccountType);
				customerBank.setCustBankAccountNumber(inCustAccountNumber);
				customerBank.setCustBankAccountName(inCustAccountName);
				// customerBank.setCreatedDate(DateUtil.getCurrentTimestamp());
				// customerBank.setCreatedBy(userName);
				customerBank.setUpdatedDate(DateUtil.getCurrentTimestamp());
				customerBank.setUpdatedBy(userName);

				CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
				returnCode = customerBankInfoCrud.updateCustomerBank(customerBank);
				LogUtil.log(LogUtil.Message_Type.Information, " Update done in CustomerBankInfoServlet; return code from CRUD provider is " + returnCode);
			}

			//Handle delete record
			if (rowAction.equalsIgnoreCase("delete")) {
				String inCustBank = request.getParameter("jspCustBankName");
				String inCustBranch = request.getParameter("jspcustBankBranch");
				String inCustAccountName = request.getParameter("jspcustAccountName");
				String inCustAccountType = request.getParameter("jspcustAccountType");
				String inCustAccountNumber = request.getParameter("jspcustAccountNumber");
				String inCustCode = request.getParameter("jspcustIdFromCustomerScreen");
				String inCustBankId = request.getParameter("jspcustBankId");

				CustomerBanks customerBank = new CustomerBanks();
				CustomerBanksId customerBankId = new CustomerBanksId();

				customerBankId.setCustCode(Util.convertToInt(inCustCode));
				customerBankId.setCustBankId(Util.convertToInt(inCustBankId));
				customerBank.setId(customerBankId);

				CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
				returnCode = customerBankInfoCrud.deleteCustomerBank(customerBank);
				LogUtil.log(LogUtil.Message_Type.Information, " Delete done in CustomerBankInfoServlet; return code from CRUD provider is " + returnCode);
			}

			session.setAttribute("customerBankInfoId", dispKey);
			if ("customerBankInfoUpdate".equals(action)) {
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 0);
			} // used for update when pressed first time

			else if ("customerBankInfoAdd".equals(action)) {
		        response.setContentType("text/html;charset=UTF-8");
		        response.getWriter().write("ReturnCode_" + 0);
			} // used for add when pressed first time

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