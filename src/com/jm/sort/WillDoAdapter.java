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

import com.jm.entity.WillDo;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class WillDoAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<WillDo> mlist;
	private Context context;
	public boolean isDianPu = false;
	private boolean isProgress = false;

	public WillDoAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<WillDo>();
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
	public void addMoreData(WillDo type) {

		mlist.add(type);
	}

	public void setTypeList(List<WillDo> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<WillDo> getUserList() {
		return mlist;

	}

	public void appendUserList(List<WillDo> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<WillDo>();
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
		View v;
		WillDo type = mlist.get(position);

		if (isProgress) {
			v = inflater.inflate(R.layout.progress_footer, null);
			return v;
		} else {
			WillDoItem view = (WillDoItem) inflater.inflate(
					R.layout.willdo_list, null);
			view.setWillDo(mlist.get(position));
			view.initView();
			ImageLoader.getInstance().displayImage(type.head_photo,
					(ImageView) view.findViewById(R.id.iv_pic));
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type.username);
			if (!type.status.equals("1")) {
				view.findViewById(R.id.iv_renzheng).setVisibility(View.GONE);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("时长" + type.long_service);
			sb.append("价格" + type.price);

			StringBuffer sb2 = new StringBuffer();
			sb2.append("实际价格" + type.reserve_price);
			sb2.append("(" + type.rebate + "折)");
			((TextView) view.findViewById(R.id.tv_2_1)).setText(sb.toString());
			((TextView) view.findViewById(R.id.tv_3_1)).setText(sb2.toString());
			((TextView) view.findViewById(R.id.tv_address))
					.setText(type.store_address);
			((TextView) view.findViewById(R.id.tv_distance))
					.setText(type.distance);

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
