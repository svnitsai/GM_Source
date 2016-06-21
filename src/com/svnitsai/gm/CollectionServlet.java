package com.svnitsai.gm;

import java.io.IOException;
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
		else if("saveCollectionFromList".equals(action))
		{
			handleSaveCollection(request, response, true);
		}
		else if("saveCollection".equals(action))
		{
			handleSaveCollection(request, response, false);
		}
	}
	
	private void handleGetCollection(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String selectedDate = request.getParameter("selectedDate");
		String selectedCustGroup = request.getParameter("selectedCustGroup");
		String selectedMerchantId = request.getParameter("merchantId");
		String selectedMerchantName = request.getParameter("merchantName");
		String selectedAgentCode = request.getParameter("agentId");
		String selectedAgentName = request.getParameter("agentName");
		String filterBy = request.getParameter("filterBy");
		if("merchant".equals(filterBy))
		{
			selectedDate = "";
			selectedCustGroup = "All";
			selectedAgentCode = "";
		}
		else if("agent".equals(filterBy)) 
		{
			selectedDate = "";
			selectedCustGroup = "All";
			selectedMerchantId = "";
		}
		else 
		{
			selectedMerchantId = "";
			selectedAgentCode = "";
		}
		
		Collection<CollectionBean> bean = DBHandler.getCollections(selectedDate, selectedCustGroup, selectedMerchantId, null, selectedAgentCode, false);
		request.setAttribute("collectionData", bean);
		request.setAttribute("selectedDate", selectedDate);
		request.setAttribute("selectedCustGroup", selectedCustGroup);
		request.setAttribute("merchantId", selectedMerchantId);
		request.setAttribute("merchantName", selectedMerchantName);
		request.setAttribute("agentId", selectedAgentCode);
		request.setAttribute("agentName", selectedAgentName);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/web/collectionDisplay.jsp");
		dispatcher.forward(request, response);
	}

	private void handleSaveCollection(HttpServletRequest request, HttpServletResponse response, boolean redirect) throws ServletException, IOException 
	{
		CollectionBean bean = new CollectionBean();
		
		// iterate through parameters to get the supplier data
		Enumeration<String> paramNames = request.getParameterNames();
		boolean isAgentCollection = false;
		while(paramNames.hasMoreElements())
		{
			String name = paramNames.nextElement();
			
			if(name.startsWith("collectionRefId"))
			{
				bean.setCollectionId( Util.convertToLong(request.getParameter(name)));
			}
			else if(name.startsWith("deferredDate"))
			{
				bean.setDeferredDateStr(request.getParameter(name));
			}
			else if(name.startsWith("deferredReasonChoice"))
			{
				bean.setDeferredReasonChoiceStr(request.getParameter(name));
			}
			else if(name.startsWith("deferredReason"))
			{
				bean.setDeferredReason(request.getParameter(name));
			}
			else if(name.startsWith("invoiceAmt"))
			{
				bean.setInvoiceAmount(Util.convertToDouble(request.getParameter(name)));
			}
			else if(name.startsWith("agentId"))
			{
				bean.setAgentCode(Util.convertToLong(request.getParameter(name)));
			}
			else if(name.startsWith("agentName"))
			{
				isAgentCollection = true;
				bean.setAgentName(request.getParameter(name));
			}
			else if(name.startsWith("formNumber"))
			{
				bean.setFormNumber(request.getParameter(name));
			}
			else if(name.startsWith("colLedgerNo"))
			{
				bean.setLedgerNumber(request.getParameter(name));
			}
			else if(name.startsWith("remarks"))
			{
				bean.setRemarks(request.getParameter(name));
			}
			else if(name.startsWith("status"))
			{
				String statusStr = request.getParameter(name);
				if(statusStr.equalsIgnoreCase("CLOSED"))
				{
					bean.setStatus(statusStr);
				}
			}
			else if(name.startsWith("detailRefID_") && !name.equals("detailRefID_0"))
			{
				// check if this is an existing payment (where detail ID is set) 
				String detailId = request.getParameter(name);
				
				System.out.println("Reading details for " + name);
				String index = name.replace("detailRefID", "");
				CollectionDetailBean detailBean = new CollectionDetailBean();
				detailBean.setCollectionDetailId( Util.convertToLong(request.getParameter(name)));
				
				String paidAmt = request.getParameter("paidAmt" + index);
				if(paidAmt != null)
				{
					paidAmt = paidAmt.replace(",", "");
					paidAmt = paidAmt.replace(" ", "");
				}
				else
				{
					paidAmt = "0";
				}
				detailBean.setPaidAmount(Util.convertToDouble(paidAmt.trim()));
				detailBean.setPaymentRemarks( request.getParameter("comments" + index));
				detailBean.setCompanyCode( Util.convertToLong(request.getParameter("companyID" + index)));
				detailBean.setLedgerNumber(Util.convertToInt(request.getParameter("ledger" + index)));
				if("0".equals(detailId))
				{
					// detailId =0 ==> New payment
					String paymentType = request.getParameter("paymentType" + index);
					detailBean.setSupplierCode( Util.convertToLong(request.getParameter("supplierId" + index)));
					if(detailBean.getSupplierCode() == 0)
					{
						// supplier not selected. It must be the other payment types. Get the type
						// cash(0), Adjustment (-1) or RG (-2)
						detailBean.setSupplierCode( Util.convertToLong(request.getParameter("paymentType" + index)));
					}
					detailBean.setSupplierBankId(Util.convertToLong(request.getParameter("supplierBankId" + index)));
					//detailBean.setCustomerBankName(request.getParameter("merchantBank" + index));
					detailBean.setCollectionDateStr(request.getParameter("collectionDate" + index));
				}
				bean.getDetailsList().add(detailBean);
			}
			
		}
		
		bean.updateDeferredReason();
		
		if(isAgentCollection && !redirect)
		{
			DBHandler.updateAgentPayments(bean);
		}
		else
		{
			DBHandler.updateCollectionInfo(bean);
		}
		
		
		if(redirect)
		{
			handleGetCollection(request, response);
		}
		else
		{
			request.setAttribute("action", "save");
			String responseStr = "<html><head><script>window.close();</script></head><body>Done</body></html>";
			response.setContentLength(responseStr.length());
		    //And write the string to output.
			response.getOutputStream().write(responseStr.getBytes());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
}
