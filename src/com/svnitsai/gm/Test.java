package com.svnitsai.gm;

import com.svnitsai.gm.database.generated.UserAccess;
import com.svnitsai.gm.database.provider.UserAccessProviderImpl;
import com.svnitsai.gm.util.date.*;

public class Test {

	public static void main(String[] args) {
	
		UserAccessProviderImpl userAPI = new UserAccessProviderImpl();
		
		/*
		UserAccess userAccessData = new UserAccess();
		//userAccessData.setUserId(1);
		userAccessData.setUserName("Admin");
		userAccessData.setPassword("admin");
		userAccessData.setRole("admin");
		userAccessData.setCreatedBy("SVN_Admin");
		userAccessData.setUpdatedBy("SVN_Admin");
		userAccessData.setCreatedDate(DateUtil.getCurrentTimestamp());
		userAccessData.setUpdatedDate(DateUtil.getCurrentTimestamp());
		userAPI.Create(userAccessData);
		*/
		
		UserAccess userAccess= (UserAccess) userAPI.ReadById(0);
		if (userAccess != null) System.out.println ("The user value is " + userAccess.getUserName());
		else System.out.println("User not found");

		userAccess= (UserAccess) userAPI.ReadById(1);
		if (userAccess != null) System.out.println ("The user value is " + userAccess.getUserName());
		else System.out.println("User not found");

		userAccess= (UserAccess) userAPI.ReadByUsername("admin");
		if (userAccess != null) System.out.println ("The user value is " + userAccess.getUserName() + " / " + userAccess.getPassword());
		else System.out.println("User not found");

		userAccess= (UserAccess) userAPI.ReadByUsername("Admin");
		if (userAccess != null) System.out.println ("The user value is " + userAccess.getUserName());
		else System.out.println("User not found");

	}

}
