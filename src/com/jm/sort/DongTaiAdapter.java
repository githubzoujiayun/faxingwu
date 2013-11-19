package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
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
import com.jm.util.LogUtil;

public class DongTaiAdapter extends BaseAdapter {

	private ArrayList<Hair> hair_list;
	private LayoutInflater inflater;
	private List<DongTai> mlist;
	private Context context;
	private DongTai dongtai;;
	private boolean isProgress = false;

	public DongTaiAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<DongTai>();
		hair_list = new ArrayList<Hair>();
		this.context = context;
	}

	public List<DongTai> getHairList() {
		return mlist;
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
			LogUtil.e("hair_list = new ArrayList<Hair>();");
			hair_list = new ArrayList<Hair>();
		} else {
			LogUtil.e("hair_list.size = " + hair_list.size());
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
			view.setHairList(hair_list, Integer.parseInt(dongtai.getWork_id()));
			view.setDongTai(dongtai);
			view.initView();

			FinalBitmap.create(context).display(
					(ImageView) view.findViewById(R.id.iv_pic),
					dongtai.getWork_image());

			((TextView) view.findViewById(R.id.tv_utext)).setText(dongtai
					.getContent());
			if (dongtai.getContent().toString().trim().equals("")) {
				view.findViewById(R.id.tv_utext).setVisibility(View.GONE);
			}
			((TextView) view.findViewById(R.id.tv_utime)).setText(dongtai
					.getAdd_time());
			StringBuffer sb = new StringBuffer();
			sb.append(" 鉂� " + dongtai.getCollect_num() + " ");
			((Button) view.findViewById(R.id.btn_dongtai_info)).setText(sb
					.toString());

			StringBuffer sb2 = new StringBuffer();
			sb2.append("璇勮 (" + dongtai.getComment_num() + ")  ");
			((TextView) view.findViewById(R.id.tv_dongtai_info)).setText(sb2
					.toString());
			return view;
		}
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
			notifyDataSetChanged();
		}
		if (hair_list != null) {
			hair_list.clear();
			notifyDataSetChanged();
		}
		
	}

}
