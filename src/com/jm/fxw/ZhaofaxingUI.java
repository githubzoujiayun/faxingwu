package com.jm.fxw;

import java.io.File;
import java.io.FileReader;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.entity.Type;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.HairTypeAdapter;
import com.jm.util.HairTypeUtil;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;

public class ZhaofaxingUI extends FinalActivity implements OnClickListener,
		OnItemClickListener {

	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;

	private long firstTime = 0;
	private GridView gv_hairtype;
	private HairTypeUtil hairTypeUtil;
	private HairTypeAdapter hairTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhaofaxing);
		CheckUpdate checkUpdate = new CheckUpdate(ZhaofaxingUI.this);
		checkUpdate.setNoNewversion(false);
		checkUpdate.check();
		init();
		new UpLoadCrashLog().execute();
		setFirstType(1);
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "发型分类页面");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "发型分类页面");
	}

	private void setInco() {

		findViewById(R.id.iv_zhaofaxing).setBackgroundResource(
				R.drawable.zhaofaxing1);
		((TextView) findViewById(R.id.tv_zhaofaxing)).setTextColor(Color
				.parseColor("#f01c61"));

	}

	private void setFirstType(int FirstTypeIndex) {
		ResetFirstType();
		switch (FirstTypeIndex) {
		case 1:

			findViewById(R.id.iv_recommend).setBackgroundResource(
					R.drawable.icon_recommend);
			((TextView) findViewById(R.id.tv_recommend))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_recommend_type_triangle).setVisibility(
					View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getrecommendList());

			break;
		case 2:

			findViewById(R.id.iv_women).setBackgroundResource(
					R.drawable.icon_women);
			((TextView) findViewById(R.id.tv_women))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_women_type_triangle).setVisibility(
					View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getwomenList());
			break;
		case 3:

			findViewById(R.id.iv_man)
					.setBackgroundResource(R.drawable.icon_man);
			((TextView) findViewById(R.id.tv_man))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_man_type_triangle).setVisibility(View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getmanList());
			break;
		case 4:

			findViewById(R.id.iv_facetype).setBackgroundResource(
					R.drawable.icon_facetype);
			((TextView) findViewById(R.id.tv_facetype))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_facetype_type_triangle).setVisibility(
					View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getfacetypeList());
			break;
		case 5:

			findViewById(R.id.iv_perm).setBackgroundResource(
					R.drawable.icon_perm);
			((TextView) findViewById(R.id.tv_perm))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_perm_type_triangle)
					.setVisibility(View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getpermList());
			break;
		case 6:

			findViewById(R.id.iv_color).setBackgroundResource(
					R.drawable.icon_color);
			((TextView) findViewById(R.id.tv_color))
					.setTextColor(Constant.color_RoseRed);
			findViewById(R.id.iv_color_type_triangle).setVisibility(
					View.VISIBLE);
			hairTypeAdapter.setHairList(hairTypeUtil.getcolorList());
			break;
		}
		hairTypeAdapter.notifyDataSetChanged();
	}

	private void ResetFirstType() {
		((TextView) findViewById(R.id.tv_recommend))
				.setTextColor(Constant.color_Gary);
		((TextView) findViewById(R.id.tv_women))
				.setTextColor(Constant.color_Gary);
		((TextView) findViewById(R.id.tv_man))
				.setTextColor(Constant.color_Gary);
		((TextView) findViewById(R.id.tv_facetype))
				.setTextColor(Constant.color_Gary);
		((TextView) findViewById(R.id.tv_perm))
				.setTextColor(Constant.color_Gary);
		((TextView) findViewById(R.id.tv_color))
				.setTextColor(Constant.color_Gary);
		findViewById(R.id.iv_recommend).setBackgroundResource(
				R.drawable.icon_recommend1);
		findViewById(R.id.iv_women).setBackgroundResource(
				R.drawable.icon_women1);
		findViewById(R.id.iv_man).setBackgroundResource(R.drawable.icon_man1);
		findViewById(R.id.iv_facetype).setBackgroundResource(
				R.drawable.icon_facetype1);
		findViewById(R.id.iv_perm).setBackgroundResource(R.drawable.icon_perm1);
		findViewById(R.id.iv_color).setBackgroundResource(
				R.drawable.icon_color1);
		findViewById(R.id.iv_recommend_type_triangle).setVisibility(
				View.INVISIBLE);
		findViewById(R.id.iv_women_type_triangle).setVisibility(View.INVISIBLE);
		findViewById(R.id.iv_man_type_triangle).setVisibility(View.INVISIBLE);
		findViewById(R.id.iv_facetype_type_triangle).setVisibility(
				View.INVISIBLE);
		findViewById(R.id.iv_perm_type_triangle).setVisibility(View.INVISIBLE);
		findViewById(R.id.iv_color_type_triangle).setVisibility(View.INVISIBLE);

	}

	private void init() {
		setInco();
		findViewById(R.id.lin_recommend).setOnClickListener(this);
		findViewById(R.id.lin_women).setOnClickListener(this);
		findViewById(R.id.lin_man).setOnClickListener(this);
		findViewById(R.id.lin_facetype).setOnClickListener(this);
		findViewById(R.id.lin_perm).setOnClickListener(this);
		findViewById(R.id.lin_color).setOnClickListener(this);

		gv_hairtype = (GridView) findViewById(R.id.gv_hairtype);
		gv_hairtype.setOnItemClickListener(this);
		hairTypeAdapter = new HairTypeAdapter(this);
		gv_hairtype.setAdapter(hairTypeAdapter);
		hairTypeUtil = new HairTypeUtil();
		// sm = SessionManager.getInstance();
		// share = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		// editor = share.edit();
		//
		// // 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		// WindowManager windowManager = getWindowManager();
		// Display display = windowManager.getDefaultDisplay();
		// int screenWidth = display.getWidth();
		// Calendar_Width = screenWidth - 40;
		// Cell_Width = Calendar_Width / 2 + 1;
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		// Cell_Width, Cell_Width);

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

	// 上传crash log
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
						LogUtil.e("创建连接");
						Connection conn = ((ClientApp) getApplication())
								.getConnection();
						File file = new File(SessionManager.getInstance()
								.getCacheDir() + Constant.CRASH_FILE_NAME);
						LogUtil.e("开始上传 + " + file.toString());
						response = conn.uploadCatchlog(file);
					} else {
						LogUtil.e("读取文件失败");
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
					LogUtil.e("错误日志上传失败");
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
			if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
				Toast.makeText(ZhaofaxingUI.this, "连按两次退出发型屋",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_recommend:
			setFirstType(1);
			break;
		case R.id.lin_women:
			setFirstType(2);
			break;
		case R.id.lin_man:
			setFirstType(3);
			break;
		case R.id.lin_facetype:
			setFirstType(4);
			break;
		case R.id.lin_perm:
			setFirstType(5);
			break;
		case R.id.lin_color:
			setFirstType(6);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Type type = hairTypeAdapter.getItem(position);

		Intent i = new Intent(this, ZhaoFaXingListUI.class);
		i.putExtra("firstType", type.firstType + "");
		i.putExtra("secondType", type.secondType + "");
		i.putExtra("hairName", type.hairName + "");
		i.putExtra("typeBySex", type.typeBySex);
		startActivity(i);
	}
}
