package com.jm.sort;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jm.entity.Hair;
import com.jm.fxw.R;
import com.jm.util.LogUtil;

public class MerchantGalleryAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Hair> list;
	private Bitmap bitmap;

	public MerchantGalleryAdapter(Context context, Bitmap bitmap) {
		this.bitmap = bitmap;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Hair>();
	}

	public void setList(ArrayList<Hair> plist) {
		if (list == null) {
			list = new ArrayList<Hair>();
		}
		list = plist;
	}

	public void clearList() {
		list.clear();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView != null && convertView instanceof MerchantGalleryItem) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = (MerchantGalleryItem) inflater.inflate(
					R.layout.biggallery, null);
			holder = new ViewHolder();
			((MerchantGalleryItem) convertView).initView(holder);
			convertView.setTag(holder);
		}
		((MerchantGalleryItem) convertView).setPosition(position);
		((MerchantGalleryItem) convertView).setUrl(list.get(position).getPic());
		LogUtil.e("bitmap) = " + bitmap);
		((MerchantGalleryItem) convertView).fillView(holder, bitmap);
		return convertView;

	}
}
