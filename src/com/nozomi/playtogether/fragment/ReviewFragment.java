package com.nozomi.playtogether.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.ImageButton;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nozomi.Models.Review;
import com.nozomi.autohidescrollview.view.AutoHideXScrollView;
import com.nozomi.autohidescrollview.view.XScrollView.IXScrollViewListener;
import com.nozomi.playtogether.R;
import com.nozomi.playtogether.activity.MainActivity;
import com.nozomi.playtogether.activity.MainActivity.FragmentName;
import com.nozomi.util.CommDef;
import com.nozomi.util.CommUtil;
import com.nozomi.view.BackFragment;
import com.nozomi.view.BounceListView;
import com.nozomi.view.ProgressLayoutView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView.OnEditorActionListener;

public class ReviewFragment extends BackFragment {
	private AutoHideXScrollView scrollView = null;
	private TextView blankView = null;
	private ProgressLayoutView progressLayoutView = null;

	private ObjectMapper om = new ObjectMapper();
	private int userId = 0;
	private int eventId = 0;
	private ArrayList<Review> reviewArray = new ArrayList<Review>();
	private ReviewAdapter reviewAdapter = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
			Locale.getDefault());

	@Override
	public void setBundle(Bundle bundle) {
		eventId = bundle.getInt("event_id");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.review_fragment, container, false);

		SharedPreferences sp = getSharedPreferences("playtogether",
				Context.MODE_PRIVATE);
		userId = sp.getInt(CommDef.SP_USER_ID, 0);

		initView(view);
		loadDataFromServer(true, true);

		return view;
	}

	private void initView(View view) {
		RelativeLayout topicBarView = (RelativeLayout) view
				.findViewById(R.id.topic_bar);
		LinearLayout footerBarView = (LinearLayout) view
				.findViewById(R.id.foot_bar);
		scrollView = (AutoHideXScrollView) view.findViewById(R.id.scroll);
		scrollView.setHeaderAndFooter(topicBarView, footerBarView);
		scrollView.setXScrollViewListener(new IXScrollViewListener() {

			@Override
			public void onRefresh() {
				loadDataFromServer(false, true);
			}

			@Override
			public void onLoadMore() {
				loadDataFromServer(false, false);
			}
		});

		ImageButton backView = (ImageButton) view.findViewById(R.id.back);
		backView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		BounceListView reviewListView = (BounceListView) view
				.findViewById(R.id.review_list);
		reviewAdapter = new ReviewAdapter(getSupportActivity());
		reviewListView.setAdapter(reviewAdapter);

		blankView = (TextView) view.findViewById(R.id.blank);

		if (reviewArray.isEmpty()) {
			scrollView.setVisibility(View.INVISIBLE);
		} else {
			scrollView.setVisibility(View.VISIBLE);
		}

		final EditText contentView = (EditText) view.findViewById(R.id.content);
		contentView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(android.widget.TextView v,
					int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					String content = contentView.getText().toString();
					if (content.equals("")) {
						CommUtil.makeToast(getSupportActivity(), "说点什么…");
					} else {
						progressLayoutView.increaseProgressRef(true);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("user_id", userId);
						map.put("event_id", eventId);
						map.put("content", content);
						map.put("count", 10);
						CommUtil.makeRequest(getSupportActivity(),
								CommDef.ADD_REVIEW, map,
								new Listener<JsonNode>() {

									@Override
									public void onResponse(JsonNode response) {
										int retCode = response.findValue(
												"retCode").asInt();
										if (retCode == 0) {
											CommUtil.makeToast(
													getSupportActivity(),
													"评论成功");
											contentView.setText("");
											try {
												Review[] resultArray = om
														.readValue(
																response.findValue(
																		"content")
																		.toString(),
																Review[].class);
												reviewArray.clear();
												for (Review review : resultArray) {
													reviewArray.add(review);
												}
												reviewAdapter
														.notifyDataSetChanged();
												scrollView.smoothScrollTo(0, 0);
												scrollView
														.showHeaderAndFooter();

												if (reviewArray.isEmpty()) {
													scrollView
															.setVisibility(View.INVISIBLE);
													blankView
															.setVisibility(View.VISIBLE);
												} else {
													scrollView
															.setVisibility(View.VISIBLE);
													blankView
															.setVisibility(View.GONE);
												}

											} catch (Exception e) {
												CommUtil.makeToast(
														getSupportActivity(),
														"服务器异常");
											}
										} else if (retCode == -1) {
											CommUtil.makeToast(
													getSupportActivity(),
													"网络连接失败");
										} else if (retCode == 1) {
											CommUtil.makeToast(
													getSupportActivity(),
													"服务器异常");
										}
										progressLayoutView
												.decreaseProgressRef(true);
									}
								});
					}
				}
				return false;
			}
		});

		progressLayoutView = (ProgressLayoutView) view
				.findViewById(R.id.progress_layout);

	}

	private void loadDataFromServer(final boolean isFirst,
			final boolean isHeader) {
		progressLayoutView.increaseProgressRef(isFirst);
		int offset = 0;
		if (!isHeader) {
			offset = reviewArray.size();
		}

		CommUtil.makeRequest(getSupportActivity(),
				String.format(CommDef.GET_REVIEW, eventId, offset, 10),
				new Listener<JsonNode>() {
					@Override
					public void onResponse(JsonNode response) {
						int retCode = response.findValue("retCode").asInt();
						if (retCode == 0) {
							try {
								JsonNode contentJn = response
										.findValue("content");
								Review[] resultArray = om.readValue(
										contentJn.toString(), Review[].class);
								if (isHeader) {
									reviewArray.clear();
								}
								for (Review review : resultArray) {
									reviewArray.add(review);
								}
								reviewAdapter.notifyDataSetChanged();
								if (isHeader) {
									scrollView.smoothScrollTo(0, 0);
									scrollView.showHeaderAndFooter();
								}

								if (reviewArray.isEmpty()) {
									scrollView.setVisibility(View.INVISIBLE);
									blankView.setVisibility(View.VISIBLE);
								} else {
									scrollView.setVisibility(View.VISIBLE);
									blankView.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								CommUtil.makeToast(getSupportActivity(),
										"服务器异常");
							}
						} else if (retCode == -1) {
							CommUtil.makeToast(getSupportActivity(), "网络连接失败");
						}
						if (isHeader) {
							scrollView.stopRefresh();
						} else {
							scrollView.stopLoadMore();
						}
						progressLayoutView.decreaseProgressRef(isFirst);
					}
				});
	}

	@Override
	public void onBackPressed() {
		((MainActivity) getSupportActivity()).setFrament(
				FragmentName.EventdetailFragment, null);
	}

	private class ReviewAdapter extends BaseAdapter {

		private Context context;

		public ReviewAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return reviewArray.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.review, null, false);
				holder.creatorNameView = (TextView) convertView
						.findViewById(R.id.creator_name);
				holder.createTimeView = (TextView) convertView
						.findViewById(R.id.create_time);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.content);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Review review = reviewArray.get(position);
			holder.creatorNameView.setText(review.getCreatorName());
			holder.createTimeView.setText(sdf.format(new Date(review
					.getCreateTime() * 1000)));
			holder.contentView.setText(review.getContent());

			return convertView;
		}

		private class ViewHolder {
			TextView creatorNameView;
			TextView createTimeView;
			TextView contentView;
		}
	}

}
