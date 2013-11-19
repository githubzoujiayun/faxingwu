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

import com.jm.entity.User;
import com.jm.fxw.R;

public class UserAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<User> mlist;

	private Context context;
	private FinalBitmap fb;
	private boolean isProgress = false;

	public UserAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<User>();
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
	public void addMoreData(User type) {

		mlist.add(type);
	}

	public void setTypeList(List<User> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<User> getUserList() {
		return mlist;

	}

	public void appendUserList(List<User> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<User>();
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
		User type = mlist.get(position);
		if (isProgress) {
			view = inflater.inflate(R.layout.progress_footer, null);
			return view;
		} else {
			if (convertView != null && convertView instanceof UserItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.user_list, null);
			}

			fb = FinalBitmap.create(context);
			fb.display((ImageView) view.findViewById(R.id.iv_pic),
					type.getHead_photo());

			// else {
			// hairPic.setImageResource(R.drawable.moren_touxiang);
			// }

			((TextView) view.findViewById(R.id.tv_1_1)).setText(type
					.getUsername());
			((TextView) view.findViewById(R.id.tv_2_1)).setText(type
					.getStore_name());
			((TextView) view.findViewById(R.id.tv_3_1)).setText(type.getCity()
					+ type.getAddress() + type.getDistance());
			return view;
		}
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
