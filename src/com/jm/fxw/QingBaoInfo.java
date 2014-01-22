package com.jm.fxw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.jm.connection.Connection;
import com.jm.connection.Response;
import com.jm.entity.NewsList;
import com.jm.finals.Constant;
import com.jm.sort.NewsListAdapter;
import com.jm.util.LogUtil;
import com.jm.util.TispToastFactory;

public class QingBaoInfo extends FinalActivity implements OnClickListener {
	private List<NewsList> mlist = new ArrayList<NewsList>();
	private NewsListAdapter adapter;
	private ListView ListView;
	private String nid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newslist);

		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		ListView = (ListView) findViewById(R.id.yuyue_list);
		View view = LayoutInflater.from(this).inflate(R.layout.newslisthead,
				null);
		ListView.addHeaderView(view);
		ListView.setDivider(null);
		adapter = new NewsListAdapter(this);
		ListView.setAdapter(adapter);
		nid = getIntent().getStringExtra("nid");
		if ("".equals(nid)) {
			LogUtil.e("传入数据有误，页面关闭");
			finish();
		}

		new getNewListInfo().execute();
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

	class getNewListInfo extends AsyncTask<String, Integer, Response> {

		@Override
		protected void onPreExecute() {
		}

		protected Map<String, Object> getInfoInqVal() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("news_id", nid);
			return map;
		}

		@Override
		protected Response doInBackground(String... params) {
			Connection conn = ((ClientApp) getApplication()).getConnection();
			return conn
					.executeAndParse(Constant.URN_NEWS_INFO, getInfoInqVal());
		}

		protected void onPostExecute(Response result) {
			if (result == null) {
				return;
			}
			if (result.isSuccessful()) {
				((TextView) findViewById(R.id.tv_title)).setText(result
						.getString("title"));

				((TextView) findViewById(R.id.tv_time)).setText(result
						.getString("add_time"));
				if (!"".equals(result.getString("content_list"))) {
					mlist = result.getList("content_list", new NewsList());
					adapter.setNewsListList(mlist);
					adapter.notifyDataSetChanged();
				} else {
					TispToastFactory
							.getToast(QingBaoInfo.this, result.getMsg()).show();
				}
			} else {
				TispToastFactory.getToast(QingBaoInfo.this, result.getMsg())
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

}
