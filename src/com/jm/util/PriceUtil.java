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
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ������������
	 */
	private static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @param scale
	 *            ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 * @return ������������
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
	 * �ṩ��ȷ�ĳ˷����㡣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ���������Ļ�
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (b1.multiply(b2)).doubleValue();
	}
}
