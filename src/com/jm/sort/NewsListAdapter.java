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

import com.jm.entity.NewsList;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<NewsList> mlist;
	private NewsList type;
	private Context context;

	public NewsListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<NewsList>();
		this.context = context;
	}

	public void setNewsListList(List<NewsList> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	public List<NewsList> getList() {
		return mlist;
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
		NewsListItem view = (NewsListItem) inflater.inflate(
				R.layout.newsinfolist_list, null);
		type = mlist.get(position);

		ImageLoader.getInstance().displayImage(type.getImage(),
				(ImageView) view.findViewById(R.id.iv_NewListInfo));
		((TextView) view.findViewById(R.id.tv_NewListInfo)).setText("      "
				+ type.getContent());
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
	}

}
