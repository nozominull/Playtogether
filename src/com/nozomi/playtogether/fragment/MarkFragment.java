package com.nozomi.playtogether.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.ImageButton;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nozomi.Models.Event;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class MarkFragment extends BackFragment {
	private AutoHideXScrollView scrollView = null;
	private TextView blankView = null;
	private ProgressLayoutView progressLayoutView = null;

	private ObjectMapper om = new ObjectMapper();
	private boolean isFirstRun = true;

	private ArrayList<Event> eventArray = new ArrayList<Event>();
	private EventAdapter eventAdapter = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
			Locale.getDefault());
	private int userId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.mark_fragment, container, false);
		SharedPreferences sp = getSharedPreferences("playtogether",
				Context.MODE_PRIVATE);
		userId = sp.getInt(CommDef.SP_USER_ID, 0);
		initView(view);

		if (isFirstRun) {
			isFirstRun = false;
			loadDataFromServer(true, true);
		} else {
			scrollView.smoothScrollTo(0, 0);
			scrollView.showHeaderAndFooter();
		}

		return view;
	}

	private void initView(View view) {
		RelativeLayout topicBarView = (RelativeLayout) view
				.findViewById(R.id.topic_bar);
		LinearLayout footerBarView = (LinearLayout) getSupportActivity()
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

		BounceListView eventListView = (BounceListView) view
				.findViewById(R.id.event_list);
		eventAdapter = new EventAdapter(getSupportActivity());
		eventListView.setAdapter(eventAdapter);
		eventListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("from_fragment",
						FragmentName.MarkFragment);
				bundle.putInt("event_id", eventArray.get(position).getId());
				((MainActivity) getSupportActivity()).setFrament(
						FragmentName.EventdetailFragment, bundle);

			}
		});

		blankView = (TextView) view.findViewById(R.id.blank);
		blankView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadDataFromServer(true, true);
			}
		});

		if (eventArray.isEmpty()) {
			scrollView.setVisibility(View.INVISIBLE);
			blankView.setVisibility(View.VISIBLE);
		} else {
			scrollView.setVisibility(View.VISIBLE);
			blankView.setVisibility(View.GONE);
		}

		progressLayoutView = (ProgressLayoutView) view
				.findViewById(R.id.progress_layout);

	}

	private void loadDataFromServer(final boolean isFirst,
			final boolean isHeader) {
		progressLayoutView.increaseProgressRef(isFirst);
		int offset = 0;
		if (!isHeader) {
			offset = eventArray.size();
		}

		CommUtil.makeRequest(getSupportActivity(),
				String.format(CommDef.GET_MARK, userId, offset, 10),
				new Listener<JsonNode>() {
					@Override
					public void onResponse(JsonNode response) {
						int retCode = response.findValue("retCode").asInt();
						if (retCode == 0) {
							try {
								JsonNode contentJn = response
										.findValue("content");
								Event[] resultArray = om.readValue(
										contentJn.toString(), Event[].class);
								if (isHeader) {
									eventArray.clear();
								}
								for (Event event : resultArray) {
									eventArray.add(event);
								}
								eventAdapter.notifyDataSetChanged();
								if (isHeader) {
									scrollView.smoothScrollTo(0, 0);
									scrollView.showHeaderAndFooter();
								}

								if (eventArray.isEmpty()) {
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
		getSupportActivity().finish();
	}

	private class EventAdapter extends BaseAdapter {

		private Context context;

		public EventAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return eventArray.size();
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
						R.layout.event, null, false);
				holder.creatorNameView = (TextView) convertView
						.findViewById(R.id.creator_name);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.name);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.time);
				holder.postionView = (TextView) convertView
						.findViewById(R.id.postion);

				holder.attendView = (TextView) convertView
						.findViewById(R.id.attend);
				holder.reviewView = (TextView) convertView
						.findViewById(R.id.review);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Event event = eventArray.get(position);
			holder.creatorNameView.setText(event.getCreatorName());
			holder.nameView.setText(event.getName());
			holder.postionView.setText(event.getPosition());
			holder.timeView
					.setText(sdf.format(new Date(event.getTime() * 1000)));
			holder.attendView.setText(String.valueOf(event.getAttend()));
			holder.reviewView.setText(String.valueOf(event.getReview()));
			return convertView;
		}

		private class ViewHolder {
			TextView creatorNameView;
			TextView nameView;
			TextView timeView;
			TextView postionView;
			TextView attendView;
			TextView reviewView;
		}
	}

}
