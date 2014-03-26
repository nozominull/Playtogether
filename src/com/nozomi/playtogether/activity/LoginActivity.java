package com.nozomi.playtogether.activity;

import java.util.HashMap;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.ImageButton;
import org.holoeverywhere.widget.TextView;

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

public class LoginActivity extends Activity {
	private ProgressLayoutView progressLayoutView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
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

		TextView registerView = (TextView) findViewById(R.id.register);
		registerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				finish();
			}
		});

		final EditText nameView = (EditText) findViewById(R.id.name);
		final EditText passwordView = (EditText) findViewById(R.id.password);
		Button loginView = (Button) findViewById(R.id.login);

		loginView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameView.getText().toString().trim();
				if (name.equals("")) {
					CommUtil.makeToast(LoginActivity.this, "请输入用户名");
					return;
				}
				String password = passwordView.getText().toString().trim();
				if (password.equals("")) {
					CommUtil.makeToast(LoginActivity.this, "请输入密码");
					return;
				}
				progressLayoutView.increaseProgressRef(true);
				password = CommUtil.md5(password);				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("password", password);
				CommUtil.makeRequest(LoginActivity.this, CommDef.LOGIN, map,
						new Listener<JsonNode>() {
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
									CommUtil.makeToast(LoginActivity.this,
											"登陆成功");
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									overridePendingTransition(R.anim.fade_in,
											R.anim.fade_out);
									finish();
								} else if (retCode == 1) {
									CommUtil.makeToast(LoginActivity.this,
											"用户名或密码错误");
								} else if (retCode == -1) {
									CommUtil.makeToast(LoginActivity.this,
											"网络连接失败");
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
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
