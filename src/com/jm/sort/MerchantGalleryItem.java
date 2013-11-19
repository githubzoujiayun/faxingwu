package com.jm.sort;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jm.fxw.R;
import com.jm.sort.MerchantGalleryAdapter.ViewHolder;
import com.jm.util.LogUtil;

public class MerchantGalleryItem extends LinearLayout {
	private Context context;
	private String url = "";

	private FinalBitmap fb;
	private int position;

	public MerchantGalleryItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setUrl(String url) {
		this.url = url;

	}

	public void initView(ViewHolder holder) {
		holder.view = (ImageView) findViewById(R.id.bimg_view);
	}

	public void fillView(ViewHolder holder, Bitmap bitmap) {
		if ("".equals(url)) {
			return;
		}
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float size = display.getHeight();
		fb = FinalBitmap.create(context);
		// Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.empty_photo);
		fb.display(holder.view, url, (int) size, (int) size, bitmap, bitmap);
		//
		// Bitmap bitmap = ImageUtil.getCachedBitmap(url);
		// if (bitmap != null && !bitmap.isRecycled()) {
		// if (bitmap.getWidth() < winWidth) {
		// float scan = winWidth / (float) bitmap.getWidth();
		// Matrix matrix = new Matrix();
		// matrix.postScale(scan, scan);
		// bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		// bitmap.getHeight(), matrix, true);
		// }
		// holder.view.setImageBitmap(bitmap);
		// cacheBitmap.addBitmap(position, bitmap);
		// } else {
		// holder.view.setImageBitmap(null);
		// }

	}
}
