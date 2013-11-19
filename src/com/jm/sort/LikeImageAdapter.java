package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jm.entity.Like;
import com.jm.fxw.R;

public class LikeImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Like> mlist;
	private ImageView iv_image;
	private Like like;
	private Context context;
	private FinalBitmap fb;

	public LikeImageAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<Like>();
		this.context = context;
	}

	public void setLikeList(List<Like> list) {
		if (list == null) {
			return;
		}
		mlist = list;

	}

	public List<Like> getList() {
		return mlist;
	}

	@Override
	public int getCount() {

		return mlist.size();
	}

	@Override
	public Object getItem(int position) {

		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View contentView, ViewGroup arg2) {
		LikeImageItem view = (LikeImageItem) inflater.inflate(
				R.layout.likesmallimg_grid, null);
		like = mlist.get(position);
		fb = FinalBitmap.create(context);
		fb.display((ImageView) view.findViewById(R.id.iv_UserLikePicShow),
				like.getPic());
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
	}

}
