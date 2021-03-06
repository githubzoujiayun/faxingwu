package com.jm.fxw;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.QuestionChat;
import com.jm.finals.Constant;
import com.jm.service.PushService;
import com.jm.session.SessionManager;
import com.jm.sort.ChatAdapter;
import com.jm.util.CameraAndGallery;
import com.jm.util.LogUtil;
import com.jm.util.StartActivityContController;
import com.jm.util.TispToastFactory;

public class ChatUI extends Activity implements OnClickListener,
		RecognizerDialogListener {

	protected Button btn_leftTop, btn_send, btn_image;
	protected ImageButton btn_about, btn_update, btn_help;
	protected TextView tv_mainhead, tv_tophead, tv_uphead;
	private ListView ListView;
	private ChatAdapter adapter;
	private List<QuestionChat> mlist = new ArrayList<QuestionChat>();
	private Timer timer;
	private SessionManager sm;
	private String tid;
	private String max_id = "0";

	private Matrix matrix = new Matrix();
	private CameraAndGallery cag;
	private Bitmap mCurrentBitMap1;
	private String UploadUrl1 = "";
	// 识别Dialog
	private RecognizerDialog iatDialog;

	private boolean isLoad = false;
	// 初始化参数
	private String mInitParams;
	private boolean isPushIn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInitParams = "appid=" + "522405c5";
		iatDialog = new RecognizerDialog(this, mInitParams);
		iatDialog.setListener(this);
		cag = new CameraAndGallery(this);
		setContentView(R.layout.chat);
		Intent i = getIntent();
		isPushIn = i.getBooleanExtra("isPushIn", false);
		tid = i.getStringExtra("tid");
		sm = SessionManager.getInstance();
		if (tid == null || "".equals(tid)) {
			LogUtil.e("tid is null");
			finish();
		}
		ListView = (ListView) findViewById(R.id.my_listview);
		adapter = new ChatAdapter(this, this.getWindowManager()
				.getDefaultDisplay().getWidth());
		ListView.setAdapter(adapter);
		findViewById(R.id.btn_send).setOnClickListener(this);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.iv_hairpic1).setOnClickListener(this);
		findViewById(R.id.iv_voice).setOnClickListener(this);
		getMsgList();
		timer = new Timer();
		timer.schedule(new TimerTaskTest(), 5 * 1000, 5 * 1000);
	}

	public class TimerTaskTest extends java.util.TimerTask {

		public void run() {
			getMsgList();
		}
	}

	@Override
	protected void onResume() {

		StatService.onResume(this);
		super.onResume();
		PushService.isPush = false;
	}

	@Override
	protected void onPause() {
		StatService.onPause(this);
		super.onPause();
		PushService.isPush = true;

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		timer.cancel();
		new ClearPushTask().execute();
	}

	private void getMsgList() {
		if (!isLoad) {
			new getMsgListTask().execute();
		}

	}

	class ClearPushTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("status", "4");
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_CLEARPUSH, getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
		}
	}

	/*
	 * 读取消息详情
	 */

	class getMsgListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			isLoad = true;
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			int ftid = (Integer.parseInt(sm.getUserId()))
					+ (Integer.parseInt(tid));
			LogUtil.e("ftid =" + ftid);
			map.put("uid", sm.getUserId());
			map.put("ftid", ftid);
			map.put("max_id", max_id);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();

			return conn.executeAndParse(Constant.URN_CHAT_LIST, getMsgInqVal());

		}

		protected void onPostExecute(Response result) {
			isLoad = false;
			if (result == null) {
				LogUtil.e("can not get msg List");
				return;
			}
			if (result.isSuccessful()) {
				try {
					if (!"".equals(result.getString("message_list"))) {
						mlist = result.getList("message_list",
								new QuestionChat());
						max_id = mlist.get(mlist.size() - 1).getId();
						LogUtil.e("max_id = " + max_id);
						for (QuestionChat n : mlist) {
							n.setSenderpic(result.getString("ta_head_photo"));
							n.setMypic(result.getString("my_head_photo"));
							n.setType(result.getString("ta_type"));
						}
						adapter.appendChatList(mlist);
						adapter.notifyDataSetChanged();
					}

				} catch (Exception e) {
					LogUtil.e("getMsgListTask=" + e.toString());
				}
			}
		}
	}

	class PublicInfoTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getMsgInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", sm.getUserId());
			map.put("to_id", tid);
			map.put("content", ((EditText) findViewById(R.id.text)).getText()
					.toString().trim());
			map.put("pic", UploadUrl1);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			File file = cag.getNewUploadFile();
			String fileId = null;
			if (mCurrentBitMap1 != null) {
				cag.storeBitmapFile(mCurrentBitMap1, file);
				LogUtil.d("Upload First pic" + file.getAbsolutePath());
				String uploadFileName = file.getAbsolutePath();
				fileId = conn.uploadFile(new File(uploadFileName));
				if (fileId == null || fileId.equals("")) {
					LogUtil.e("First Pic Upload Failed");
					return null;
				} else {
					UploadUrl1 = fileId;
				}
			}
			return conn.executeAndParse(Constant.URN_CHAT_PUBLISH,
					getMsgInqVal());

		}

		protected void onPostExecute(Response result) {

			findViewById(R.id.btn_send).setEnabled(true);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				getMsgList();
				((EditText) findViewById(R.id.text)).setText("");
				clearPicture();
			}
		}
	}

	private void doPhotoAction() {
		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(this,
				android.R.style.Theme_Light);
		String[] choices;
		choices = new String[2];
		choices[0] = this.getString(R.string.roll_photo); // 拍照
		choices[1] = this.getString(R.string.del_photo); // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle(R.string.photo_dlg_title);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:// 翻转照片
							lockwise();
							break;
						case 1:// 删除照片
								// doPickPhotoFromGalleryDoCrop();

							clearPicture();
							break;
						}
					}

				});
		builder.setNegativeButton(R.string.go_back,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		LogUtil.d(requestCode + "/" + resultCode + "/" + intent);

		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case CameraAndGallery.PHOTO_PICKED_WITH_DATA:
			showPicture(intent.getData());
			break;
		case CameraAndGallery.CAMERA_WITH_DATA:

			showPicture(cag.getCameraPictureUri());
			break;
		}
	}

	private void showPicture(Uri uri) {
		if (uri == null) {
			LogUtil.e("When showPicture Uri = null");
			return;
		}
		mCurrentBitMap1 = com.jm.util.ImageUtil.compressImageFromFile(uri
				.getPath());
		if (mCurrentBitMap1 == null) {
			mCurrentBitMap1 = cag.resizeBitmap(uri);
		}
		((ImageView) findViewById(R.id.iv_hairpic1))
				.setImageBitmap(mCurrentBitMap1);
	}

	public void lockwise() {
		matrix.setRotate(90);
		mCurrentBitMap1 = Bitmap.createBitmap(mCurrentBitMap1, 0, 0,
				mCurrentBitMap1.getWidth(), mCurrentBitMap1.getHeight(),
				matrix, true);
		((ImageView) findViewById(R.id.iv_hairpic1))
				.setImageBitmap(mCurrentBitMap1);

	}

	public void clearPicture() {
		UploadUrl1 = "";
		mCurrentBitMap1 = null;
		((ImageView) findViewById(R.id.iv_hairpic1))
				.setImageBitmap(BitmapFactory.decodeResource(getResources(),
						R.drawable.addphoto_green));
	}

	@Override
	public void onClick(View v) {

		EditText et = (EditText) findViewById(R.id.text);
		switch (v.getId()) {
		case R.id.iv_hairpic1:
			if (mCurrentBitMap1 != null) {

				doPhotoAction();
			} else {
				cag.doPickPhotoAction();
			}
			break;
		case R.id.btn_send:

			if (et.getText().toString().trim().equals("")
					&& mCurrentBitMap1 == null) {
				TispToastFactory.getToast(this, "请输入文字或者图片").show();
				return;
			} else {
				v.setEnabled(false);
				new PublicInfoTask().execute();
			}
			break;
		case R.id.btn_leftTop:
			timer.cancel();

			if (isPushIn) {
				StartActivityContController.goPage(ChatUI.this,
						StartActivityContController.wode);
			}
			finish();

			break;
		case R.id.iv_voice:
			showIatDialog();
			break;
		}

	}

	@Override
	public void onEnd(SpeechError arg0) {

	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		((EditText) findViewById(R.id.text)).append(builder);
		((EditText) findViewById(R.id.text))
				.setSelection(((EditText) findViewById(R.id.text)).length());

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

}
