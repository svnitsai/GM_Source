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
 * @purpose: 	Invoked by editCustomerBankInfo.jsp to do CRUD operation on CustomerBanks table
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
		System.out.println(" action is " + action);
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
		System.out.println(" disp key is " + dispKey);
		return dispKey.trim();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		System.out.println(" inside doGET ***************");
		doPost(request, response);
	}

		public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			//Get user from session to identify the user who updates CustomerBank info
			HttpSession session = request.getSession(false);
			String userName = (String) session.getAttribute("User");

			//TODO: Delme
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			LogUtil.log(LogUtil.Message_Type.Information, " Inside Customer bank info request ");
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
				
				//TODO: Delme
				System.out.println("Add from edit servlet Bank : " + inCustBank
						+ " Branch : " + inCustBranch + " Account Name : "
						+ inCustAccountName + " Account Type : "
						+ inCustAccountType + " Account Number : "
						+ inCustAccountNumber);
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

				//TODO: Delme
				System.out.println("Update from edit servlet Bank : "
						+ inCustBank + " Branch : " + inCustBranch
						+ " Account Name : " + inCustAccountName
						+ " Account Type : " + inCustAccountType
						+ " Account Number : " + inCustAccountNumber
						+ " Cust Bank Id : " + inCustBankId + " Cust Code : "
						+ inCustCode);
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
				System.out.println (" return code from crud update is " + returnCode);
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

				//TODO: Delme
				System.out.println("Delete from edit servlet Bank : "
						+ inCustBank + " Branch : " + inCustBranch
						+ " Account Name : " + inCustAccountName
						+ " Account Type : " + inCustAccountType
						+ " Account Number : " + inCustAccountNumber
						+ " Cust Bank Id : " + inCustBankId + " Cust Code : "
						+ inCustCode);
				
				CustomerBanks customerBank = new CustomerBanks();
				CustomerBanksId customerBankId = new CustomerBanksId();

				customerBankId.setCustCode(Util.convertToInt(inCustCode));
				customerBankId.setCustBankId(Util.convertToInt(inCustBankId));
				customerBank.setId(customerBankId);

				CustomerBankInfoCRUDProvider customerBankInfoCrud = new CustomerBankInfoCRUDProvider();
				returnCode = customerBankInfoCrud.deleteCustomerBank(customerBank);
			}

			session.setAttribute("customerBankInfoId", dispKey);
			if ("customerBankInfoUpdate".equals(action)) {
				System.out.println(" inside Customer Bankinfoservlet - Customer Bank info update "
								+ dispKey);
				response.sendRedirect("editCustomerBankInfo.jsp");
			} // used

			else if ("customerBankInfoAdd".equals(action)) {
				System.out.println(" inside Customer Bankinfoservlet - Customer Bank info add "
								+ dispKey);
				response.sendRedirect("editCustomerBankInfo.jsp");
			} // used

			else {
				System.out.println(" outsid else ");
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