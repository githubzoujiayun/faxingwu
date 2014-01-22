package com.jm.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.FaXingShi;
import com.jm.fxw.R;
import com.jm.fxw.WorkListUI;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.view.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FaXingShiAdapter extends BaseAdapter implements
		OnItemClickListener {

	private SmallImageAdapter likeadapter;
	private HorizontalListView likeGallery;
	private LayoutInflater inflater;
	private List<FaXingShi> mlist;
	private boolean isProgress = false;
	private Context context;

	public String priceType = "xijianchui";

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
			likeadapter = new SmallImageAdapter(context);
			FaXingShiItem view = (FaXingShiItem) inflater.inflate(
					R.layout.faxingshiuser_list, null);
			view.setFaXingShi(type);
			view.initView();
			ImageLoader.getInstance().displayImage(type.head_photo,
					(ImageView) view.findViewById(R.id.iv_pic));
			((TextView) view.findViewById(R.id.tv_1_1)).setText(type.username);

			((TextView) view.findViewById(R.id.tv_4_2)).setText(type.distance);

			if ("xijianchui".equals(this.priceType)) {
				((TextView) view.findViewById(R.id.tv_1_2))
						.setText(type.price_xijianchui);
			}
			if ("tangfa".equals(this.priceType)) {
				((TextView) view.findViewById(R.id.tv_1_2))
						.setText(type.price_tangfa);
			}
			if ("ranfa".equals(this.priceType)) {
				((TextView) view.findViewById(R.id.tv_1_2))
						.setText(type.price_ranfa);
			}
			if ("huli".equals(this.priceType)) {
				((TextView) view.findViewById(R.id.tv_1_2))
						.setText(type.price_huli);
			}
			// ///////发型师设置信息//////////////
			((TextView) view.findViewById(R.id.tv_2_1)).setText("用户评价("
					+ type.assess_num + ")");
			((TextView) view.findViewById(R.id.tv_3_1))
					.setText(type.store_name);
			((TextView) view.findViewById(R.id.tv_4_1))
					.setText(type.store_address);

			view.findViewById(R.id.lin_gallery).setVisibility(View.VISIBLE);

			likeGallery = (HorizontalListView) view
					.findViewById(R.id.his_gallery);
			likeGallery.setAdapter(likeadapter);
			likeGallery.setOnItemClickListener(this);
			likeadapter.setImageList(type.works_list, type.uid);
			if (type.works_list == null || type.works_list.size() == 0) {
				view.findViewById(R.id.lin_gallery).setVisibility(View.GONE);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", ((SmallImageItem) view).WhosHair);
		StartActivityContController.goPage(context, WorkListUI.class, false,
				map);
	}
}
