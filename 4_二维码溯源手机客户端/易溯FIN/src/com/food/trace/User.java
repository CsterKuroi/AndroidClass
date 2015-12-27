package com.food.trace;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser{
	String type;
	String factory;
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getFactory(){
		return factory;
	}
	public void setFactory(String factory){
		this. factory = factory ;
	}
	
}
