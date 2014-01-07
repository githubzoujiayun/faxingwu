package com.jm.sort;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SmallImageItem extends LinearLayout {
	public String WhosHair;

	public SmallImageItem(Context context, AttributeSet attrs, String WhosHair) {
		super(context, attrs);
		this.WhosHair = WhosHair;
	}

	public SmallImageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}
