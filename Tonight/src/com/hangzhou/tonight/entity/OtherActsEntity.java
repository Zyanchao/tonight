package com.hangzhou.tonight.entity;

/**
* @ClassName: AcitvesEntity 
* @Description: TODO(活动实体类) 
* @author yanchao 
* @date 2015-9-1 下午11:07:26 
*
 */
public class OtherActsEntity extends Entity {

	private String nums;
	public String getNums() {
		return nums;
	}


	public void setNums(String nums) {
		this.nums = nums;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	private String title;
	private String des;
	private String time;
	private String imgHeader;
	
	
	public OtherActsEntity(String title,String nums,
			String des, String time, String imgHeader) {
		this.title = title;
		this.nums = nums;
		this.des = des;
		this.time = time;
		this.imgHeader = imgHeader;
	}


	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImgHeader() {
		return imgHeader;
	}

	public void setImgHeader(String imgHeader) {
		this.imgHeader = imgHeader;
	}


	public OtherActsEntity(){
		
	}
	
	
}
