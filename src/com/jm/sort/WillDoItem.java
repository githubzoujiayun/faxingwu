package com.jm.sort;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jm.entity.WillDo;
import com.jm.fxw.HisInfoUI;
import com.jm.fxw.R;
import com.jm.fxw.YuYueUI;

public class WillDoItem extends LinearLayout implements OnClickListener {

	private Context context;

	private WillDo willdo;

	public WillDoItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public void setWillDo(WillDo zhoubian) {
		this.willdo = zhoubian;
	}

	public void initView() {
		findViewById(R.id.iv_pic).setOnClickListener(this);
		findViewById(R.id.btn_isconcerns).setOnClickListener(this);
		findViewById(R.id.lin_all).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.btn_isconcerns:
			intent = new Intent(context, YuYueUI.class);
			intent.putExtra("tid", willdo.uid);
			intent.putExtra("hid", willdo.work_id);
			context.startActivity(intent);
			break;
		case R.id.iv_pic:
		case R.id.lin_all:
			intent = new Intent(context, HisInfoUI.class);
			intent.putExtra("uid", willdo.uid);
			intent.putExtra("type", "2");
			context.startActivity(intent);
			break;
		}
	}

}
