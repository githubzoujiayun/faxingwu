package com.jm.fxw;

import java.io.File;
import java.io.FileReader;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
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

	private long firstTime = 0;
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
		new UpLoadCrashLog().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "���ͷ���ҳ��");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "���ͷ���ҳ��");
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

		// �����Ļ��͸ߣ���Ӌ�����Ļ���ȷ��ߵȷݵĴ�С
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth - 40;
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
		findViewById(R.id.fl_shoucang2).setLayoutParams(lp);

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

	// �ϴ�crash log
	class UpLoadCrashLog extends AsyncTask<String, String, JSONObject> {

		File file = new File(SessionManager.getInstance().getCacheDir(),
				Constant.CRASH_FILE_NAME);

		@Override
		protected JSONObject doInBackground(String... arg0) {
			JSONObject response = null;
			LogUtil.e("file.exists()--" + file.exists());
			if (file.exists()) {
				try {
					if (new FileReader(file).read() != -1) {
						LogUtil.e("��������");
						Connection conn = ((ClientApp) getApplication())
								.getConnection();
						File file = new File(SessionManager.getInstance()
								.getCacheDir() + Constant.CRASH_FILE_NAME);
						LogUtil.e("��ʼ�ϴ� + " + file.toString());
						response = conn.uploadCatchlog(file);
					} else {
						LogUtil.e("��ȡ�ļ�ʧ��");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			if (result == null) {
				return;
			}
			LogUtil.e("result = " + result.toString());
			try {
				if ("101".equals(result.getString("code"))) {
					file.delete();
				} else {
					LogUtil.e("������־�ϴ�ʧ��");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 800) {// ������ΰ���ʱ��������800���룬���˳�
				Toast.makeText(ZhaofaxingUI.this, "���������˳�������",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// ����firstTime
				return true;
			} else {
				System.exit(0);// �����˳�����
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
