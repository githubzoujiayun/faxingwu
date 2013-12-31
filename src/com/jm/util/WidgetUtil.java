package com.jm.util;

import java.util.List;

import android.R.string;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jm.finals.Constant;
import com.jm.fxw.R;

/*
 * I feel good when I wrote this class
 */
public class WidgetUtil {

	// 定义未选中时的颜色
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

	public static boolean CheckAllEditTextListValue(List<EditText> list) {

		for (EditText editText : list) {
			if (editText.getText().toString().trim().equals("")) {
				return false;
			}
		}
		return true;
	}

	public static boolean CheckAllEditTextValue(EditText editText) {
		if (editText.getText().toString().trim() == null
				|| editText.getText().toString().trim().equals("")) {
			editText.requestFocus();
			editText.requestFocusFromTouch();
			return false;
		}
		return true;
	}
}
