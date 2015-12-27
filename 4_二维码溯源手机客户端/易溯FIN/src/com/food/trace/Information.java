package com.food.trace;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Information extends BmobObject{
	
	String conductName;
	String conductType;
	String procedureName;
	String place;
	String operator;
	BmobDate operateTime;
	String enterprise;
	String writerId;
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	public String getConductType(){
		return conductType;
	}
	public String getConductName(){
		return conductName;
	}
	public String getProcedureName(){
		return procedureName;
	}
	public String getPlace(){
		return place;
	}
	public String getOperator(){
		return operator;
	}
	public BmobDate getOperateTime(){
		return operateTime;
	}
	public String getEnterprise(){
		return enterprise;
	}
	public void setConductType(String conductType){
		this.conductType = conductType;
	}
	public void setConductName(String conductName) {
		this.conductName = conductName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public void setOperateTime(BmobDate operateTime) {
		this.operateTime = operateTime;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	

}
