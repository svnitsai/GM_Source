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
		if("save".equals(action))
		{
			handleSave(request, response);
		}
	}

	private void handleSave(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		ArrayList<DailyPayableBean> payableList = new ArrayList<DailyPayableBean>();
		
		// iterate through parameters to get the supplier data
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements())
		{
			String name = paramNames.nextElement();
			if(name.startsWith("supplier_") && !name.equals("supplier_0"))
			{
				String supplierId = request.getParameter(name);
				DailyPayableBean bean = new DailyPayableBean();
				bean.setSupplierCode(Util.convertToLong(supplierId));
				
				String index = name.replace("supplier", "");
				bean.setPayableDateStr(request.getParameter("date" + index));
				bean.setPayableAmount(Util.convertToDouble(request.getParameter("amount" + index)));
				bean.setInstructions(request.getParameter("instructions" + index));
				payableList.add(bean);
			}
		}
		
		String statusStr = "";
		if(payableList.size() > 0)
		{
			// save the entries
			DBHandler.savePayable(payableList); 
			statusStr = "Changes Saved.";
		}
		else
		{
			statusStr = "No suppliers to save.";
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/suppliers.jsp?status=" + statusStr);
		dispatcher.forward(request, response);
	}
	
}
