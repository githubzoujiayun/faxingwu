package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jm.entity.YuYue;
import com.jm.fxw.R;
import com.jm.util.WidgetUtil;

public class YuYueAdapter extends BaseAdapter implements OnClickListener {

	private LayoutInflater inflater;
	private List<YuYue> mlist;

	private Context context;

	public YuYueAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<YuYue>();

		mlist.add(new YuYue("10:00", "11:00", "12:00"));
		mlist.add(new YuYue("13:00", "14:00", "15:00"));
		mlist.add(new YuYue("16:00", "17:00", "18:00"));
		mlist.add(new YuYue("19:00", "20:00", "21:00"));
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
		view.findViewById(R.id.tv_time1).setOnClickListener(this);
		view.findViewById(R.id.tv_time2).setOnClickListener(this);
		view.findViewById(R.id.tv_time3).setOnClickListener(this);

		((Button) view.findViewById(R.id.tv_time1)).setText(yy.time1);
		((Button) view.findViewById(R.id.tv_time2)).setText(yy.time2);
		((Button) view.findViewById(R.id.tv_time3)).setText(yy.time3);

		// ((TextView) view.findViewById(R.id.tv_price)).setText("£§"
		// + yy.getPrice());
		// ((TextView) view.findViewById(R.id.tv_price)).getPaint().setFlags(
		// Paint.STRIKE_THRU_TEXT_FLAG);
		// double p = (Double.parseDouble(yy.getPrice()) * Double.parseDouble(yy
		// .getDiscount())) / 10;
		// ((TextView) view.findViewById(R.id.tv_discount)).setText("£§" + (int)
		// p
		// + "(" + yy.getDiscount() + "’€)");

		return view;
	}

	@Override
	public int getCount() {

		return mlist.size();
	}

	@Override
	public void onClick(View v) {
		WidgetUtil.setChangeButton(v);
		switch (v.getId()) {
		case R.id.tv_time1:

			break;

		case R.id.tv_time2:

			break;
		case R.id.tv_time3:

			break;
		}

	}

}
