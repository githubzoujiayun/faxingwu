package com.jm.fxw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.sort.YuYueItem;
import com.jm.util.LogUtil;
import com.jm.util.PriceUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.WidgetUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YuYueUI extends FinalActivity implements OnClickListener {
	private String tid, hid;
	private String[] price;
	private String date;
	private String week;
	private String type;
	private List<View> blist;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyuehead);
		init();
	}

	private String getWeekOfDate(Date dt, int adddays) {
		String[] weekDays = { "����", "��һ", "�ܶ�", "����", "����", "����", "����", "����",
				"��һ", "�ܶ�", "����", "����", "����", "����" };
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
		MobileProbe.onResume(this, "ԤԼҳ��");
		getUserInfo();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobileProbe.onPause(this, "ԤԼҳ��");
	}

	private void getUserInfo() {
		new getUserInfo().execute();
	}

	private void init() {
		this.type = "ϴ����";
		price = new String[] { "", "", "", "", "", "", "", "" };

		Intent i = getIntent();
		tid = i.getStringExtra("tid");
		if (tid == null || tid.equals("")) {
			LogUtil.e("tid = " + tid);
			finish();
		}
		hid = i.getStringExtra("hid");
		if (hid == null || hid.equals("")) {
			YuyueType();
		} else {
			YuyueHair();
		}

		ChangeType("ϴ����", 1, findViewById(R.id.lin_xi));
		resetTimeButtonBg();

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

		blist = new ArrayList<View>();
		blist.add(findViewById(R.id.tv_time1));
		blist.add(findViewById(R.id.tv_time2));
		blist.add(findViewById(R.id.tv_time3));
		blist.add(findViewById(R.id.tv_time4));
		blist.add(findViewById(R.id.tv_time5));
		blist.add(findViewById(R.id.tv_time6));
		blist.add(findViewById(R.id.tv_time7));
		blist.add(findViewById(R.id.tv_time8));
		blist.add(findViewById(R.id.tv_time9));
		blist.add(findViewById(R.id.tv_time10));
		blist.add(findViewById(R.id.tv_time11));
		blist.add(findViewById(R.id.tv_time12));
		WidgetUtil.ResetAllButton(blist);
		for (View view : blist) {
			view.setOnClickListener(this);
		}
		ChangeTime(findViewById(R.id.tv_time1));
		setDateAndWeek(1, findViewById(R.id.lin_d1));
	}

	private void YuyueType() {

		LogUtil.e("��ҳ��ΪԤԼ����ʦ");
		findViewById(R.id.lin_yuyuetype).setVisibility(View.VISIBLE);
		findViewById(R.id.lin_yuyuehair).setVisibility(View.GONE);

	}

	private void YuyueHair() {
		new getYuYueHairInfo().execute();
		LogUtil.e("��ҳ��ΪԤԼ����");
		findViewById(R.id.lin_yuyuetype).setVisibility(View.GONE);
		findViewById(R.id.lin_yuyuehair).setVisibility(View.VISIBLE);

	}

	/*
	 * ��ȡԤԼ���͵���Ϣ
	 */
	class getYuYueHairInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("work_id", hid);
			map.put("uid", tid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_OTHERWILLDO,
					getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can't get YuYueHairInfo");
				return;
			}
			if (result.isSuccessful()) {
				ImageLoader.getInstance().displayImage(
						result.getString("work_image"),
						(ImageView) findViewById(R.id.iv_yuyuehair));
				setPrice(result.getString("price"), result.getString("rebate"),
						result.getString("long_service"));
			}
		}

	}

	private void setPrice(String price, String rebate) {
		((TextView) findViewById(R.id.yuyuejiage)).setText("ƽʱ�۸�:" + price
				+ "Ԫ");
		((TextView) findViewById(R.id.yuyuejiage)).getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		((TextView) findViewById(R.id.zhekoujiage)).setText("�Żݼ�"
				+ PriceUtil.getRealPrice(price, rebate) + "Ԫ" + "(" + rebate
				+ "��)");

	}

	private void setPrice(String price, String rebate, String long_service) {
		((TextView) findViewById(R.id.fuwushichang)).setText("����ʱ��:"
				+ long_service);
		findViewById(R.id.fuwushichang).setVisibility(View.VISIBLE);
		setPrice(price, rebate);
	}

	private void setTime(String rebate) {
		((TextView) findViewById(R.id.yuyueshijian)).setText("ԤԼʱ��:" + rebate);

	}

	/*
	 * ��ȡԤԼҳ�������Ϣ
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

					ImageLoader.getInstance().displayImage(
							result.getString("head_photo"),
							(ImageView) findViewById(R.id.iv_minfouserpic));
					((TextView) findViewById(R.id.tv_address)).setText("��ַ:"
							+ result.getString("store_address"));
					((TextView) findViewById(R.id.tv_dname)).setText("����:"
							+ result.getString("store_name"));
					((TextView) findViewById(R.id.tv_zuopin)).setText(result
							.getString("works_num"));
					((TextView) findViewById(R.id.tv_pingjia)).setText(result
							.getString("assess_num"));

					price = result.getString("price_info").split("_");
					ChangeType("ϴ����", 1, findViewById(R.id.lin_xi));
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
			// �û����ϴ����

			ChangeType("ϴ����", 1, v);
			break;

		case R.id.lin_tang:
			// �û�����̷�
			ChangeType("�̷�", 2, v);
			break;

		case R.id.lin_ran:
			// �û����Ⱦ��

			ChangeType("Ⱦ��", 3, v);
			break;

		case R.id.lin_hu:
			// �û��������
			ChangeType("����", 4, v);
			break;

		case R.id.lin_d1:

			setDateAndWeek(1, v);
			break;

		case R.id.lin_d2:
			setDateAndWeek(2, v);
			break;

		case R.id.lin_d3:
			setDateAndWeek(3, v);
			break;

		case R.id.lin_d4:
			setDateAndWeek(4, v);
			break;

		case R.id.lin_d5:
			setDateAndWeek(5, v);
			break;

		case R.id.lin_d6:
			setDateAndWeek(6, v);
			break;

		case R.id.lin_d7:
			setDateAndWeek(7, v);
			break;

		case R.id.tv_time1:
		case R.id.tv_time2:
		case R.id.tv_time3:
		case R.id.tv_time4:
		case R.id.tv_time5:
		case R.id.tv_time6:
		case R.id.tv_time7:
		case R.id.tv_time8:
		case R.id.tv_time9:
		case R.id.tv_time10:
		case R.id.tv_time11:
		case R.id.tv_time12:
			ChangeTime(v);
			break;
		}

	}

	private void ChangeTime(View v) {
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);
		this.time = ((Button) v).getText().toString().trim();
		setTime(date + week + time);

	}

	private void ChangeType(String string, int i, View v) {

		type = string;
		setPriceType(i);

		List<View> blist = new ArrayList<View>();
		blist.add(findViewById(R.id.lin_xi));
		blist.add(findViewById(R.id.lin_tang));
		blist.add(findViewById(R.id.lin_ran));
		blist.add(findViewById(R.id.lin_hu));
		WidgetUtil.ResetAllButton(blist);
		WidgetUtil.setChangeButton(v);

	}

	private void setDateAndWeek(int i, View v) {
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
		resetTimeButtonBg();
		setRedBg(v);
		setTime(date + week + time);
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

	private void setRedBg(View v) {
		v.setBackgroundResource(R.color.red);
	}

	private void setPriceType(int type) {

		if (price.length < 7) {
			return;
		}
		if (type == 1) {
			setPrice(price[0], price[4]);
		}
		if (type == 2) {
			setPrice(price[1], price[5]);
		}
		if (type == 3) {
			setPrice(price[2], price[6]);
		}
		if (type == 4) {
			setPrice(price[3], price[7]);
		}

	}

	public void onItemClick(View view, int position, long arg3) {
		LogUtil.e("onItemClick");
		Map<String, String> map = new HashMap<String, String>();
		map.put("tid", tid);
		map.put("type", type);
		map.put("date", date);
		map.put("week", week);
		// com.jm.entity.YuYue y = (com.jm.entity.YuYue)
		// adapter.getItem(position);
		// map.put("price", y.getPrice());
		// map.put("rebate", y.getDiscount());
		YuYueItem yy = (YuYueItem) view;
		map.put("discount", ((TextView) yy.findViewById(R.id.tv_discount))
				.getText().toString().trim());
		map.put("time", ((TextView) yy.findViewById(R.id.tv_time)).getText()
				.toString().trim());
		StartActivityContController.goPage(YuYueUI.this, YuYueCheck.class,
				true, map);
	}
}