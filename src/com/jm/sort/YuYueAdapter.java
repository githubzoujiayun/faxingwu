package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jm.entity.YuYue;
import com.jm.fxw.R;

public class YuYueAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<YuYue> mlist;

	private Context context;

	public YuYueAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<YuYue>();
		mlist.add(new YuYue("10:00"));
		mlist.add(new YuYue("11:00"));
		mlist.add(new YuYue("12:00"));
		mlist.add(new YuYue("13:00"));
		mlist.add(new YuYue("14:00"));
		mlist.add(new YuYue("15:00"));
		mlist.add(new YuYue("16:00"));
		mlist.add(new YuYue("17:00"));
		mlist.add(new YuYue("18:00"));
		mlist.add(new YuYue("19:00"));
		mlist.add(new YuYue("20:00"));
		mlist.add(new YuYue("21:00"));
		mlist.add(new YuYue("22:00"));
		this.context = context;
	}

	public String getDiscount() {
		return mlist.get(0).getDiscount();

	}

	public void setHairList(List<YuYue> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	public void setPrice(String price, String discount) {
		for (YuYue yy : mlist) {
			yy.setPrice(price);
			yy.setDiscount(discount);
		}
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	public List<YuYue> getList() {
		return mlist;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		YuYue yy = mlist.get(position);
		if (convertView != null && convertView instanceof YuYueItem) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.yuyue_list, null);
		}
		((TextView) view.findViewById(R.id.tv_time)).setText(yy.getTime());
		if ("".equals(yy.getPrice())) {
			return view;
		}
		if ("".equals(yy.getDiscount())) {
			return view;
		}
		((TextView) view.findViewById(R.id.tv_price)).setText("£§"
				+ yy.getPrice());
		((TextView) view.findViewById(R.id.tv_price)).getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		double p = (Double.parseDouble(yy.getPrice()) * Double.parseDouble(yy
				.getDiscount())) / 10;
		((TextView) view.findViewById(R.id.tv_discount)).setText("£§" + (int) p
				+ "(" + yy.getDiscount() + "’€)");

		return view;
	}

	@Override
	public int getCount() {

		return mlist.size();
	}

}
