package com.mgoogle.lee.kr.com;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class KrItem implements Parcelable {
	String bm=null;
	Bitmap bitMap=null;
	public Bitmap getBitMap() {
		return bitMap;
	}
	public void setBitMap(Bitmap bitMap) {
		this.bitMap = bitMap;
	}
	String title=null;
	String file_out=null;
	boolean img_loaded=false;

	public boolean isImg_loaded() {
		return img_loaded;
	}
	public void setImg_loaded(boolean img_loaded) {
		this.img_loaded = img_loaded;
	}
	public String getFile_out() {
		return file_out;
	}
	public void setFile_out(String file_out) {
		this.file_out = file_out;
	}
	public KrItem(String title, String brief, String url, String bm) {
		super();
		this.title = title;
		this.brief = brief;
		this.url = url;
		this.bm = bm;
	}
	public KrItem(String title, String brief, String url, String bm,String file_out) {
		super();
		this.title = title;
		this.brief = brief;
		this.url = url;
		this.bm = bm;
		setFile_out(file_out);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	String brief=null;
	String url=null;
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
