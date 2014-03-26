package com.nozomi.playtogether.activity;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.SharedPreferences;

import com.nozomi.playtogether.R;
import com.nozomi.util.CommDef;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		initView();
	}

	private void initView() {
		ImageView logoView = (ImageView) findViewById(R.id.logo);
		Animation animation = new AlphaAnimation(0, 1);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				SharedPreferences sp = getSharedPreferences("playtogether",
						Context.MODE_PRIVATE);
				int userId = sp.getInt(CommDef.SP_USER_ID, 0);
				if (userId == 0) {
					Intent intent = new Intent(SplashActivity.this,
							LoginActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.grow_fade_in,
							R.anim.shrink_fade_out);
					finish();
				} else {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);

					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.grow_fade_in,
							R.anim.shrink_fade_out);
				}

			}
		});
		logoView.startAnimation(animation);

	}

	@Override
	public void onBackPressed() {

	}
}
