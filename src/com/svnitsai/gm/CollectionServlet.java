package com.svnitsai.gm;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet ("/servlet/collection")
public class CollectionServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		String action = request.getParameter("action");
		if("getCollections".equals(action))
		{
			handleGetCollection(request, response);
		}
		else if("saveCollection".equals(action))
		{
			System.out.println("Saving collection....");
			handleGetCollection(request, response);
		}
	}

	private void handleGetCollection(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String filterBy = request.getParameter("filterBy");
		String selectedDate = request.getParameter("selectedDate");
		String selectedMerchantId = request.getParameter("merchantId");
		String selectedMerchantName = request.getParameter("merchantName");
		
		LinkedList<CollectionBean> bean = readCollectionInfo(	request, 
																filterBy, 
																selectedDate,
																selectedMerchantId);
		request.setAttribute("collectionData", bean);
		
		if("date".equals(filterBy))
		{
			request.setAttribute("displayStr", "Collection Details for " + selectedDate);
		}
		else 
		{
			request.setAttribute("displayStr", "Collection Details for " + selectedMerchantName);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/collectionDisplay.jsp");
		dispatcher.forward(request, response);
	}
	
	private LinkedList<CollectionBean> readCollectionInfo(HttpServletRequest request,
															String filterBy,
															String selectedDate,
															String selectedMerchantId) 
	{
		// TODO: Replace with DB call to get actual data

		LinkedList<CollectionBean> beanList = new LinkedList<CollectionBean>();
		
		// Entry 1
		CollectionBean bean1 = DBHandler.getCollectionInfo("1");
		beanList.add(bean1);
		
		// Entry 2
		CollectionBean bean2 = DBHandler.getCollectionInfo("2");
		beanList.add(bean2);
		
		// Entry 3
		CollectionBean bean3 = DBHandler.getCollectionInfo("3");
		beanList.add(bean3);
		
		
		return beanList;
	}
}
