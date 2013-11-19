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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.Msg;
import com.jm.fxw.R;

public class MsgAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Msg> mlist;

	private Context context;

	private int position;
	private Button btn;
	private FinalBitmap fb;

	private ImageView hairPic;

	public MsgAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Msg>();
		this.context = context;
	}

	/**
	 * 添加数据列表
	 * 
	 * @param goods
	 */
	public void addMoreData(Msg type) {

		mlist.add(type);
	}

	public void setTypeList(List<Msg> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	public void appendTypeList(List<Msg> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Msg>();
		}
		mlist.addAll(list);
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
		this.position = position;
		Msg type = mlist.get(position);
		if (convertView != null && convertView instanceof MsgItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.msg_list, null);
		}
		((TextView) view.findViewById(R.id.tv_utime)).setText(type.getTime());
		((TextView) view.findViewById(R.id.tv_utext))
				.setText(type.getContent());
		((TextView) view.findViewById(R.id.tv_uname)).setText(type
				.getUser_name());
		((TextView) view.findViewById(R.id.tv_tid)).setText(type.getUid());

		fb = FinalBitmap.create(context);
		fb.display((ImageView) view.findViewById(R.id.iv_msgupic),
				type.getUser_pic());
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
