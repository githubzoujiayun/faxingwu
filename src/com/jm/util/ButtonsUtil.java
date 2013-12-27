package com.jm.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import com.jm.finals.Constant;
import com.jm.fxw.R;

/*
 * I feel good when I wrote this class
 */
public class ButtonsUtil {

	private static int NormalButtonTextColor = Constant.color_Black;
	private static int NormalButtonBackGround = R.drawable.shape_w;

	private static int ButtonTextColor = Constant.color_White;
	private static int ButtonBackGround = R.drawable.shape_red;

	public static void ResetAllButton(List<View> list) {

		for (View view : list) {
			setButton(view, NormalButtonTextColor, NormalButtonBackGround);
		}
	}

	public static void setChangeButton(View view) {
		setButton(view, ButtonTextColor, ButtonBackGround);
	}

	public static void setButton(View view, int color, int drawable) {
		setButtonBackgroundResource(view, drawable);
		setButtonTextColor(view, color);
	}

	public static void setButtonBackgroundResource(View view, int drawable) {
		((Button) view).setBackgroundResource(drawable);
	}

	public static void setButtonTextColor(View view, int color) {
		((Button) view).setTextColor(color);
	}
}
