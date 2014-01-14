package com.jm.sort;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jm.entity.QuestionChat;
import com.jm.fxw.R;
import com.jm.util.LogUtil;

public class ChatAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<QuestionChat> mlist;

	private int ScreenWidth;

	public ChatAdapter(Context context, int ScreenWidth) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<QuestionChat>();
		this.ScreenWidth = ScreenWidth;
	}

	public void appendChatList(List<QuestionChat> list) {
		if (list == null) {
			return;
		}
		if (mlist == null) {
			mlist = new ArrayList<QuestionChat>();
		}
		for (int i = 0; i < list.size(); i++) {
			mlist.add(list.get(i));
		}
	}

	public List<QuestionChat> getChatList() {
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
		ChatItem view = (ChatItem) inflater.inflate(R.layout.quesionchatlist,
				null);
		view.setChatsAndScreenWidth(mlist.get(position), ScreenWidth);
		view.initView();
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
	}
}