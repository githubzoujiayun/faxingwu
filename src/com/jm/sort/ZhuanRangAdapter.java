package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.ZhuanRang;
import com.jm.fxw.R;

public class ZhuanRangAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<ZhuanRang> mlist;
	private Context context;
	private boolean isProgress = false;

	public ZhuanRangAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<ZhuanRang>();
		this.context = context;
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setProgress(boolean isProgress) {
		this.isProgress = isProgress;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<ZhuanRang> getUserList() {
		return mlist;

	}

	public void appendZhuanRangList(List<ZhuanRang> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<ZhuanRang>();
		}

		mlist.addAll(list);

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
		ZhuanRang type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.zhuanrang_list, null);
		}
		FinalBitmap.create(context).display(
				(ImageView) view.findViewById(R.id.iv_pic), type.head_photo);
		((TextView) view.findViewById(R.id.tv_1_1)).setText(type.address);
		((TextView) view.findViewById(R.id.tv_2_1)).setText("风格:" + type.style
				+ "  面积:" + type.acreage);
		((TextView) view.findViewById(R.id.tv_3_1)).setText(type.city);
		((TextView) view.findViewById(R.id.tv_3_2)).setText(type.add_time);
		return view;

	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
			notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {

	}
}
