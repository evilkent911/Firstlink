package com.trinerva.icrm.model;

import android.app.Application;

public class MyApplication extends Application{
	
	String companyId;
	
	public void setCompanyId(String companyId){
		
		this.companyId = companyId;
		
	}

	public String getCompanyId(){
		return companyId;
	}
}
