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

import com.jm.entity.ZhouBian;
import com.jm.fxw.R;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZhouBianAdapter extends BaseAdapter implements OnClickListener {

	private SmallImageAdapter likeadapter;
	private HorizontalListView likeGallery;
	private LayoutInflater inflater;
	private List<ZhouBian> mlist;
	private boolean isProgress = false;
	private Context context;

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
			ImageLoader.getInstance().displayImage(type.head_photo,
					(ImageView) view.findViewById(R.id.iv_pic));
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type.username);
			((TextView) view.findViewById(R.id.tv_3_2)).setText(type.distance);
			
			if (type.type.equals("2")) {
				((TextView) view.findViewById(R.id.tv_2_1)).setText("用户评价("
						+ type.assess_num + ")");
				((TextView) view.findViewById(R.id.tv_3_1))
						.setText(type.store_name);
				if (!type.store_address.equals("")) {
					((TextView) view.findViewById(R.id.tv_4_1))
							.setText(type.store_address);
				} else {
					((TextView) view.findViewById(R.id.tv_4_1))
							.setVisibility(View.GONE);
				}
				view.findViewById(R.id.lin_gallery).setVisibility(View.VISIBLE);

				likeadapter = new SmallImageAdapter(context);
				likeGallery = (HorizontalListView) view
						.findViewById(R.id.his_gallery);
				likeGallery.setAdapter(likeadapter);
				likeadapter.setImageList(type.works_list);
				if (type.works_list == null || type.works_list.size() == 0) {
					view.findViewById(R.id.lin_gallery)
							.setVisibility(View.GONE);
				}

			} else {
				((TextView) view.findViewById(R.id.tv_2_1)).setText("用户提问("
						+ type.problem_num + ")");
				((TextView) view.findViewById(R.id.tv_3_1)).setText(type.city);
				if (!type.signature.equals("")) {
					((TextView) view.findViewById(R.id.tv_4_1))
							.setText(type.signature);
				} else {
					((TextView) view.findViewById(R.id.tv_4_1))
							.setVisibility(View.GONE);
				}
			}

			if (!type.status.equals("1")) {
				view.findViewById(R.id.iv_renzheng).setVisibility(View.GONE);
			}
			likeadapter.notifyDataSetChanged();
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
