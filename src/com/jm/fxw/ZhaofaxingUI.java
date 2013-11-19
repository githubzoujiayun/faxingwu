package com.jm.fxw;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.StartActivityContController;

public class ZhaofaxingUI extends FinalActivity implements OnClickListener {
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
	private SessionManager sm;
	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhaofaxing);
		CheckUpdate checkUpdate = new CheckUpdate(ZhaofaxingUI.this);
		checkUpdate.setNoNewversion(false);
		checkUpdate.check();
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "发型分类页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "发型分类页面");
	}

	private void setInco() {
		// TODO Auto-generated method stub
		findViewById(R.id.iv_zhaofaxing).setBackgroundResource(
				R.drawable.zhaofaxing1);
		((TextView) findViewById(R.id.tv_zhaofaxing)).setTextColor(Color
				.parseColor("#f01c61"));

	}

	private void init() {
		setInco();
		sm = SessionManager.getInstance();
		share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		editor = share.edit();

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 2 + 1;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				Cell_Width, Cell_Width);

		findViewById(R.id.fl_duanfa).setOnClickListener(this);
		findViewById(R.id.fl_zhongfa).setOnClickListener(this);
		findViewById(R.id.fl_changfa).setOnClickListener(this);
		findViewById(R.id.fl_panfa).setOnClickListener(this);
		findViewById(R.id.fl_nanfa).setOnClickListener(this);
		findViewById(R.id.fl_quanbu).setOnClickListener(this);
		findViewById(R.id.fl_shoucang).setOnClickListener(this);
		//
		findViewById(R.id.fl_duanfa).setLayoutParams(lp);
		findViewById(R.id.fl_zhongfa).setLayoutParams(lp);
		findViewById(R.id.fl_changfa).setLayoutParams(lp);
		findViewById(R.id.fl_panfa).setLayoutParams(lp);
		findViewById(R.id.fl_nanfa).setLayoutParams(lp);
		findViewById(R.id.fl_quanbu).setLayoutParams(lp);
		findViewById(R.id.fl_shoucang).setLayoutParams(lp);

	}

	public void Click(View v) {
		switch (v.getId()) {
		case R.id.lin_guangchang:
			StartActivityContController.goPage(ZhaofaxingUI.this,
					GuangChangUI.class, false);
			break;
		case R.id.lin_zhaofaxing:
			StartActivityContController.goPage(ZhaofaxingUI.this,
					ZhaofaxingUI.class, false);
			break;
		case R.id.lin_faxingshi:
			StartActivityContController.goPage(ZhaofaxingUI.this,
					FaxingshiUI.class, false);
			break;
		case R.id.lin_wode:
			StartActivityContController.goPage(ZhaofaxingUI.this,
					StartActivityContController.wode);
			break;

		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, ZhaoFaXingListUI.class);
		switch (v.getId()) {
		case R.id.fl_duanfa:
			intent.putExtra("type", "1");
			startActivity(intent);
			break;
		case R.id.fl_zhongfa:
			intent.putExtra("type", "2");
			startActivity(intent);
			break;
		case R.id.fl_changfa:
			intent.putExtra("type", "3");
			startActivity(intent);
			break;
		case R.id.fl_panfa:
			intent.putExtra("type", "4");
			startActivity(intent);
			break;
		case R.id.fl_nanfa:
			intent.putExtra("type", "5");
			startActivity(intent);
			break;
		case R.id.fl_quanbu:
			intent.putExtra("type", "6");
			startActivity(intent);
			break;
		case R.id.fl_shoucang:
			intent.putExtra("type", "7");
			startActivity(intent);
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(ZhaofaxingUI.this);
		builder.setMessage("确认离开发型屋吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
}
