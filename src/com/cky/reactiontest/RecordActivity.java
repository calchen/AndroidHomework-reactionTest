package com.cky.reactiontest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 用户输入姓名的界面
 * 
 * @author 陈恺垣
 * @version 1.0
 */
public class RecordActivity extends Activity implements OnClickListener {
	// 姓名常量，用于在Activity中传递数据时标记用户姓名
	private static final int NAME = 1;
	// 输入框，用于输入用户姓名
	private EditText enterName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		// 获取控件
		enterName = (EditText) findViewById(R.id.enterName);
		// 获取可能暂存的用户姓名，如果存在就显示在输入框中，以免去用户频繁输入姓名的烦恼。
		Intent data = getIntent();
		String possibleName = data.getStringExtra("possibleName");
		if (possibleName != null) {
			enterName.setText(possibleName);
		}
	}

	/**
	 * 重写OnClickListener接口中的onClick(View v)方法，实现对按钮的点击响应。
	 */
	@Override
	public void onClick(View v) {
		// 将用户输入的姓名传递给调用的Activity
		Intent data = new Intent();
		data.setData(Uri.parse(enterName.getText().toString()));
		setResult(NAME, data);
		finish();
	}

}
