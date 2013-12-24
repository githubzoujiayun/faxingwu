package com.jm.sort;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jm.entity.QuestionChat;
import com.jm.fxw.HisInfoUI;
import com.jm.fxw.R;
import com.jm.session.SessionManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuesionChatItem extends LinearLayout implements OnClickListener {

	private TextView time;
	private Context context;
	private ImageView imgFriend;
	private ImageView imgMy;

	private LinearLayout mylin, hislin, lin_voice, lin_text;
	private RelativeLayout chat_layout;
	private ImageView image;
	private TextView text;
	public QuestionChat u;

	public QuesionChatItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setChats(QuestionChat u) {
		this.u = u;
	}

	public void initView() {
		time = (TextView) findViewById(R.id.time);
		lin_voice = (LinearLayout) findViewById(R.id.lin_voice);
		chat_layout = (RelativeLayout) findViewById(R.id.team_singlechat_id_listiteam);
		image = (ImageView) findViewById(R.id.team_singlechat_id_listiteam_headicon);
		lin_text = (LinearLayout) findViewById(R.id.team_singlechat_id_listiteam_message);

		text = (TextView) findViewById(R.id.tv_message);
		mylin = (LinearLayout) findViewById(R.id.mylin);
		hislin = (LinearLayout) findViewById(R.id.hislin);
		imgFriend = (ImageView) findViewById(R.id.hisimage);
		imgFriend.setOnClickListener(this);
		imgMy = (ImageView) findViewById(R.id.myimage);

		fillView();
	}

	public void fillView() {
		RelativeLayout.LayoutParams rl_chat_left = ((RelativeLayout.LayoutParams) chat_layout
				.getLayoutParams());

		RelativeLayout.LayoutParams rl_tv_msg_left = ((RelativeLayout.LayoutParams) lin_text
				.getLayoutParams());

		RelativeLayout.LayoutParams rl_iv_headicon_left = ((RelativeLayout.LayoutParams) image
				.getLayoutParams());

		if (u == null) {
			return;
		}
		// 这是我收到的信息
		if (!u.getSender().equals(SessionManager.getInstance().getUserId())) {
			lin_voice.setGravity(Gravity.LEFT);
			rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT, -1);
			rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			rl_tv_msg_left.addRule(RelativeLayout.RIGHT_OF,
					R.id.team_singlechat_id_listiteam_headicon);
			rl_tv_msg_left.addRule(RelativeLayout.LEFT_OF, 0);

			lin_text.setBackgroundResource(R.drawable.balloon_l_selector);
			ImageLoader.getInstance().displayImage(u.getSenderpic(), imgFriend);
			lin_text.setGravity(Gravity.LEFT);
			imgMy.setVisibility(View.GONE);
			mylin.setVisibility(View.GONE);
			lin_text.setBackgroundResource(R.drawable.balloon_l_selector);
			lin_text.setGravity(Gravity.LEFT);

		}
		// 这是我发出的信息
		else {
			rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
			rl_chat_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
			rl_iv_headicon_left.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			rl_tv_msg_left.addRule(RelativeLayout.RIGHT_OF, 0);
			rl_tv_msg_left.addRule(RelativeLayout.LEFT_OF,
					R.id.team_singlechat_id_listiteam_headicon);

			lin_text.setBackgroundResource(R.drawable.balloon_r_selector);
			lin_text.setGravity(Gravity.RIGHT);
			imgFriend.setVisibility(View.GONE);
			hislin.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(u.getMypic(), imgMy);
			lin_text.setBackgroundResource(R.drawable.balloon_r_selector);
			lin_text.setGravity(Gravity.RIGHT);
			imgFriend.setVisibility(View.GONE);
			hislin.setVisibility(View.GONE);
		}
		if (!u.getPic().equals("")) {
			findViewById(R.id.chatpicture).setVisibility(View.VISIBLE);

			ImageLoader.getInstance().displayImage(u.getPic(),
					(ImageView) findViewById(R.id.chatpicture));
		}
		if (u.getMessage().equals("")) {
			text.setVisibility(View.GONE);
		} else {
			text.setText(u.getMessage());
		}
		time.setText(u.getDatetime());

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if (!u.getSender().equals(SessionManager.getInstance().getUserId())) {
			intent = new Intent(context, HisInfoUI.class);
			intent.putExtra("uid", u.getSender());
			intent.putExtra("type", u.getType());
			context.startActivity(intent);
		}

	}
}