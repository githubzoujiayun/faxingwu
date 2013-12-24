package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jm.entity.Type;
import com.jm.fxw.R;

public class HairTypeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Type> mlist;
	private boolean isProgress = false;

	public HairTypeAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Type>();
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
	public Type getItem(int position) {
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
