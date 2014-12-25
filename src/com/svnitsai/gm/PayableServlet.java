package com.svnitsai.gm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet ("/servlet/payable")
public class PayableServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		String action = request.getParameter("action");
		if( "save".equals(action) || 
			"print".equals(action))
		{
			handleSave(request, response, action);
		}
		
	}

	private void handleSave(HttpServletRequest request, HttpServletResponse response, String action) 
			throws ServletException, IOException 
	{
		ArrayList<DailyPayableBean> payableList = new ArrayList<DailyPayableBean>();
		
		// iterate through parameters to get the supplier data
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements())
		{
			String name = paramNames.nextElement();
			// The supplier parameter is sent only if supplier is selected.
			// hence the last row added with no data is excluded
			if(name.startsWith("supplier_") && !name.equals("supplier_0"))
			{
				String supplierId = request.getParameter(name);
				DailyPayableBean bean = new DailyPayableBean();
				bean.setSupplierCode(Util.convertToLong(supplierId));
				
				String index = name.replace("supplier", "");
				bean.setPayableDateStr(request.getParameter("date" + index));
				
				String paidAmt = request.getParameter("amount" + index);
				if(paidAmt != null)
				{
					paidAmt = paidAmt.replace(",", "");
					paidAmt = paidAmt.replace(" ", "");
				}
				else
				{
					paidAmt = "0";
				}
				bean.setPayableAmount(Util.convertToDouble(paidAmt.trim()));
				
				bean.setInstructions(request.getParameter("instructions" + index));
				bean.setPayableId(Util.convertToLong(request.getParameter("refId" + index)));
				payableList.add(bean);
			}
		}
		
		DBHandler.savePayable(payableList);
		String statusStr ="Changes Saved.";
		
		if("save".equals(action))
		{
			RequestDispatcher dispatcher = request.getRequestDispatcher("/web/suppliers.jsp?status=" + statusStr);
			dispatcher.forward(request, response);
		}
		else if("print".equals(action))
		{
			RequestDispatcher dispatcher = request.getRequestDispatcher("/servlet/report?type=PayableMorning&date=today");
			dispatcher.forward(request, response);
		}
	}
	
}
