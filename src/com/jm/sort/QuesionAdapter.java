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

import com.jm.entity.Question;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuesionAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Question> mlist;
	private Context context;
	private boolean isProgress = false;

	public QuesionAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Question>();
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
	public void addMoreData(Question type) {

		mlist.add(type);
	}

	public void setTypeList(List<Question> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<Question> getUserList() {
		return mlist;

	}

	public void appendQuestionList(List<Question> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Question>();
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
		Question type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.question_list, null);
		}
		ImageLoader.getInstance().displayImage(type.pic,
				(ImageView) view.findViewById(R.id.iv_pic));
		((TextView) view.findViewById(R.id.tv_tid)).setText(type.id);
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
