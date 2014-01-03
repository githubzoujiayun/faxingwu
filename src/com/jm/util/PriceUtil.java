package com.jm.util;

import java.math.BigDecimal;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jm.fxw.R;

public class PriceUtil {
	private static final int DEF_DIV_SCALE = 2;

	public static void setRealPrice(EditText et_serprice, Spinner sp_discount,
			TextView tv_realprice) {
		try {

			double price = Double.valueOf((et_serprice).getText().toString()
					.trim());
			double discount = Double.valueOf((sp_discount).getSelectedItem()
					.toString().trim());
			tv_realprice.setText(mul(div(price, 10.0), discount) + "");
		} catch (Exception e) {
			tv_realprice.setText("-");
		}
	}

	public static String getRealPrice(String et_serprice, String str_discount) {
		try {

			double price = Double.valueOf(et_serprice);
			double discount = Double.valueOf(str_discount);
			return mul(div(price, 10.0), discount) + "";
		} catch (Exception e) {
			return "-";
		}
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	private static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (b1.multiply(b2)).doubleValue();
	}
}
