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
import android.widget.RatingBar;
import android.widget.TextView;

import com.jm.entity.Rating;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserRatingAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Rating> mlist;

	private Context context;

	private int position;
	private boolean isProgress = false;

	public UserRatingAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Rating>();
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
	public void addMoreData(Rating type) {

		mlist.add(type);
	}

	public void setTypeList(List<Rating> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<Rating> getRatingList() {
		return mlist;

	}

	public void appendRatingList(List<Rating> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Rating>();
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
		this.position = position;
		Rating type = mlist.get(position);
		if (isProgress) {
			view = inflater.inflate(R.layout.progress_footer, null);

			return view;
		} else {
			if (convertView != null && convertView instanceof UserItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.rating_list, null);
			}

			ImageLoader.getInstance().displayImage(type.getHead_photo(),
					(ImageView) view.findViewById(R.id.iv_pic));
			((TextView) view.findViewById(R.id.tv_info))
					.setText(type.getInfo());
			((TextView) view.findViewById(R.id.tv_time)).setText(type
					.getAdd_time());
			((TextView) view.findViewById(R.id.tv_name)).setText(type
					.getUsername());
			((RatingBar) view.findViewById(R.id.rb1)).setRating(Float
					.valueOf(type.getService()));
			((RatingBar) view.findViewById(R.id.rb2)).setRating(Float
					.valueOf(type.getPrice()));
			((RatingBar) view.findViewById(R.id.rb3)).setRating(Float
					.valueOf(type.getMilieu()));
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
