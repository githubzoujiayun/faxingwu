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
	private String WhosHair;

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

	public void setImageList(List<Hair> list, String WhosId) {
		if (list == null) {
			return;
		}
		mlist = list;
		this.WhosHair = WhosId;

	}

	@Override
	public int getCount() {

		return mlist.size();
	}

	@Override
	public Hair getItem(int position) {

		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View contentView, ViewGroup arg2) {
		SmallImageItem view = (SmallImageItem) inflater.inflate(
				R.layout.smallimg_grid, null);
		view.WhosHair = WhosHair;
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
