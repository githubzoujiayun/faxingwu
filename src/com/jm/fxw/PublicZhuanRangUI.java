package com.jm.fxw;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.weibo.sdk.android.Oauth2AccessToken;

public class PublicZhuanRangUI extends FinalActivity implements OnClickListener {

	private ProgressDialog dialog;
	private SessionManager sm;
	private String str_job = "欧美风格";
	public static Oauth2AccessToken accessToken;
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Integer provinceId, cityId;
	private static final String[] jobs = { "欧美风格", "日韩风格", "简洁风格", "复古风格",
			"精品风格", "个性风格" };
	private ArrayAdapter<String> jobs_spadapter;
	private Spinner sp_job;
	private String strProvince, strCity, strCounty;
	// 市，自治区集合
	private int[] city = { R.array.beijin_province_item,
			R.array.tianjin_province_item, R.array.heibei_province_item,
			R.array.shanxi1_province_item, R.array.neimenggu_province_item,
			R.array.liaoning_province_item, R.array.jilin_province_item,
			R.array.heilongjiang_province_item, R.array.shanghai_province_item,
			R.array.jiangsu_province_item, R.array.zhejiang_province_item,
			R.array.anhui_province_item, R.array.fujian_province_item,
			R.array.jiangxi_province_item, R.array.shandong_province_item,
			R.array.henan_province_item, R.array.hubei_province_item,
			R.array.hunan_province_item, R.array.guangdong_province_item,
			R.array.guangxi_province_item, R.array.hainan_province_item,
			R.array.chongqing_province_item, R.array.sichuan_province_item,
			R.array.guizhou_province_item, R.array.yunnan_province_item,
			R.array.xizang_province_item, R.array.shanxi2_province_item,
			R.array.gansu_province_item, R.array.qinghai_province_item,
			R.array.linxia_province_item, R.array.xinjiang_province_item,
			R.array.hongkong_province_item, R.array.aomen_province_item,
			R.array.taiwan_province_item };

	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	private ArrayAdapter<CharSequence> county_adapter;

	// /////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publiczhuanrang);
		sm = SessionManager.getInstance();
		initView();

		loadSpinner();
	}



	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {

		StatService.onPause(this);
		super.onPause();
	}
	private void initView() {
		findViewById(R.id.btn_PublicTop).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		sp_job = (Spinner) findViewById(R.id.job_spinnered);
		jobs_spadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, jobs);

		jobs_spadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_job.setAdapter(jobs_spadapter);
		sp_job.setOnItemSelectedListener(new JobSelectedListener());
	}

	private void loadSpinner() {
		province_spinner = (Spinner) findViewById(R.id.province_spinner);
		city_spinner = (Spinner) findViewById(R.id.city_spinner);
		// 绑定省份的数据
		province_spinner.setPrompt("请选择省份");
		province_adapter = ArrayAdapter.createFromResource(this,
				R.array.province_item, android.R.layout.simple_spinner_item);
		province_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		province_spinner.setAdapter(province_adapter);
		// select(province_spinner, province_adapter, R.array.province_item);
		// 添加监听，一开始的时候城市，县区的内容是不显示的而是根据省的内容进行联动
		province_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						provinceId = province_spinner.getSelectedItemPosition();
						strProvince = province_spinner.getSelectedItem()
								.toString();// 得到选择的内容，也就是省的名字

						if (true) {
							System.out.println("province: "
									+ province_spinner.getSelectedItem()
											.toString() + provinceId.toString());

							city_spinner = (Spinner) findViewById(R.id.city_spinner);
							city_spinner.setPrompt("请选择城市");// 设置标题
							select(city_spinner, city_adapter, city[provinceId]);// 城市一级的数据绑定
							/*
							 * 通过这个city[provinceId]指明了该省市的City集合 R。array.beijing
							 */
							city_spinner
									.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											cityId = city_spinner
													.getSelectedItemPosition();// 得到city的id
											strCity = city_spinner
													.getSelectedItem()
													.toString();// 得到city的内容
											Log.v("test", "city: "
													+ city_spinner
															.getSelectedItem()
															.toString()// 输出测试一下
													+ cityId.toString());

											city_spinner
													.setOnItemSelectedListener(new OnItemSelectedListener() {

														@Override
														public void onItemSelected(
																AdapterView<?> arg0,
																View arg1,
																int arg2,
																long arg3) {
															strCity = city_spinner
																	.getSelectedItem()
																	.toString();// 得到city的内容

														}

														@Override
														public void onNothingSelected(
																AdapterView<?> arg0) {

														}

													});

										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {
											

										}

									});
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}

	/* 通过方法动态的添加适配器 */
	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter,
			int arry) {
		// 注意这里的arry不仅仅但是一个整形，他代表了一个数组！
		adapter = ArrayAdapter.createFromResource(this, arry,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		// spin.setSelection(0,true);
	}

	class JobSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> AdapterView, View view,
				int position, long arg3) {
			
			String selected = AdapterView.getItemAtPosition(position)
					.toString();
			str_job = selected;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
			System.out.println("NothingSelected");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_leftTop:
			finish();
			break;

		case R.id.btn_PublicTop:
			if (CheckValue()) {
				LogUtil.i("开始上传图片");
				new UpLoadImage().execute();
			}
			break;
		}

	}

	private boolean CheckValue() {

		return true;
	}

	class UpLoadImage extends AsyncTask<String, Integer, Response> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PublicZhuanRangUI.this, "",
					getString(R.string.pubHair), true);

		}

		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());

			map.put("style", str_job);

			map.put("acreage",
					((EditText) findViewById(R.id.et_zhuanrangmianji))
							.getText());
			map.put("city", strCity);
			map.put("address",
					((EditText) findViewById(R.id.et_jobaddress)).getText());
			map.put("telephone",
					((EditText) findViewById(R.id.et_jobphone)).getText());
			map.put("store_name",
					((EditText) findViewById(R.id.et_zhuanrangname)).getText());
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_ADDZHUANRANG,
					getListInqVal());

		}

		@Override
		protected void onPostExecute(Response res) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (res == null) {
				netDialog();
				return;
			}
			if (res.isSuccessful()) {
				TispToastFactory.getToast(PublicZhuanRangUI.this, res.getMsg())
						.show();
				finish();
			} else {

				TispToastFactory.getToast(PublicZhuanRangUI.this, res.getMsg())
						.show();
			}

		}
	}

	/*
	 * time out 后 充值请求
	 */

	public void netDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.getinfofail);
		builder.setMessage(R.string.netunused);
		builder.setPositiveButton(R.string.giveup,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						PublicZhuanRangUI.this.finish();

					}
				});
		builder.setNegativeButton(R.string.tryagain,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new UpLoadImage().execute();
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

}
