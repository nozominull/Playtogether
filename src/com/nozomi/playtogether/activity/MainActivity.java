package com.nozomi.playtogether.activity;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.LinearLayout;

import com.nozomi.playtogether.R;
import com.nozomi.playtogether.fragment.CreateEventFragment;
import com.nozomi.playtogether.fragment.EventFragment;
import com.nozomi.playtogether.fragment.EventdetailFragment;
import com.nozomi.playtogether.fragment.SetPositionFragment;
import com.nozomi.playtogether.fragment.MarkFragment;
import com.nozomi.playtogether.fragment.MineFragment;
import com.nozomi.playtogether.fragment.ReviewFragment;
import com.nozomi.playtogether.fragment.ShowPositionFragment;
import com.nozomi.view.BackFragment;
import com.nozomi.view.ProgressLayoutView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private LinearLayout eventLayoutView = null;
	private ImageView eventImageView = null;
	private LinearLayout markLayoutView = null;
	private ImageView markImageView = null;
	private LinearLayout mineLayoutView = null;
	private ImageView mineImageView = null;
	private LinearLayout createEventLayoutView = null;
	private ImageView createEventImageView = null;
	private ProgressLayoutView progressLayoutView = null;

	private FragmentManager fm = null;
	private BackFragment currentFragment = null;
	private EventFragment eventFragment = null;
	private EventdetailFragment eventdetailFragment = null;
	private ReviewFragment reviewFragment = null;
	private MarkFragment markFragment = null;
	private MineFragment mineFragment = null;
	private CreateEventFragment createEventFragment = null;
	private SetPositionFragment setPositionFragment = null;
	private ShowPositionFragment showPositionFragment = null;

	public enum FragmentName {
		EventFragment, EventdetailFragment, MarkFragment, MineFragment, ReviewFragment, CreateEventFragment, SetPositionFragment,ShowPositionFragment
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		fm = getSupportFragmentManager();

		initView();

	}

	private void initView() {

		eventLayoutView = (LinearLayout) findViewById(R.id.event_layout);
		eventLayoutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFrament(FragmentName.EventFragment, null);
			}
		});
		eventImageView = (ImageView) findViewById(R.id.event_image);

		markLayoutView = (LinearLayout) findViewById(R.id.mark_layout);
		markLayoutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFrament(FragmentName.MarkFragment, null);
			}
		});
		markImageView = (ImageView) findViewById(R.id.mark_image);

		mineLayoutView = (LinearLayout) findViewById(R.id.mine_layout);
		mineLayoutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFrament(FragmentName.MineFragment, null);
			}
		});
		mineImageView = (ImageView) findViewById(R.id.mine_image);

		createEventLayoutView = (LinearLayout) findViewById(R.id.create_event_layout);
		createEventLayoutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFrament(FragmentName.CreateEventFragment, null);
			}
		});
		createEventImageView = (ImageView) findViewById(R.id.create_event_image);

		progressLayoutView = (ProgressLayoutView) findViewById(R.id.progress_layout);

		setFrament(FragmentName.EventFragment, null);
	}

	public void setFrament(FragmentName fragmentName, Bundle bundle) {
		BackFragment lastFragment = currentFragment;

		if (fragmentName == FragmentName.EventdetailFragment) {
			if (eventdetailFragment == null) {
				eventdetailFragment = new EventdetailFragment();
			}
			currentFragment = eventdetailFragment;
		} else if (fragmentName == FragmentName.ReviewFragment) {
			if (reviewFragment == null) {
				reviewFragment = new ReviewFragment();
			}
			currentFragment = reviewFragment;
		} else if (fragmentName == FragmentName.SetPositionFragment) {
			if (setPositionFragment == null) {
				setPositionFragment = new SetPositionFragment();
			}
			currentFragment = setPositionFragment;
		} else if (fragmentName == FragmentName.ShowPositionFragment) {
			if (showPositionFragment == null) {
				showPositionFragment = new ShowPositionFragment();
			}
			currentFragment = showPositionFragment;
		} else {

			if (fragmentName == FragmentName.EventFragment) {
				if (eventFragment == null) {
					eventFragment = new EventFragment();
				}
				currentFragment = eventFragment;

				eventLayoutView.setBackgroundColor(getResources().getColor(
						R.color.topic_bar));
				eventImageView
						.setBackgroundResource(R.drawable.main_event_true);
			} else {
				eventLayoutView.setBackgroundColor(Color.TRANSPARENT);
				eventImageView
						.setBackgroundResource(R.drawable.main_event_false);
			}

			if (fragmentName == FragmentName.MarkFragment) {
				if (markFragment == null) {
					markFragment = new MarkFragment();
				}
				currentFragment = markFragment;

				markLayoutView.setBackgroundColor(getResources().getColor(
						R.color.topic_bar));
				markImageView.setBackgroundResource(R.drawable.main_mark_true);
			} else {
				markLayoutView.setBackgroundColor(Color.TRANSPARENT);
				markImageView.setBackgroundResource(R.drawable.main_mark_false);
			}

			if (fragmentName == FragmentName.MineFragment) {
				if (mineFragment == null) {
					mineFragment = new MineFragment();
				}
				currentFragment = mineFragment;

				mineLayoutView.setBackgroundColor(getResources().getColor(
						R.color.topic_bar));
				mineImageView.setBackgroundResource(R.drawable.main_mine_true);
			} else {
				mineLayoutView.setBackgroundColor(Color.TRANSPARENT);
				mineImageView.setBackgroundResource(R.drawable.main_mine_false);
			}

			if (fragmentName == FragmentName.CreateEventFragment) {
				if (createEventFragment == null) {
					createEventFragment = new CreateEventFragment();
				}
				currentFragment = createEventFragment;

				createEventLayoutView.setBackgroundColor(getResources().getColor(
						R.color.topic_bar));
				createEventImageView
						.setBackgroundResource(R.drawable.main_create_event_true);
			} else {
				createEventLayoutView.setBackgroundColor(Color.TRANSPARENT);
				createEventImageView
						.setBackgroundResource(R.drawable.main_create_event_false);
			}

		}

		currentFragment.setBundle(bundle);
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.main_content, currentFragment);
		ft.commit();

		if (currentFragment != lastFragment) {
			progressLayoutView.increaseProgressRef(true);
		}
	}

	@Override
	public void onBackPressed() {
		currentFragment.onBackPressed();
	}

	public void decreaseProgressRef(boolean isVisiable) {
		progressLayoutView.decreaseProgressRef(true);
	}

}
