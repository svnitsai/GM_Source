package com.svnitsai.gm.database.provider;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.svnitsai.gm.util.date.DateUtil;
import com.svnitsai.gm.util.display.DisplayUtil;
import com.svnitsai.gm.util.exception.DBException;
import com.svnitsai.gm.util.hibernate.HibernateUtil;
import com.svnitsai.gm.util.log.LogUtil;

public class CustomerInfoProvider {
	
	public Object getCustomerInfoList (String customerType) {

		Session session = HibernateUtil.getSession();
		List result = null;

		try {
			String SQL_QUERY = " select c.custId as CustId, c.custCode as CustCode, ISNULL(c.custName,'') as CustName, ISNULL(c.custAddress1,'') as CustAddress1" 
					+ ", ISNULL(c.custAddress2,'') as CustAddress2, ISNULL(c.custAddress3,'') as CustAddress3, "
							+ " ISNULL(c.custAddress4,'') as CustAddress4, ISNULL(c.custCity,'') as CustCity, c.custState as CustState, c.custCountry as CustCountry " +
					", c.custContactNumber as CustContactNumber, c.custExtension as CustExtension, c.custType as CustType" +
					" from Customer c" +
					" where upper(c.custType) = upper(:hostCustType) order by c.custName asc";
			/* Run as native SQL query*/
			Query query = session.createSQLQuery(SQL_QUERY).setParameter("hostCustType", customerType);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			result = query.list();
			if (result.isEmpty()) {
				LogUtil.log(LogUtil.Message_Type.Information,
						" No records to display on customerInfoProvider; for custType: " + customerType  + "   @ "
								+ DateUtil.getCurrentTimestamp().toString());
				result = null;
			} else {
				LogUtil.log(LogUtil.Message_Type.Information, " Total "
						+ result.size()
						+ " record(s) to display on customerInfoProvider; for custType: " + customerType  + "   @ "
						+ DateUtil.getCurrentTimestamp().toString());
//				for (Object object : result) {
//					Map row = (Map) object;
//					LogUtil.log(
//							LogUtil.Message_Type.Information,
//							" custId: " + row.get("CustId")
//									+ ", CustCode: "
//									+ row.get("CustCode")
//									+ ", CustName: " + row.get("CustName"));
//				}
			}
		} catch (JDBCException e) {
			DBException.HandleJDBCException(e);
		} catch (HibernateException e) {
			DBException.HandleHibernateException(e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			
			session.close();
			return result;
		}
	}
	
//	public static void main(String[] args) {
//		
//		  System.out.println(" Get customer info - main. "); 
//		  CustomerInfoProvider  customerInfoProvider = new CustomerInfoProvider();
//		  List result = (List) customerInfoProvider.getCustomerInfoList("merchant");
//		  
//		  if ((result != null) && (result.size() > 0)) { 
//			  for (Object object : result) { 
//				  Map row = (Map) object; System.out.println(" ");
//				  System.out.println(" CustName: " + row.get("CustName")); 
//				  System.out.println ("   Address is " + DisplayUtil.getAddress(row.get("CustAddress1").toString()
//						  , row.get("CustAddress2").toString(), row.get("CustAddress3").toString(), row.get("CustAddress4").toString()));
//				  System.out.println ("   City is " + DisplayUtil.getCity(row.get("CustCity").toString()));
//				  } 
//			  }
//
//	
//		  result = (List) customerInfoProvider.getCustomerInfoList("supplier");
//		  
//		  if ((result != null) && (result.size() > 0)) { 
//			  for (Object object : result) { 
//				  Map row = (Map) object; System.out.println(" ");
//				  System.out.println(" CustName: " + row.get("CustName")); 
//				  System.out.println (" Address is " + DisplayUtil.getAddress(row.get("CustAddress1").toString()
//						  , row.get("CustAddress2").toString(), row.get("CustAddress3").toString(), row.get("CustAddress4").toString()));
//				  System.out.println ("   City is " + DisplayUtil.getCity(row.get("CustCity").toString()));
//				  } 
//		  }
//}
	
}
