package com.cky.reactiontest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cky.domain.Player;
import com.cky.services.MainServices;

/**
 * 排行榜界面
 * 
 * @author 陈恺垣
 * @version 1.0
 */
public class RankingActivity extends Activity {
	// 主要的业务逻辑服务
	private MainServices mainServices = null;
	// 上中下三个TextView的集合
	private ArrayList<TextView> textViewList = null;
	// 上TextView，用于显示第一名
	private TextView first = null;
	// 中TextView，用于显示第二名
	private TextView second = null;
	// 下TextView，用于显示第三名
	private TextView third = null;
	// 用户的反应时间
	private String time = null;
	// 预测用户可能的姓名
	private static String possibleName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

		// 找到三个控件
		first = (TextView) findViewById(R.id.first);
		second = (TextView) findViewById(R.id.second);
		third = (TextView) findViewById(R.id.third);
		// 将三个控件依次添加到集合中
		textViewList = new ArrayList<TextView>();
		textViewList.add(first);
		textViewList.add(second);
		textViewList.add(third);

		// 获取主要业务逻辑服务
		mainServices = new MainServices(this);

		// 传入的数据中有time则表示为新纪录需要显示RecordActivity
		Intent intent = getIntent();
		if (intent != null) {
			time = intent.getStringExtra("time");
			if (time != null && time != "") {
				intent = new Intent(this, RecordActivity.class);
				// 如果用户姓名存在的话，传递用户姓名，方便用户保存记录
				intent.putExtra("possibleName", possibleName);

				startActivityForResult(intent, 1);
			}
		}
		// 这是排行榜的显示
		setRanking();

		// 显示一下版权（姓名班级信息）
		TextView right = (TextView) findViewById(R.id.right);
		right.setText("计科20112308039陈恺垣");
	}

	/**
	 * 根据rankings设置显示排行榜
	 */
	private void setRanking() {
		// 获取排行榜的集合，并以此展示在三个TextView控件中
		ArrayList<Player> ranking = mainServices.loadRanking();
		if (ranking != null) {
			for (int i = 0; i < ranking.size(); i++) {
				Player player = ranking.get(i);
				TextView tv = textViewList.get(i);
				String str = "名次：" + player.getRanking() + "\n姓名："
						+ player.getName() + "\n时间：" + player.getGrade()
						+ "ms\n日期：" + player.getDateTime();

				tv.setText(str);
			}
		}
	}

	/**
	 * 接受调用的Activity返回的数据。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == 1) {
				// 获取用户姓名。
				String name = data.getData().toString();
				// 将用户姓名暂存下来，方便用户下次保存记录
				possibleName = name;
				Player player = new Player(name, Long.parseLong(time),
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.format(new Date()));
				// 将新用户添加到rankings中
				mainServices.addPlayer(player);
				setRanking();
			}
		}
	}
}
