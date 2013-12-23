package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.Hair;
import com.jm.entity.Type;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HairTypeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Type> mlist;
	private Context context;

	private ImageView hairPic;
	private boolean isProgress = false;

	public HairTypeAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Type>();
		this.context = context;
	}

	/**
	 * ��������б�
	 * 
	 * @param goods
	 */
	public void addMoreData(Type type) {

		mlist.add(type);
	}

	public List<Type> getHairList() {
		return mlist;
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setProgress(boolean isProgress) {
		this.isProgress = isProgress;
	}

	public void appendHairList(List<Type> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Type>();
		}

		mlist.addAll(list);

	}

	public void setHairList(List<Type> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	@Override
	public int getCount() {
		if (isProgress) {
			return 1;
		} else {
			return mlist.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (isProgress) {
			view = inflater.inflate(R.layout.progress_footer, null);

			return view;
		} else {
			Type type = mlist.get(position);
			if (convertView != null && convertView instanceof HairItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.hairtype_list, null);
			}
			view.findViewById(R.id.img_hairtype).setBackgroundResource(
					type.picResource);
			((TextView) view.findViewById(R.id.tv_hairtype))
					.setText(type.hairName);

		}
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
		notifyDataSetChanged();
	}
}
