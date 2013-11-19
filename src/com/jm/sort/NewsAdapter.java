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

import com.jm.entity.News;
import com.jm.fxw.R;

public class NewsAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<News> mlist;
	private Context context;
	private boolean isProgress = false;

	public NewsAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<News>();
		this.context = context;
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setProgress(boolean isProgress) {
		this.isProgress = isProgress;
	}

	/**
	 * 添加数据列表
	 * 
	 * @param goods
	 */
	public void addMoreData(News type) {

		mlist.add(type);
	}

	public void setTypeList(List<News> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<News> getUserList() {
		return mlist;

	}

	public void appendNewsList(List<News> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<News>();
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
		News type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.news_list, null);
		}
		FinalBitmap.create(context).display(
				(ImageView) view.findViewById(R.id.iv_pic), type.pic);
		if ("".equals(type.title)) {
			view.findViewById(R.id.lin_1).setVisibility(View.GONE);
		} else {
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type.title);
		}

		((TextView) view.findViewById(R.id.tv_3_1)).setText(type.add_time);
		((TextView) view.findViewById(R.id.tv_2_1)).setText(type.content);
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
