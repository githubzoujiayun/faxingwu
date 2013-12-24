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

import com.jm.entity.ZhouBian;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZhouBianAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<ZhouBian> mlist;
	private Context context;

	public boolean isDianPu = false;
	private boolean isProgress = false;

	public ZhouBianAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<ZhouBian>();
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
	public void addMoreData(ZhouBian type) {

		mlist.add(type);
	}

	public void setTypeList(List<ZhouBian> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<ZhouBian> getUserList() {
		return mlist;

	}

	public void appendUserList(List<ZhouBian> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<ZhouBian>();
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
		ZhouBian type = mlist.get(position);

		if (isProgress) {
			v = inflater.inflate(R.layout.progress_footer, null);
			return v;
		} else {
			ZhouBianItem view = (ZhouBianItem) inflater.inflate(
					R.layout.zhoubianuser_list, null);
			view.setZhouBian(mlist.get(position));
			view.initView();

			ImageLoader.getInstance().displayImage(type.getHead_photo(),
					(ImageView) view.findViewById(R.id.iv_pic));
			((Button) view.findViewById(R.id.btn_isconcerns))
					.setText(type.isconcerns.equals("1") ? "已关注" : " + 关注");
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type
					.getUsername());
			((TextView) view.findViewById(R.id.tv_3_1)).setText(type.signature);

			((TextView) view.findViewById(R.id.tv_2_1)).setText("共 "
					+ type.works_num + "作品");
			if (!type.store_address.equals("")) {
				((TextView) view.findViewById(R.id.tv_4_1))
						.setText(type.store_address);
			} else {
				((TextView) view.findViewById(R.id.tv_4_1))
						.setVisibility(View.GONE);
			}

			if (!type.status.equals("1")) {
				view.findViewById(R.id.iv_renzheng).setVisibility(View.GONE);
			}
			if (isDianPu) {
				view.isDianPu = isDianPu;
				((Button) view.findViewById(R.id.btn_isconcerns))
						.setVisibility(View.GONE);

				((TextView) view.findViewById(R.id.tv_3_1))
						.setVisibility(View.GONE);

				((TextView) view.findViewById(R.id.tv_2_1)).setText(type
						.getAddress());
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
