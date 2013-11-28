package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.jm.fxw.R;
import com.jm.util.ImageUtil;
import com.jm.util.LogUtil;

public class SamplePagerAdapter extends PagerAdapter {
	public List<String> urls = new ArrayList<String>();

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());

		if (!urls.get(position).equals("")) {
			String b = ImageUtil.pictureStringExists(urls.get(position));
			if (!"".equals(b)) {
				LogUtil.e("Í¼Æ¬´æÔÚ b = " + b);
				LogUtil.e("Í¼Æ¬´æÔÚ position = " + position);
				photoView.setImageBitmap(BitmapFactory.decodeFile(b));
			} else {
				photoView.setImageResource(R.drawable.empty_photo);
			}
		}
		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		return photoView;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.size();
	}
}
