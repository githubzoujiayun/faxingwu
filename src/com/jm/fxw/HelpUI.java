package com.jm.fxw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cnzz.mobile.android.sdk.MobileProbe;

public class HelpUI extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		findViewById(R.id.btn_leftTop).setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobileProbe.onResume(this, "∞Ô÷˙“≥√Ê");
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobileProbe.onPause(this, "∞Ô÷˙“≥√Ê");
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
