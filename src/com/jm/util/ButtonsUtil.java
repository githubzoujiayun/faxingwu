package com.jm.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.jm.finals.Constant;
import com.jm.fxw.R;

@SuppressLint("ResourceAsColor")
public class ButtonsUtil {

	public static void ResetAllButton(List<View> list) {

		for (View view : list) {
			setButton(view, Constant.color_Black, R.drawable.shape_w);
		}
	}

	public static void setChangeButton(View view) {
		setButton(view, Constant.color_White, R.drawable.shape_red);
	}

	public static void setButton(View view, int color, int drawable) {
		((Button) view).setTextColor(color);
		((Button) view).setBackgroundResource(drawable);
	}
}
