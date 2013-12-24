package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.Job;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JobAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Job> mlist;
	private Context context;
	private boolean isProgress = false;

	public JobAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Job>();
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

	public List<Job> getUserList() {
		return mlist;

	}

	public void appendJobList(List<Job> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Job>();
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
		Job type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.job_list, null);
		}
		ImageLoader.getInstance().displayImage(type.head_photo,
				(ImageView) view.findViewById(R.id.iv_pic));
		((TextView) view.findViewById(R.id.tv_1_1)).setText(type.job);
		((TextView) view.findViewById(R.id.tv_2_1)).setText("薪资:" + type.money);
		if (type.interviews.equals("1")) {
			((TextView) view.findViewById(R.id.tv_2_1)).setText("薪资:" + "面谈");
		}
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
