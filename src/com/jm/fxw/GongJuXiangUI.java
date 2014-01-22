package com.jm.fxw;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mobstat.StatService;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.jm.data.DatabaseHelper;
import com.jm.util.StartActivityContController;

public class GongJuXiangUI extends OrmLiteBaseActivity<DatabaseHelper>
		implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gongjuxiang);
		findViewById(R.id.btn_leftTop).setOnClickListener(this);
		findViewById(R.id.lin_qingbao).setOnClickListener(this);
		findViewById(R.id.lin_zhaopin).setOnClickListener(this);
		findViewById(R.id.lin_zhuanrang).setOnClickListener(this);
		findViewById(R.id.lin_jishuahuati).setOnClickListener(this);

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftTop:

			finish();
			break;
		case R.id.lin_qingbao:

			StartActivityContController.goPage(this, 104);
			break;
		case R.id.lin_zhaopin:

			StartActivityContController.goPage(this, JobListUI.class, true);
			break;
		case R.id.lin_zhuanrang:
			StartActivityContController.goPage(this, ZhuanRangListUI.class,
					true);
			break;
		case R.id.lin_jishuahuati:
			StartActivityContController.goPage(this, JiShuHuaTiUI.class, true);
			break;

		}
	}
}
