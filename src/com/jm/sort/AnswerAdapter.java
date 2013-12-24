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

import com.jm.entity.Answer;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AnswerAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Answer> mlist;
	private Context context;
	private boolean isProgress = false;

	public AnswerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Answer>();
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
	public void addMoreData(Answer type) {

		mlist.add(type);
	}

	public void setTypeList(List<Answer> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<Answer> getUserList() {
		return mlist;

	}

	public void appendAnswerList(List<Answer> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Answer>();
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
		Answer type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.answer_list, null);
		}

		ImageLoader.getInstance().displayImage(type.head_photo,
				(ImageView) view.findViewById(R.id.iv_pic));
		((TextView) view.findViewById(R.id.tv_ta_id)).setText(type.ta_id);
		((TextView) view.findViewById(R.id.tv_tid)).setText(type.pid);
		((TextView) view.findViewById(R.id.tv_1_1)).setText(type.ta_name);
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
