package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jm.entity.DongTai;
import com.jm.entity.Hair;
import com.jm.fxw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DongTaiAdapter extends BaseAdapter {

	private ArrayList<Hair> hair_list;
	private LayoutInflater inflater;
	private List<DongTai> mlist;
	private DongTai dongtai;;
	private boolean isProgress = false;

	public DongTaiAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<DongTai>();
		hair_list = new ArrayList<Hair>();
	}

	public List<DongTai> getHairList() {
		return mlist;
	}

	public List<Hair> getHList() {
		return hair_list;
	}

	public boolean isProgress() {
		return isProgress;
	}

	public void setProgress(boolean isProgress) {
		this.isProgress = isProgress;
	}

	public void appendList(List<DongTai> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<DongTai>();
		}
		mlist.addAll(list);
	}

	public void appendHairList(List<Hair> list) {
		if (hair_list == null) {
			hair_list = new ArrayList<Hair>();
		}
		hair_list.addAll(list);
	}

	public void setHairList(List<DongTai> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	@Override
	public int getCount() {
		if (isProgress) {
			return 1;
		} else {
			return mlist.size();
		}
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

		if (isProgress) {
			v = inflater.inflate(R.layout.progress_footer, null);
			return v;
		} else {
			DongTaiItem view = (DongTaiItem) inflater.inflate(
					R.layout.dongtai_list, null);
			dongtai = mlist.get(position);
			ImageLoader.getInstance().displayImage(dongtai.getWork_image(),
					(ImageView) view.findViewById(R.id.iv_pic));
			((TextView) view.findViewById(R.id.tv_utext)).setText(dongtai
					.getContent());
			if (dongtai.getContent().toString().trim().equals("")) {
				view.findViewById(R.id.tv_utext).setVisibility(View.GONE);
			}
			StringBuffer sb = new StringBuffer();
			sb.append(dongtai.getCollect_num());
			((Button) view.findViewById(R.id.btn_dongtai_info)).setText(sb
					.toString());

			StringBuffer sb2 = new StringBuffer();
			sb2.append("ÆÀÂÛ(" + dongtai.getComment_num() + ")  ");
			((TextView) view.findViewById(R.id.tv_dongtai_info)).setText(sb2
					.toString());
			return view;
		}
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
		if (hair_list != null) {
			hair_list.clear();
		}
		notifyDataSetChanged();

	}

}
