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

public class HairAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Hair> mlist;

	private ArrayList<Hair> hlist;
	private Context context;

	private ImageView hairPic;
	private boolean isProgress = false;

	public HairAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Hair>();
		hlist = new ArrayList<Hair>();
		this.context = context;
	}

	/**
	 * 添加数据列表
	 * 
	 * @param goods
	 */
	public void addMoreData(Hair hair) {

		mlist.add(hair);
	}

	public ArrayList<Hair> getList() {
		return hlist;
	}

	public List<Hair> getHairList() {
		return mlist;
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setProgress(boolean isProgress) {
		this.isProgress = isProgress;
	}

	public void appendHList(List<Hair> list) {
		if (list == null) {
			return;
		}
		if (hlist == null) {
			hlist = new ArrayList<Hair>();
		}
		hlist.addAll(list);
	}

	public void appendHairList(List<Hair> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Hair>();
		}

		mlist.addAll(list);

	}

	public void setHairList(List<Hair> list) {
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
			Hair hair = mlist.get(position);
			if (convertView != null && convertView instanceof HairItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.hair_list, null);
			}
			ImageLoader.getInstance().displayImage(hair.getPic(),
					(ImageView) view.findViewById(R.id.img_hair));

		}
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
		if (hlist != null) {
			hlist.clear();
		}
		notifyDataSetChanged();
	}
}
