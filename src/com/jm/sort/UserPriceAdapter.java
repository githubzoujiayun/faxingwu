package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.User;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserPriceAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<User> mlist;

	private Context context;

	private int position;
	private Button btn;
	private int discount = 1;
	private boolean isProgress = false;

	public UserPriceAdapter(Context context) {
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

	public void setPriceList(int i) {
		discount = i;
		try {
			for (User u : mlist) {
				if (i == 2) {
					double p = (Double
							.parseDouble(u.getPrice_info().split("_")[1]) * Double
							.parseDouble(u.getPrice_info().split("_")[5])) / 10;
					u.setPrice("￥" + (int) p + "("
							+ u.getPrice_info().split("_")[4] + "折)");
				} else if (i == 3) {
					double p = (Double
							.parseDouble(u.getPrice_info().split("_")[2]) * Double
							.parseDouble(u.getPrice_info().split("_")[6])) / 10;
					u.setPrice("￥" + (int) p + "("
							+ u.getPrice_info().split("_")[4] + "折)");

				} else if (i == 4) {
					double p = (Double
							.parseDouble(u.getPrice_info().split("_")[3]) * Double
							.parseDouble(u.getPrice_info().split("_")[7])) / 10;
					u.setPrice("￥" + (int) p + "("
							+ u.getPrice_info().split("_")[4] + "折)");
				} else {
					{
						double p = (Double.parseDouble(u.getPrice_info().split(
								"_")[0]) * Double.parseDouble(u.getPrice_info()
								.split("_")[4])) / 10;
						u.setPrice("￥" + (int) p + "("
								+ u.getPrice_info().split("_")[4] + "折)");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

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
		this.position = position;
		User type = mlist.get(position);
		if (isProgress) {
			view = inflater.inflate(R.layout.progress_footer, null);

			return view;
		} else {
			if (convertView != null && convertView instanceof UserItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.price_list, null);
			}

			setPriceList(discount);
			ImageLoader.getInstance().displayImage(type.getHead_photo(),
					(ImageView) view.findViewById(R.id.iv_pic));

			((TextView) view.findViewById(R.id.tv_1_1)).setText(type
					.getUsername());
			((TextView) view.findViewById(R.id.tv_2_1)).setText(type
					.getStore_name());
			((TextView) view.findViewById(R.id.tv_3_1)).setText(type
					.getStore_address());
			((TextView) view.findViewById(R.id.tv_3_2))
					.setText(type.getPrice());
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
