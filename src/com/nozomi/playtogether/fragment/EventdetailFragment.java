package com.nozomi.playtogether.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ImageButton;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nozomi.Models.Eventdetail;
import com.nozomi.Models.User;
import com.nozomi.autohidescrollview.view.AutoHideScrollView;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class EventdetailFragment extends BackFragment {
	private AutoHideScrollView scrollView = null;
	private TextView creatorNameView = null;
	private TextView nameView = null;
	private TextView contentView = null;
	private TextView timeView = null;
	private TextView postionView = null;
	private TextView userCountView = null;
	private Button attendView = null;
	private Button markView = null;
	private Button reviewView = null;
	private ProgressLayoutView progressLayoutView = null;

	private ObjectMapper om = new ObjectMapper();
	private FragmentName fromFragment = null;
	private int eventId = 0;
	private int userId = 0;
	private Eventdetail eventdetail = null;
	private ArrayList<User> userArray = new ArrayList<User>();
	private UserAdapter userAdapter = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
			Locale.getDefault());

	@Override
	public void setBundle(Bundle bundle) {
		if (bundle != null) {
			fromFragment = (FragmentName) bundle
					.getSerializable("from_fragment");
			eventId = bundle.getInt("event_id");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.eventdetail_fragment, container,
				false);
		SharedPreferences sp = getSharedPreferences("playtogether",
				Context.MODE_PRIVATE);
		userId = sp.getInt(CommDef.SP_USER_ID, 0);

		initView(view);
		loadDataFromServer();
		return view;
	}

	private void initView(View view) {
		LinearLayout footerBarView = (LinearLayout) getSupportActivity()
				.findViewById(R.id.foot_bar);
		footerBarView.setVisibility(View.INVISIBLE);
		RelativeLayout topicBarView = (RelativeLayout) view
				.findViewById(R.id.topic_bar);
		footerBarView = (LinearLayout) view.findViewById(R.id.foot_bar);

		scrollView = (AutoHideScrollView) view.findViewById(R.id.scroll);
		scrollView.setHeaderAndFooter(topicBarView, footerBarView);

		ImageButton backView = (ImageButton) view.findViewById(R.id.back);
		backView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		creatorNameView = (TextView) view.findViewById(R.id.creator_name);
		nameView = (TextView) view.findViewById(R.id.name);
		contentView = (TextView) view.findViewById(R.id.content);
		timeView = (TextView) view.findViewById(R.id.time);
		postionView = (TextView) view.findViewById(R.id.postion);
		postionView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle bundle = new Bundle();
				bundle.putDouble("latitude", eventdetail.getLatitude());
				bundle.putDouble("longitude", eventdetail.getLongitude());
				((MainActivity) getSupportActivity()).setFrament(
						FragmentName.ShowPositionFragment, bundle);
			}
		});

		BounceListView userListView = (BounceListView) view
				.findViewById(R.id.user_list);
		userAdapter = new UserAdapter(getSupportActivity());
		userListView.setAdapter(userAdapter);

		userCountView = (TextView) view.findViewById(R.id.user_count);

		markView = (Button) view.findViewById(R.id.mark);
		markView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressLayoutView.increaseProgressRef(true);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", userId);
				map.put("event_id", eventId);
				if (eventdetail.getMark() == 0) {
					map.put("mark", 1);
				} else if (eventdetail.getMark() == 1) {
					map.put("mark", 0);
				}
				CommUtil.makeRequest(getSupportActivity(), CommDef.SET_MARK,
						map, new Listener<JsonNode>() {
							@Override
							public void onResponse(JsonNode response) {
								int retCode = response.findValue("retCode")
										.asInt();
								if (retCode == 0) {
									if (eventdetail.getMark() == 0) {
										eventdetail.setMark(1);
										markView.setText("已收藏");
										CommUtil.makeToast(
												getSupportActivity(), "收藏成功");
									} else if (eventdetail.getMark() == 1) {
										eventdetail.setMark(0);
										markView.setText("收藏");
										CommUtil.makeToast(
												getSupportActivity(), "取消收藏成功");
									}
								} else if (retCode == -1) {
									CommUtil.makeToast(getSupportActivity(),
											"网络连接失败");
								} else if (retCode == 1) {
									CommUtil.makeToast(getSupportActivity(),
											"服务器异常");
								}
								progressLayoutView.decreaseProgressRef(true);
							}
						});
			}
		});

		attendView = (Button) view.findViewById(R.id.attend);
		attendView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressLayoutView.increaseProgressRef(true);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("user_id", userId);
				map.put("event_id", eventId);
				if (eventdetail.getAttend() == 0) {
					map.put("attend", 1);
				} else if (eventdetail.getAttend() == 1) {
					map.put("attend", 0);
				}
				CommUtil.makeRequest(getSupportActivity(), CommDef.SET_ATTEND,
						map, new Listener<JsonNode>() {
							@Override
							public void onResponse(JsonNode response) {
								int retCode = response.findValue("retCode")
										.asInt();
								if (retCode == 0) {
									if (eventdetail.getAttend() == 0) {
										eventdetail.setAttend(1);
										attendView.setText("已参加");
										CommUtil.makeToast(
												getSupportActivity(), "参加成功");
									} else if (eventdetail.getAttend() == 1) {
										eventdetail.setAttend(0);
										attendView.setText("参加");
										CommUtil.makeToast(
												getSupportActivity(), "退出成功");
									}
									try {
										User[] resultArray = om.readValue(
												response.findValue("content")
														.toString(),
												User[].class);
										userArray.clear();
										for (User user : resultArray) {
											userArray.add(user);
										}
										userAdapter.notifyDataSetChanged();

										userCountView.setText(String.format(
												"参加的人: (%d)", userArray.size()));

										scrollView.smoothScrollTo(0, 0);
										scrollView.showHeaderAndFooter();
									} catch (Exception e) {
										CommUtil.makeToast(
												getSupportActivity(), "服务器异常");
									}
								} else if (retCode == -1) {
									CommUtil.makeToast(getSupportActivity(),
											"网络连接失败");
								} else if (retCode == 1) {
									CommUtil.makeToast(getSupportActivity(),
											"服务器异常");
								}
								progressLayoutView.decreaseProgressRef(true);
							}
						});
			}
		});

		reviewView = (Button) view.findViewById(R.id.review);
		reviewView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("event_id", eventId);
				((MainActivity) getSupportActivity()).setFrament(
						FragmentName.ReviewFragment, bundle);
			}
		});

		progressLayoutView = (ProgressLayoutView) view
				.findViewById(R.id.progress_layout);

	}

	private void updateView() {
		creatorNameView.setText(eventdetail.getCreatorName());
		nameView.setText(eventdetail.getName());
		contentView.setText(eventdetail.getContent());
		timeView.setText("集合时间: "
				+ sdf.format(new Date(eventdetail.getTime() * 1000)));
		postionView.setText("集合地点: " + eventdetail.getPosition());
		userCountView.setText(String.format("参加的人: (%d)", userArray.size()));

		if (eventdetail.getAttend() == 0) {
			attendView.setText("参加");
		} else if (eventdetail.getAttend() == 1) {
			attendView.setText("已参加");
		}

		if (eventdetail.getMark() == 0) {
			markView.setText("收藏");
		} else if (eventdetail.getMark() == 1) {
			markView.setText("已收藏");
		}
		reviewView.setText(String.format("评论(%d)", eventdetail.getReview()));
	}

	private void loadDataFromServer() {
		progressLayoutView.increaseProgressRef(true);
		CommUtil.makeRequest(getSupportActivity(),
				String.format(CommDef.GET_EVENTDETAIL, userId, eventId),
				new Listener<JsonNode>() {
					@Override
					public void onResponse(JsonNode response) {
						int retCode = response.findValue("retCode").asInt();
						if (retCode == 0) {
							try {
								eventdetail = om.readValue(
										response.findValue("content")
												.toString(), Eventdetail.class);

								User[] resultArray = om.readValue(response
										.findValue("user").toString(),
										User[].class);
								userArray.clear();
								for (User user : resultArray) {
									userArray.add(user);
								}
								userAdapter.notifyDataSetChanged();
								scrollView.smoothScrollTo(0, 0);
								scrollView.showHeaderAndFooter();

								updateView();
							} catch (Exception e) {
								CommUtil.makeToast(getSupportActivity(),
										"服务器异常");
							}
						} else if (retCode == -1) {
							CommUtil.makeToast(getSupportActivity(), "网络连接失败");
						}
						progressLayoutView.decreaseProgressRef(true);
					}
				});
	}

	@Override
	public void onBackPressed() {
		((MainActivity) getSupportActivity()).setFrament(fromFragment, null);
	}

	private class UserAdapter extends BaseAdapter {

		private Context context;

		public UserAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return userArray.size();
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
						R.layout.user, null, false);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.name);
				holder.sexView = (TextView) convertView.findViewById(R.id.sex);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			User user = userArray.get(position);
			holder.nameView.setText(user.getName());
			if (user.getSex() == 0) {
				holder.sexView.setText("♀");
			} else if (user.getSex() == 1) {
				holder.sexView.setText("♂");
			} else if (user.getSex() == 2) {
				holder.sexView.setText("?");
			}

			return convertView;
		}

		private class ViewHolder {
			TextView nameView;
			TextView sexView;
		}
	}

}
