package com.hangzhou.tonight.entity;

/**
* @ClassName: AcitvesEntity 
* @Description: TODO(活动实体类) 
* @author yanchao 
* @date 2015-9-1 下午11:07:26 
*
 */
public class OtherActsEntity extends Entity {

	
private String title;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String[] getImg() {
	return img;
}
public void setImg(String[] img) {
	this.img = img;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
}
public String getSales_num() {
	return sales_num;
}
public void setSales_num(String sales_num) {
	this.sales_num = sales_num;
}
private String[] img;
private String value;
private String price;
private String sales_num;




	
	
}
