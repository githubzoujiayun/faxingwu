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

import com.jm.entity.User;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserGalleryAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<User> mlist;

	private Context context;

	private boolean isProgress = false;

	public UserGalleryAdapter(Context context) {
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
				view = inflater.inflate(R.layout.hair_list, null);
			}
			ImageLoader.getInstance().displayImage(type.getHead_photo(),
					(ImageView) view.findViewById(R.id.img_hair));
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
