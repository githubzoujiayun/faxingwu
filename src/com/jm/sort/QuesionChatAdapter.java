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

public class QuesionChatAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<QuestionChat> mlist;

	public QuesionChatAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<QuestionChat>();
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
		QuesionChatItem view = (QuesionChatItem) inflater.inflate(
				R.layout.quesionchatlist, null);
		view.setChats(mlist.get(position));
		view.initView();
		return view;
	}

	public void clear() {
		if (mlist != null) {
			mlist.clear();
		}
	}
}
