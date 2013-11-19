package com.jm.fxw;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.YuYueAdapter;
import com.jm.sort.YuYueItem;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;

/**
 * Android实现日历控件
 * 
 * @Description: Android实现日历控件
 * 
 * @File: MainActivity.java
 * 
 * @Package com.calendar.demo
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-6-21 上午11:42:32
 * 
 * @Version V1.0
 */
public class YuYueUI extends FinalActivity implements OnClickListener,
		OnItemClickListener {

	private FinalBitmap fb;
	private ListView yuyuelist;
	private String tid;
	private String[] price;
	private YuYueAdapter adapter;
	private String date;
	private String week;
	private String type;

	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue);
		init();
	}

	private String getWeekOfDate(Date dt, int adddays) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日",
				"周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) + adddays;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	private String getDateOfYeay(Date dt, int adddays) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(dt);
		cal.set(Calendar.DATE, adddays + 1);
		int month = cal.get(Calendar.MONTH) + 1;
		return month + "-" + cal.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobileProbe.onResume(this, "预约页面");
		getUserInfo();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "预约页面");
	}

	private void getUserInfo() {
		new getUserInfo().execute();
		new getTipsInfo().execute();
	}

	private void init() {
		sm = SessionManager.getInstance();
		this.type = "洗剪吹";
		price = new String[] { "", "", "", "", "", "", "", "" };
		yuyuelist = (android.widget.ListView) findViewById(R.id.yuyueinfo_list);
		yuyuelist.addHeaderView(LayoutInflater.from(this).inflate(
				R.layout.yuyuehead, null));
		yuyuelist.setOnItemClickListener(this);
		adapter = new YuYueAdapter(this);
		yuyuelist.setAdapter(adapter);
		fb = FinalBitmap.create(this);
		Intent i = getIntent();
		tid = i.getStringExtra("tid");
		if (tid == null || tid.equals("")) {
			LogUtil.e("tid = " + tid);
			finish();
		}

		resetTypeButtonBg();
		resetTimeButtonBg();
		findViewById(R.id.lin_xi).setBackgroundResource(R.drawable.left_bg1);
		((TextView) findViewById(R.id.btn_t1)).setTextColor(Color.rgb(240,28,97));
		findViewById(R.id.lin_d1).setBackgroundResource(R.color.red);

		Date dt = new Date();
		((TextView) findViewById(R.id.btn_d1)).setText(getWeekOfDate(dt, 0));
		((TextView) findViewById(R.id.btn_d2)).setText(getWeekOfDate(dt, 1));
		((TextView) findViewById(R.id.btn_d3)).setText(getWeekOfDate(dt, 2));
		((TextView) findViewById(R.id.btn_d4)).setText(getWeekOfDate(dt, 3));
		((TextView) findViewById(R.id.btn_d5)).setText(getWeekOfDate(dt, 4));
		((TextView) findViewById(R.id.btn_d6)).setText(getWeekOfDate(dt, 5));
		((TextView) findViewById(R.id.btn_d7)).setText(getWeekOfDate(dt, 6));
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		((TextView) findViewById(R.id.btn_r1)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 0));
		((TextView) findViewById(R.id.btn_r2)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 1));
		((TextView) findViewById(R.id.btn_r3)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 2));
		((TextView) findViewById(R.id.btn_r4)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 3));
		((TextView) findViewById(R.id.btn_r5)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 4));
		((TextView) findViewById(R.id.btn_r6)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 5));
		((TextView) findViewById(R.id.btn_r7)).setText(getDateOfYeay(dt,
				cal.get(Calendar.DATE) + 6));

		findViewById(R.id.lin_xi).setOnClickListener(this);
		findViewById(R.id.lin_tang).setOnClickListener(this);
		findViewById(R.id.lin_ran).setOnClickListener(this);
		findViewById(R.id.lin_hu).setOnClickListener(this);
		findViewById(R.id.lin_d1).setOnClickListener(this);
		findViewById(R.id.lin_d2).setOnClickListener(this);
		findViewById(R.id.lin_d3).setOnClickListener(this);
		findViewById(R.id.lin_d4).setOnClickListener(this);
		findViewById(R.id.lin_d5).setOnClickListener(this);
		findViewById(R.id.lin_d6).setOnClickListener(this);
		findViewById(R.id.lin_d7).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_zuopin).setOnClickListener(this);
		findViewById(R.id.lin_pingjia).setOnClickListener(this);
		setDateAndWeek(1);
	}

	/*
	 * 读取提示信息
	 */
	class getTipsInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", tid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn
					.executeAndParse(Constant.URN_TIPS_INFO, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {
				try {
					JSONArray alist = result.getJsonString("notice_info")
							.getJSONArray("info");
					((TextView) findViewById(R.id.tv_tip1))
							.setText((CharSequence) alist.get(0));
					findViewById(R.id.lin_tip1).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip2))
							.setText((CharSequence) alist.get(1));
					findViewById(R.id.lin_tip2).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip3))
							.setText((CharSequence) alist.get(2));
					findViewById(R.id.lin_tip3).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip4))
							.setText((CharSequence) alist.get(3));
					findViewById(R.id.lin_tip4).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_tip5))
							.setText((CharSequence) alist.get(4));
					findViewById(R.id.lin_tip5).setVisibility(View.VISIBLE);
				} catch (JSONException e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	/*
	 * 读取预约页面个人信息
	 */
	class getUserInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", tid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get userinfo");
				return;
			}
			if (result.isSuccessful()) {

				try {
					((TextView) findViewById(R.id.tv_username)).setText(result
							.getString("username"));
					fb.display((ImageView) findViewById(R.id.iv_minfouserpic),
							result.getString("head_photo"));
					((TextView) findViewById(R.id.tv_address)).setText("地址:"
							+ result.getString("store_address"));
					((TextView) findViewById(R.id.tv_dname)).setText("店名:"
							+ result.getString("store_name"));
					((TextView) findViewById(R.id.tv_zuopin)).setText(result
							.getString("works_num"));
					((TextView) findViewById(R.id.tv_pingjia)).setText(result
							.getString("assess_num"));

					price = result.getString("price_info").split("_");
					setPriceType(1);
				} catch (Exception e) {
					LogUtil.e(e.toString());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.btn_leftTop:
			finish();
			break;
		case R.id.lin_pingjia:
			map.put("uid", tid);
			StartActivityContController.goPage(YuYueUI.this,
					RatingListUI.class, true, map);
			break;
		case R.id.lin_zuopin:

			map.put("uid", tid);
			StartActivityContController.goPage(YuYueUI.this, WorkListUI.class,
					false, map);
			break;

		case R.id.lin_xi:
			// 用户点击洗剪吹
			type = "洗剪吹";
			setPriceType(1);
			resetTypeButtonBg();
			v.setBackgroundResource(R.drawable.left_bg1);
			((TextView) findViewById(R.id.btn_t1)).setTextColor(Color.rgb(240,28,97));
			break;

		case R.id.lin_tang:
			// 用户点击烫发
			type = "烫发";
			setPriceType(2);
			resetTypeButtonBg();
			v.setBackgroundResource(R.drawable.center_bg1);
			((TextView) findViewById(R.id.btn_t2)).setTextColor(Color.rgb(240,28,97));
			break;

		case R.id.lin_ran:
			// 用户点击染发
			type = "染发";
			setPriceType(3);
			resetTypeButtonBg();
			v.setBackgroundResource(R.drawable.center_bg1);
			((TextView) findViewById(R.id.btn_t3)).setTextColor(Color.rgb(240,28,97));
			break;

		case R.id.lin_hu:
			// 用户点击护理
			type = "护理";
			setPriceType(4);
			resetTypeButtonBg();
			v.setBackgroundResource(R.drawable.right_bg1);
			((TextView) findViewById(R.id.btn_t4)).setTextColor(Color.rgb(240,28,97));
			break;

		case R.id.lin_d1:

			setDateAndWeek(1);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d2:
			setDateAndWeek(2);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d3:
			setDateAndWeek(3);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d4:
			setDateAndWeek(4);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d5:
			setDateAndWeek(5);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d6:
			setDateAndWeek(6);
			resetTimeButtonBg();
			setRedBg(v);
			break;

		case R.id.lin_d7:
			setDateAndWeek(7);
			resetTimeButtonBg();
			setRedBg(v);
			break;
		}

	}

	private void setDateAndWeek(int i) {
		if (i == 1) {
			date = ((TextView) findViewById(R.id.btn_r1)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d1)).getText().toString();

		}
		if (i == 2) {
			date = ((TextView) findViewById(R.id.btn_r2)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d2)).getText().toString();
		}
		if (i == 3) {
			date = ((TextView) findViewById(R.id.btn_r3)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d3)).getText().toString();
		}
		if (i == 4) {
			date = ((TextView) findViewById(R.id.btn_r4)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d4)).getText().toString();
		}
		if (i == 5) {
			date = ((TextView) findViewById(R.id.btn_r5)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d5)).getText().toString();
		}
		if (i == 6) {
			date = ((TextView) findViewById(R.id.btn_r6)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d6)).getText().toString();
		}
		if (i == 7) {
			date = ((TextView) findViewById(R.id.btn_r7)).getText().toString();
			week = ((TextView) findViewById(R.id.btn_d7)).getText().toString();
		}

	}

	private void setRedBg(View v) {
		v.setBackgroundResource(R.color.red);

	}

	private void setPriceType(int type) {

		if (price.length < 7) {
			return;
		}
		if (type == 1) {

			adapter.setPrice(price[0], price[4]);
		}
		if (type == 2) {

			adapter.setPrice(price[1], price[5]);
		}
		if (type == 3) {

			adapter.setPrice(price[2], price[6]);
		}
		if (type == 4) {

			adapter.setPrice(price[3], price[7]);
		}

		adapter.notifyDataSetChanged();
	}

	private void resetTypeButtonBg() {
		((TextView) findViewById(R.id.btn_t1)).setTextColor(Color.rgb(0, 0, 0));
		((TextView) findViewById(R.id.btn_t2)).setTextColor(Color.rgb(0, 0, 0));
		((TextView) findViewById(R.id.btn_t3)).setTextColor(Color.rgb(0, 0, 0));
		((TextView) findViewById(R.id.btn_t4)).setTextColor(Color.rgb(0, 0, 0));
		findViewById(R.id.lin_xi).setBackgroundResource(R.drawable.left_bg);
		findViewById(R.id.lin_tang).setBackgroundResource(R.drawable.center_bg);
		findViewById(R.id.lin_ran).setBackgroundResource(R.drawable.center_bg);
		findViewById(R.id.lin_hu).setBackgroundResource(R.drawable.right_bg);
	}

	private void resetTimeButtonBg() {
		findViewById(R.id.lin_d1).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d2).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d3).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d4).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d5).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d6).setBackgroundResource(R.color.black_gary);
		findViewById(R.id.lin_d7).setBackgroundResource(R.color.black_gary);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		LogUtil.e("onItemClick");
		Map<String, String> map = new HashMap<String, String>();
		if (adapter.getList() == null || position < 0
				|| position >= adapter.getList().size()) {
			LogUtil.e("position = " + position);
			return;
		}

		map.put("tid", tid);
		map.put("type", type);
		map.put("date", date);
		map.put("week", week);
		com.jm.entity.YuYue y = (com.jm.entity.YuYue) adapter.getItem(position);
		map.put("price", y.getPrice());
		map.put("rebate", y.getDiscount());
		try {
			YuYueItem yy = (YuYueItem) view;
			map.put("discount", ((TextView) yy.findViewById(R.id.tv_discount))
					.getText().toString().trim());
			map.put("time", ((TextView) yy.findViewById(R.id.tv_time))
					.getText().toString().trim());
			StartActivityContController.goPage(YuYueUI.this, YuYueCheck.class,
					true, map);
		} catch (Exception e) {
			LogUtil.e(e.toString());
		}

	}
}