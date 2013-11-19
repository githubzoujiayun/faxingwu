package com.jm.fxw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.Rating;
import com.jm.finals.Constant;
import com.jm.session.SessionManager;
import com.jm.sort.UserRatingAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;
import com.jm.view.MyListView;
import com.jm.view.MyListView.ScrollCallback;

public class RatingListUI extends Activity implements OnClickListener,
		ScrollCallback {
	private MyListView ListView;
	private UserRatingAdapter adapter;
	private List<Rating> mlist;
	private int page = 1;
	private int pageCount = 0;
	private SessionManager sm;
	private boolean isloading = false;
	private boolean showlast = false;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratinglist);
		init();
		sm = SessionManager.getInstance();
		uid = getIntent().getStringExtra("uid");
		ListView = (MyListView) findViewById(R.id.lv_rating_listview);
		adapter = new UserRatingAdapter(this);
		ListView.setAdapter(adapter);
		ListView.setOnRefreshListener(new com.jm.view.MyListView.OnRefreshListener() {

			public void onRefresh() {
				adapter.clear();
				page = 1;
				pageCount = 0;
				new GetRatingListTask().execute();
			}
		});
		new GetRatingListTask().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "评价列表页面");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "评价列表页面");
	}

	private void init() {

		findViewById(R.id.btn_leftTop).setOnClickListener(this);

	}

	/*
	 * 读取动态列表
	 */
	class GetRatingListTask extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mlist == null || mlist.size() == 0) {
				adapter.setProgress(true);
			}
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn.executeAndParse(Constant.URN_YUYUE_COMMENT_INFO
					+ "&page=" + page, getInfoInqVal());

		}

		protected void onPostExecute(Response result) {
			ListView.onRefreshComplete();
			isloading = false;
			adapter.setProgress(false);
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				mlist = result.getList("assess_list", new Rating());
				if (mlist == null || mlist.size() == 0) {
					TispToastFactory.getToast(RatingListUI.this, "暂无数据").show();
					return;
				}
				pageCount = Integer.parseInt(result.getString("page_count"));
				showlast = false;
				adapter.appendRatingList(mlist);
				adapter.notifyDataSetChanged();
			} else {
				TispToastFactory.getToast(RatingListUI.this, result.getMsg())
						.show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:
			this.finish();
			break;

		}
	}

	@Override
	public void scroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (showlast) {
			return;
		}
		if (totalItemCount - (firstVisibleItem + visibleItemCount) <= 6) {

			LogUtil.d("正在显示" + page + "页," + "总页数为" + pageCount + "页");

			if (page < pageCount) {
				if (!isloading) {
					LogUtil.d("即将加载" + ++page + "页");
					isloading = true;
					new GetRatingListTask().execute();
				}
			}
		}

	}

	@Override
	public void onScrollChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
