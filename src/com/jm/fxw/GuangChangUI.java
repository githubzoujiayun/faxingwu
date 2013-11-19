package com.jm.fxw;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnzz.mobile.android.sdk.MobileProbe;
import com.jm.session.SessionManager;
import com.jm.util.StartActivityContController;

public class GuangChangUI extends FinalActivity {
	private SharedPreferences share;
	@ViewInject(id = R.id.lin_guangchang, click = "Click")
	LinearLayout lin_guangchang;
	@ViewInject(id = R.id.lin_zhaofaxing, click = "Click")
	LinearLayout lin_zhaofaxing;
	@ViewInject(id = R.id.lin_faxingshi, click = "Click")
	LinearLayout lin_faxingshi;
	@ViewInject(id = R.id.lin_wode, click = "Click")
	LinearLayout lin_wode;
	@ViewInject(id = R.id.lin_dongtai, click = "Click")
	LinearLayout lin_dongtai;
	@ViewInject(id = R.id.lin_tongcheng, click = "Click")
	LinearLayout lin_zhoubian;
	@ViewInject(id = R.id.lin_chakanjiange, click = "Click")
	LinearLayout lin_chakanjiange;
	@ViewInject(id = R.id.lin_qingbaozhan, click = "Click")
	LinearLayout lin_qingbaozhan;
	@ViewInject(id = R.id.lin_question, click = "Click")
	LinearLayout lin_question;
	@ViewInject(id = R.id.lin_redianhuati, click = "Click")
	LinearLayout lin_redianhuati;
	@ViewInject(id = R.id.lin_zhaopinxinxi, click = "Click")
	LinearLayout lin_zhaopinxinxi;
	private SessionManager sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guangchang);
		init();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobileProbe.onPause(this, "广场页面");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileProbe.onResume(this, "广场页面");
		init();
	}

	private void setInco() {
		// TODO Auto-generated method stub
		findViewById(R.id.iv_guangchang).setBackgroundResource(
				R.drawable.guangchang1);
		((TextView) findViewById(R.id.tv_guangchang)).setTextColor(Color
				.parseColor("#f01c61"));
	}

	private void init() {
		sm = SessionManager.getInstance();
		setInco();

	}

	public void Click(View v) {
		switch (v.getId()) {

		case R.id.lin_redianhuati:
			// changeCondition("4", v);
			StartActivityContController.goPage(this, JiShuHuaTiUI.class, true);
			break;
		case R.id.lin_zhaopinxinxi:
			// changeCondition("4", v);
			StartActivityContController.goPage(this, JobListUI.class, true);
			break;
		case R.id.lin_guangchang:
			StartActivityContController.goPage(GuangChangUI.this,
					GuangChangUI.class, false);
			break;
		case R.id.lin_zhaofaxing:
			StartActivityContController.goPage(GuangChangUI.this,
					ZhaofaxingUI.class, false);
			break;
		case R.id.lin_faxingshi:
			StartActivityContController.goPage(GuangChangUI.this,
					FaxingshiUI.class, false);
			break;
		case R.id.lin_wode:
			StartActivityContController.goPage(GuangChangUI.this,
					StartActivityContController.wode);
			break;
		case R.id.lin_dongtai:
			StartActivityContController.goPage(GuangChangUI.this,
					DongTaiUI.class, false);
			break;
		case R.id.lin_tongcheng:
			StartActivityContController.goPage(GuangChangUI.this,
					TongChengUI.class, false);
			break;
		case R.id.lin_chakanjiange:
			StartActivityContController.goPage(GuangChangUI.this,
					PriceListUI.class, false);
			break;

		case R.id.lin_qingbaozhan:
			StartActivityContController.goPage(GuangChangUI.this, 104);
			break;
		case R.id.lin_question:
			StartActivityContController.goPage(GuangChangUI.this,
					QuestionUI.class, true);
			break;

		}

	}

}
