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
import android.widget.TextView;

import com.jm.entity.Reserve;
import com.jm.fxw.R;

public class ReserveAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<Reserve> mlist;

	private Context context;

	private int position;
	private Button btn;

	private boolean isProgress = false;

	public ReserveAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Reserve>();
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
	public void addMoreData(Reserve type) {

		mlist.add(type);
	}

	public void setTypeList(List<Reserve> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<Reserve> getReserveList() {
		return mlist;

	}

	public void appendUserList(List<Reserve> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<Reserve>();
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
		Reserve type = mlist.get(position);
		if (isProgress) {
			view = inflater.inflate(R.layout.progress_footer, null);

			return view;
		} else {
			if (convertView != null && convertView instanceof UserItem) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.reserve_list, null);
			}
			FinalBitmap.create(context).display(view.findViewById(R.id.iv_pic),
					type.getHead_photo());
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type
					.getMy_name());
			((TextView) view.findViewById(R.id.tv_reservetype)).setText("预约类型:"
					+ type.getReserver_type());

			((TextView) view.findViewById(R.id.tv_2_1)).setText("手机号码:"
					+ type.getMy_tel());
			((TextView) view.findViewById(R.id.tv_3_1)).setText(type
					.getReserve_time() + type.getReserve_hour());
			//
			//
			// 0(自己取消)
			// 1(进行中) 确定或者取消
			// 2(已完成，未评价)
			// 3(发型师取消)
			// 4(已完成，已评价)
			if ("0".equals(type.getStatus())) {
				((TextView) view.findViewById(R.id.tv_3_2)).setText("用户取消");
			} else if ("1".equals(type.getStatus())) {
				((TextView) view.findViewById(R.id.tv_3_2)).setText("进行中");

			} else if ("2".equals(type.getStatus())) {
				((TextView) view.findViewById(R.id.tv_3_2)).setText("已完成");

			} else if ("3".equals(type.getStatus())) {
				((TextView) view.findViewById(R.id.tv_3_2)).setText("发型师取消");

			} else if ("4".equals(type.getStatus())) {
				((TextView) view.findViewById(R.id.tv_3_2)).setText("已评价");

			} else {
				((TextView) view.findViewById(R.id.tv_2_2)).setText("未知状态");
			}
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
