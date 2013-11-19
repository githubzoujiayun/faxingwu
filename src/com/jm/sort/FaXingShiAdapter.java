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

import com.jm.entity.FaXingShi;
import com.jm.fxw.R;

public class FaXingShiAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<FaXingShi> mlist;
	private Context context;
	private FinalBitmap fb;

	public boolean isDianPu = false;
	private boolean isProgress = false;

	public FaXingShiAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<FaXingShi>();
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
	public void addMoreData(FaXingShi type) {

		mlist.add(type);
	}

	public void setTypeList(List<FaXingShi> list) {
		if (list == null) {
			return;
		}
		mlist = list;
	}

	@Override
	public int getCount() {
		return mlist.size();

	}

	public List<FaXingShi> getUserList() {
		return mlist;

	}

	public void appendUserList(List<FaXingShi> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<FaXingShi>();
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
		FaXingShi type = mlist.get(position);

		if (isProgress) {
			v = inflater.inflate(R.layout.progress_footer, null);
			return v;
		} else {
			FaXingShiItem view = (FaXingShiItem) inflater.inflate(
					R.layout.faxingshiuser_list, null);
			view.setFaXingShi(mlist.get(position));
			view.initView();

			fb = FinalBitmap.create(context);
			fb.display((ImageView) view.findViewById(R.id.iv_pic),
					type.getHead_photo());
			((Button) view.findViewById(R.id.btn_isconcerns))
					.setText(type.isconcerns.equals("1") ? "取消关注" : " + 关注");
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type
					.getUsername());

			((TextView) view.findViewById(R.id.tv_2_1)).setText("共 "
					+ type.works_num + "作品");

			if (!type.store_address.equals("")) {
				((TextView) view.findViewById(R.id.tv_4_1))
						.setText(type.store_address);
			} else {
				((TextView) view.findViewById(R.id.tv_4_1))
						.setVisibility(View.GONE);
			}
			if (!type.states.equals("1")) {
				view.findViewById(R.id.iv_renzheng).setVisibility(View.GONE);
			}

			if ("".equals(type.distance)) {

				((TextView) view.findViewById(R.id.tv_3_1))
						.setText(type.signature);
			} else {
				((TextView) view.findViewById(R.id.tv_3_1))
						.setText(type.distance);
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
