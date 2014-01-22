package com.jm.sort;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jm.entity.FaXingShi;
import com.jm.fxw.HairInfoUI;
import com.jm.fxw.HisInfoUI;
import com.jm.fxw.R;
import com.jm.fxw.YuYueUI;
import com.jm.session.SessionManager;

public class FaXingShiItem extends LinearLayout implements OnClickListener {

	private Context context;

	private FaXingShi faxingshit;

	public FaXingShiItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setFaXingShi(FaXingShi faxingshit) {
		this.faxingshit = faxingshit;
	}

	public void initView() {
		findViewById(R.id.lin_all).setOnClickListener(this);
		findViewById(R.id.btn_yuyuefaxingshi).setOnClickListener(this);
		if (!SessionManager.getInstance().isUser()) {
			findViewById(R.id.btn_yuyuefaxingshi).setVisibility(View.GONE);
		}
		findViewById(R.id.iv_pic).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_pic:
			intent = new Intent(context, HisInfoUI.class);
			intent.putExtra("uid", faxingshit.uid);
			intent.putExtra("type", "2");
			context.startActivity(intent);
			break;
		case R.id.btn_yuyuefaxingshi:
		case R.id.lin_all:
			intent = new Intent(context, YuYueUI.class);
			intent.putExtra("tid", faxingshit.uid);
			context.startActivity(intent);
			break;
		}

	}
}
