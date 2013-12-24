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

import com.jm.entity.Comment;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Comment> mlist;

	private Context context;
	private Comment comment;

	public CommentAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Comment>();
		this.context = context;
	}

	/**
	 * 添加数据列表
	 * 
	 * @param goods
	 */
	public void addMoreData(Comment type) {

		mlist.add(type);
	}

	public List<Comment> getList() {
		return mlist;
	}

	public void setTypeList(List<Comment> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		comment = mlist.get(position);
		if (convertView != null && convertView instanceof CommentItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.comment_list, null);
		}
		((TextView) view.findViewById(R.id.tv_utime)).setText(comment
				.getCtime());
		((TextView) view.findViewById(R.id.tv_utext)).setText(comment
				.getCtext());
		((TextView) view.findViewById(R.id.tv_uname)).setText(comment
				.getCname());
		ImageLoader.getInstance().displayImage(comment.getCpic(),
				(ImageView) view.findViewById(R.id.iv_commentupic));
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
			notifyDataSetChanged();
		}
	}
}
