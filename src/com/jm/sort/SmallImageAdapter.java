package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jm.entity.Hair;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SmallImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Hair> mlist;

	private Context context;


	public SmallImageAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Hair>();
		this.context = context;
	}

	public void setImageList(List<Hair> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	@Override
	public int getCount() {

		return mlist.size();
	}

	@Override
	public Object getItem(int position) {

		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View contentView, ViewGroup arg2) {
		SmallImageItem view = (SmallImageItem) inflater.inflate(
				R.layout.smallimg_grid, null);
		
		ImageLoader.getInstance().displayImage(mlist.get(position).getPic(),
				(ImageView) view.findViewById(R.id.iv_HairShow));
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();

		}
	}

}
