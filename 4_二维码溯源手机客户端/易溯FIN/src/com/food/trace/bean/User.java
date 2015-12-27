package com.food.trace.bean;

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
	public void setFactory_id(String factory){
		this. factory = factory ;
	}
}
