package com.nozomi.playtogether.activity;

import java.util.HashMap;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.ImageButton;
import org.holoeverywhere.widget.RadioButton;

import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.JsonNode;
import com.nozomi.playtogether.R;
import com.nozomi.util.CommDef;
import com.nozomi.util.CommUtil;
import com.nozomi.view.ProgressLayoutView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class RegisterActivity extends Activity {
	private ProgressLayoutView progressLayoutView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		initView();
	}

	private void initView() {
		ImageButton backView = (ImageButton) findViewById(R.id.back);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		final EditText nameView = (EditText) findViewById(R.id.name);
		final EditText passwordView = (EditText) findViewById(R.id.password);
		final EditText passwordAgainView = (EditText) findViewById(R.id.password_again);

		final RadioGroup sexGroupView = (RadioGroup) findViewById(R.id.sex_group);
		final RadioButton maleView = (RadioButton) findViewById(R.id.male);
		final RadioButton femaleView = (RadioButton) findViewById(R.id.female);
		final RadioButton ryView = (RadioButton) findViewById(R.id.ry);

		Button registerView = (Button) findViewById(R.id.register);

		registerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameView.getText().toString().trim();
				if (name.equals("")) {
					CommUtil.makeToast(RegisterActivity.this, "请输入用户名");
					return;
				}
				String password = passwordView.getText().toString().trim();
				if (password.equals("")) {
					CommUtil.makeToast(RegisterActivity.this, "请输入密码");
					return;
				}
				String passwordAgain = passwordAgainView.getText().toString()
						.trim();
				if (!passwordAgain.equals(password)) {
					CommUtil.makeToast(RegisterActivity.this, "密码不一致");
					return;
				}
				progressLayoutView.increaseProgressRef(true);
				password = CommUtil.md5(password);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("password", password);
				if (sexGroupView.getCheckedRadioButtonId() == maleView.getId()) {
					map.put("sex", 1);
				} else if (sexGroupView.getCheckedRadioButtonId() == femaleView
						.getId()) {
					map.put("sex", 0);
				} else if (sexGroupView.getCheckedRadioButtonId() == ryView
						.getId()) {
					map.put("sex", 2);
				}
				CommUtil.makeRequest(RegisterActivity.this, CommDef.REGISTER,
						map, new Listener<JsonNode>() {
							@Override
							public void onResponse(JsonNode response) {
								int retCode = response.findValue("retCode")
										.asInt();
								if (retCode == 0) {
									int id = response.findValue("id").asInt();
									SharedPreferences sp = getSharedPreferences(
											"playtogether",
											Context.MODE_PRIVATE);
									Editor editor = sp.edit();
									editor.putInt(CommDef.SP_USER_ID, id);
									editor.commit();
									CommUtil.makeToast(RegisterActivity.this,
											"注册成功");
									Intent intent = new Intent(
											RegisterActivity.this,
											MainActivity.class);
									startActivity(intent);
									overridePendingTransition(R.anim.fade_in,
											R.anim.fade_out);
									finish();
								} else if (retCode == 1) {
									CommUtil.makeToast(RegisterActivity.this,
											"用户名已存在");
								} else if (retCode == 2) {
									CommUtil.makeToast(RegisterActivity.this,
											"服务器异常");
								}
								progressLayoutView.decreaseProgressRef(true);
							}
						});
			}
		});
		progressLayoutView = (ProgressLayoutView) findViewById(R.id.progress_layout);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();

	}

}
