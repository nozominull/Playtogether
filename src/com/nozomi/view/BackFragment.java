package com.nozomi.view;

import org.holoeverywhere.app.Fragment;

import com.nozomi.playtogether.activity.MainActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public abstract class BackFragment extends Fragment {
	public abstract void onBackPressed();

	public void setBundle(Bundle bundle) {
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim;
		if (enter) {
			anim = AnimationUtils.loadAnimation(getActivity(),
					android.R.anim.slide_in_left);
		} else {
			anim = AnimationUtils.loadAnimation(getActivity(),
					android.R.anim.slide_out_right);
		}

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationStart(Animation animation) {
				((MainActivity) getSupportActivity()).decreaseProgressRef(true);
			}
		});

		return anim;
	}

}
