package com.moma.bean;

import java.io.Serializable;

public class Feature  implements Serializable{

	private static final long serialVersionUID = 1L;
	private String no;
	private String feature;
	private int update;
	private int download;
	private String lcoal;
	private int isdelete;

	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	public int getDownload() {
		return download;
	}
	public void setDownload(int download) {
		this.download = download;
	}
	public String getLcoal() {
		return lcoal;
	}
	public void setLcoal(String lcoal) {
		this.lcoal = lcoal;
	}
	public Feature(String no, String feature, String lcoal) {
		super();
		this.no = no;
		this.feature = feature;
		this.lcoal = lcoal;
	}
	public Feature () {

	}

}
