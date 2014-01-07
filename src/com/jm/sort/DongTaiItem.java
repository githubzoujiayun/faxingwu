package com.jm.sort;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jm.entity.DongTai;
import com.jm.entity.Hair;
import com.jm.fxw.HairInfoUI;
import com.jm.fxw.R;

public class DongTaiItem extends LinearLayout implements OnClickListener {

	private Context context;

	private int id;
	private ArrayList<Hair> hair_list;

	public DongTaiItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		hair_list = new ArrayList<Hair>();
		this.context = context;
	}

	public void setHairList(ArrayList<Hair> list, int index) {
		this.id = index;
		hair_list = list;
	}

	public void initView() {
		findViewById(R.id.iv_pic).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_pic:
			intent = new Intent(context, HairInfoUI.class);
			intent.putExtra("hlist", (Serializable) hair_list);
			intent.putExtra("id", id);
			context.startActivity(intent);
			break;
		}

	}
}
