package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Comment;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.CommentAdapter;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;
import com.weibo.sdk.android.Oauth2AccessToken;

public class HuaTiInfoUI extends Activity implements OnClickListener,
		OnItemClickListener, RecognizerDialogListener {

	private CommentAdapter adapter;
	private ListView listView;
	private List<Comment> mlist = new ArrayList<Comment>();
	private String to_uid = "";
	private SessionManager sm;
	private FinalBitmap fbPic;
	// //////////////////////////////////
	public static Oauth2AccessToken accessToken;
	private EditText ed_comment;
	private Handler mHandler;
	private String usertype;
	private String inthid;
	// /////////////////////////////////////////s
	// 识别Dialog
	private RecognizerDialog iatDialog;

	private boolean isPushIn;
	// 初始化参数
	private String mInitParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInitParams = "appid=" + "522405c5";
		iatDialog = new RecognizerDialog(this, mInitParams);
		iatDialog.setListener(this);
		// 初始化缓存对象.S

		// ///////////////////////////////
		fbPic = FinalBitmap.create(this);
		mHandler = new Handler();
		sm = SessionManager.getInstance();
		setContentView(R.layout.huatiinfo);
		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "话题详情页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "话题详情页面");
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.comment_list);
		View view = LayoutInflater.from(this).inflate(R.layout.huatihead, null);
		listView.addHeaderView(view);
		adapter = new CommentAdapter(this);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		ed_comment = (EditText) findViewById(R.id.et_comment);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_xihuan).setOnClickListener(this);
		findViewById(R.id.lin_fenxiang).setOnClickListener(this);
		findViewById(R.id.lin_sixin).setOnClickListener(this);
		findViewById(R.id.btn_yuyue).setOnClickListener(this);
		findViewById(R.id.iv_voice).setOnClickListener(this);

		// mgadapter = new MerchantGalleryAdapter(this, bitmap);
		// FallingGallery gallery = (FallingGallery)
		// findViewById(R.id.gallery_photos);
		// gallery.setAdapter(mgadapter);
		// gallery.setOnItemSelectedListener(this);
		inthid = getIntent().getStringExtra("hid");
		isPushIn = getIntent().getBooleanExtra("isPushIn", false);
		new getHuaTiInfoTask().execute();
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.iv_voice:
			showIatDialog();
			break;

		case R.id.lin_sixin:
			map.put("tid", to_uid);
			StartActivityContController.goPage(HuaTiInfoUI.this, ChatUI.class,
					true, map);
			break;
		case R.id.btn_yuyue:
			// 打开预约界面
			if (((Button) v).getText().equals("预约TA")) {
				map.put("tid", to_uid);
				StartActivityContController.goPage(HuaTiInfoUI.this,
						YuYueUI.class, true, map);
			} else {
				goUserInfoPage();
			}
			break;
		case R.id.btn_leftTop:
			if (isPushIn) {
				StartActivityContController.goPage(HuaTiInfoUI.this,
						StartActivityContController.wode);
			}
			this.finish();
			break;
		case R.id.btn_ok:
			/*
			 * 发布评论
			 */
			if ("".equals(ed_comment.getText().toString().trim())) {
				TispToastFactory.getToast(HuaTiInfoUI.this, "请输入评论内容").show();
			}
			if (sm.isLogin()) {
				/*
				 * 取消输入法
				 */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(HuaTiInfoUI.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				findViewById(R.id.btn_ok).setEnabled(false);
				new PublicCommentTask().execute();
			} else {
				goLoginPage(intent);
			}
			break;

		case R.id.iv_hairinfo_headphoto:
			goUserInfoPage();
			break;
		}

	}

	private void goUserInfoPage() {
		if (!to_uid.equals(sm.getUserId())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", to_uid);
			map.put("type", usertype);
			StartActivityContController.goPage(HuaTiInfoUI.this,
					HisInfoUI.class, false, map);
		}

	}

	/*
	 * 用户发布评论
	 */
	class PublicCommentTask extends AsyncTask<String, Integer, Response> {
		private Map<String, Object> getListInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("action", "add");
			map.put("uid", sm.getUserId());
			map.put("news_id", inthid);
			map.put("content", ed_comment.getText().toString().trim());
			return map;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_HUATICOMMENT,
					getListInqVal());

		}

		protected void onPostExecute(Response result) {

			findViewById(R.id.btn_ok).setEnabled(true);
			if (result == null) {
				LogUtil.e("can not publish comment");
				return;
			} else if (result.isSuccessful()) {

				TispToastFactory.getToast(HuaTiInfoUI.this, result.getMsg())
						.show();
				ed_comment.setText("");
				new getHuaTiInfoTask().execute();
			} else {

				TispToastFactory.getToast(HuaTiInfoUI.this, result.getMsg())
						.show();
			}
		}
	}

	private void showTipInHandler(String s) {
		final String str = s;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				TispToastFactory.getToast(HuaTiInfoUI.this, str).show();
			}
		});
	}

	private void goLoginPage(Intent intent) {
		intent.setClass(this, LoginUI.class);
		startActivity(intent);
	}

	/*
	 * 设置发型图片和发布者图片
	 */
	private void setHairInfo(Response res) {
		usertype = res.getString("type");
		to_uid = res.getString("uid");
		findViewById(R.id.iv_hairinfo_headphoto).setOnClickListener(this);
		fbPic.display((ImageView) findViewById(R.id.iv_hairinfo_headphoto),
				res.getString("head_photo"));
		if (to_uid.equals(sm.getUserId())) {
			findViewById(R.id.lin_sixin).setVisibility(View.GONE);
		} else {
			findViewById(R.id.lin_sixin).setVisibility(View.VISIBLE);
		}

		((Button) findViewById(R.id.btn_yuyue)).setVisibility(View.GONE);

		FinalBitmap.create(this).display(findViewById(R.id.iv_photo),
				res.getString("pic"));

		((TextView) findViewById(R.id.tv_utext)).setText(res
				.getString("content"));
		((TextView) findViewById(R.id.tv_uname)).setText(res
				.getString("username"));

	}

	/*
	 * 读取发型详情
	 */
	class getHuaTiInfoTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getChangeInfoInqVal() {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", inthid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_HUATIINFO,
					getChangeInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				LogUtil.e("can not get hair info");
				return;
			}
			if (result.isSuccessful()) {
				try {
					setHairInfo(result);
					adapter.clear();
					if (!"".equals(result.getString("comment_list"))) {
						mlist = result.getList("comment_list", new Comment());
						if (mlist != null) {
							adapter.setTypeList(mlist);
							adapter.notifyDataSetChanged();
							((TextView) findViewById(R.id.tv_commentsize))
									.setText("共有" + mlist.size() + "条评论");
						} else {
							((TextView) findViewById(R.id.tv_commentsize))
									.setText("暂无评论");
						}
					} else {
						((TextView) findViewById(R.id.tv_commentsize))
								.setText("暂无评论");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LogUtil.e(e.toString());
				}

			} else {
				showTipInHandler(result.getMsg());
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent;
		if (parent.equals(listView)) {
			if (adapter.getList() == null || position - 1 < 0
					|| position - 1 >= adapter.getList().size()) {
				LogUtil.e("position = " + (position - 1));
				return;
			}
			Comment comment = adapter.getList().get(position - 1);
			if (!comment.getUid().equals(sm.getUserId())) {
				intent = new Intent(HuaTiInfoUI.this, HisInfoUI.class);
				intent.putExtra("uid", comment.getUid());
				intent.putExtra("type", comment.getType());
				startActivity(intent);
			}
		}
	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		((EditText) findViewById(R.id.et_comment)).append(builder);
		((EditText) findViewById(R.id.et_comment))
				.setSelection(((EditText) findViewById(R.id.et_comment))
						.length());

	}

	/**
	 * 显示转写对话框.
	 * 
	 * @param
	 */
	public void showIatDialog() {
		// 获取引擎参数
		String engine = "sms";
		// 获取area参数，POI搜索时需要传入.
		String area = null;
		if (TextUtils.isEmpty(area))
			area = "";
		else
			area += ",";
		// 设置转写Dialog的引擎和poi参数.
		iatDialog.setEngine(engine, area, null);
		iatDialog.setSampleRate(RATE.rate16k);
		// 弹出转写Dialog.
		iatDialog.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
