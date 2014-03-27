package com.cky.reactiontest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cky.services.MainServices;

/**
 * 主Activity
 * 
 * @author 陈恺垣
 * @version 1.0
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	// 开始游戏按钮
	private Button playButton = null;
	// 排行榜按钮
	private Button rankingButton = null;
	// 用于标记是否点击
	private int clickFlag = 0;
	// 用于判断是否能点击
	private boolean enableClick = true;
	// 记录开始点击时系统时间
	private long beginTime = 0;
	// 记录结束点击时系统时间
	private long endTime = 0;
	// 主要的业务逻辑服务
	private MainServices mainServices = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取控件
		playButton = (Button) findViewById(R.id.paly);
		rankingButton = (Button) findViewById(R.id.ranking);
		// 获取主要业务逻辑服务
		mainServices = new MainServices(this);

		// 创建并启动一个线程，在允许的时候更改按钮背景色
		changeButtonBackground(playButton);
	}

	/**
	 * 创建并启动一个线程，在允许的时候更改按钮背景色。
	 */
	private void changeButtonBackground(final Button playButton) {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 100:
					// 记录开始时间
					beginTime = System.currentTimeMillis();
					// 将按钮背景色设置成蓝色此时用户应再次点击
					playButton.setBackgroundColor(Color.BLUE);
					playButton.setText("快点我！！！");
					break;
				default:
					break;
				}
			}
		};

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					// 不能点击后才开始运行线程逻辑。
					while (enableClick == false) {
						try {
							// 使按钮变色时间在1～5秒之间随机，增加可玩性
							Thread.sleep((int) (Math.random() * 3000 + 1));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							// 用消息机制控制按钮背景
							Message msg = new Message();
							msg.what = 100;
							handler.sendMessage(msg);
							enableClick = true;
						}
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * 重写OnClickListener接口中的onClick(View v)方法，实现对按钮的点击响应。
	 */
	@Override
	public void onClick(View v) {
		// 获取主要业务逻辑服务

		switch (v.getId()) {
		case R.id.paly:
			calcTime();
			break;
		case R.id.ranking:
			showRanking();
			break;
		default:
		}
	}

	/**
	 * 计算两次点击的时间间隔，即反应时间。
	 */
	private void calcTime() {
		// 当changeButtonBackgroun(final Button playButton)方法中的线程休眠的时候对按钮的点击是无效的。
		if (enableClick == true) {
			// 当点击有效后clickFlag自增长1。clickFlag为奇数表示本次点为开始点击，为偶数表示本次点击为结束点击。
			clickFlag++;
			if (clickFlag % 2 == 1) {
				playButton.setText(" ");
				// 点击按钮，开始后结束前不能再次点击
				enableClick = false;
			} else if (clickFlag % 2 == 0) {
				// 记录第二次点击时间并设置文字提示与背景色
				endTime = System.currentTimeMillis();
				// 反应时间，单位是毫秒
				long reactionTime = endTime - beginTime;
				// 比较反应时间，如果更优秀则跳转到排行榜并提示用户
				// 反应时间低于200毫秒都是作弊好么，你以为是神哦！
				if (reactionTime > 200 && mainServices.betterResult(reactionTime)) {
					showRanking(reactionTime);
				}
				playButton.setText("反应时间：" + reactionTime + " ms" + "\n再次点击开始");
				playButton.setBackgroundColor(Color.RED);
			}
		}
	}

	/**
	 * 不带参数的启功排行榜，表示当前记录不存在或者比已经存在的所有记录都慢。
	 */
	private void showRanking() {
		Intent intent = new Intent(this, RankingActivity.class);
		startActivity(intent);
	}

	/**
	 * 带参数的启功排行榜，表示当前记录比已经存在的所有记录中的某一条要更快。
	 * 
	 * @param Time
	 *            反应时间
	 */
	private void showRanking(long Time) {
		Intent intent = new Intent(this, RankingActivity.class);
		intent.putExtra("time", Time + "");
		startActivity(intent);
	}

}
