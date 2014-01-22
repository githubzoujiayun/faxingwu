package com.jm.fxw;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HelpUI extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		findViewById(R.id.btn_leftTop).setOnClickListener(this);
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
		finish();
	}
}
