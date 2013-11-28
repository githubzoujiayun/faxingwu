package com.jm.sort;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.jm.util.LogUtil;

public class CacheBitmap {
	
	private Map<Integer, Bitmap> map;
	private int listSize;
	
	public CacheBitmap(){
		map = new HashMap<Integer, Bitmap>();
	}
	
	public int getMapSize(){
		if(map != null){
			return map.size();
		}else{
			return 0;
		}
	}
	
	public void setListSize(int listSize){
		this.listSize = listSize;
	}
	
	public void addBitmap(int position,Bitmap bitmap){
		map.put(position, bitmap);
	}
	
	public Bitmap getBitmap(int position){
		if(map == null){
			return null;
		}else{
			return map.get(position);
		}
	}
	
	public void clearBitmap(int startPosition,int endPosition){
		if(map == null){
			return;
		}
		LogUtil.d("startPosition--->"+startPosition+"endPosition"+endPosition);
		while(startPosition<endPosition){
			recyceleBitmap(startPosition);
			startPosition++;
		}
	}
	
	public void recyceleBitmap(int position){
		if(map == null){
			return;
		}
		Bitmap bitmap = map.get(position);
		if(bitmap != null && !bitmap.isRecycled()){
			LogUtil.d("recyceleBitmap--->"+position);
			bitmap.recycle();
		}
	}
	
	public void recycleOutSides(int positionStart,int positionEnd){
		LogUtil.d("positionStart---->"+positionStart+"positionEnd--->"+positionEnd);
		if(map == null){
			return;
		}
		clearBitmap(0,positionStart);
		clearBitmap(positionEnd,listSize);
	}
}
