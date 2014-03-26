package com.nozomi.playtogether.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.holoeverywhere.widget.datetimepicker.date.DatePickerDialog;
import org.holoeverywhere.widget.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import org.holoeverywhere.widget.datetimepicker.time.RadialPickerLayout;
import org.holoeverywhere.widget.datetimepicker.time.TimePickerDialog;
import org.holoeverywhere.widget.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

import com.android.volley.Response.Listener;
import com.fasterxml.jackson.databind.JsonNode;
import com.nozomi.Models.Eventdetail;
import com.nozomi.playtogether.R;
import com.nozomi.playtogether.activity.MainActivity;
import com.nozomi.playtogether.activity.MainActivity.FragmentName;
import com.nozomi.util.CommDef;
import com.nozomi.util.CommUtil;
import com.nozomi.view.BackFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CreateEventFragment extends BackFragment {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
			Locale.getDefault());
	private Eventdetail eventdetail = null;

	@Override
	public void setBundle(Bundle bundle) {
		if (bundle != null) {
			double latitude = bundle.getDouble("latitude");
			double longitude = bundle.getDouble("longitude");
			String position = bundle.getString("position");
			eventdetail.setLatitude(latitude);
			eventdetail.setLongitude(longitude);
			eventdetail.setPosition(position);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (eventdetail == null) {
			eventdetail = new Eventdetail();
		}

		SharedPreferences sp = getSharedPreferences("playtogether",
				Context.MODE_PRIVATE);
		int userId = sp.getInt(CommDef.SP_USER_ID, 0);
		eventdetail.setCreatorId(userId);

		View view = inflater.inflate(R.layout.create_event_fragment, container,
				false);

		initView(view);

		return view;
	}

	private void initView(View view) {
		LinearLayout footBarView = (LinearLayout) getSupportActivity()
				.findViewById(R.id.foot_bar);
		footBarView.setVisibility(View.VISIBLE);

		final EditText nameView = (EditText) view.findViewById(R.id.name);

		if (eventdetail.getName() == null) {
			nameView.setText("");
		} else {
			nameView.setText(eventdetail.getName());
		}

		final TextView timeView = (TextView) view.findViewById(R.id.time);
		if (eventdetail.getTime() == null) {
			timeView.setText("");
		} else {
			timeView.setText(sdf.format(new Date(eventdetail.getTime() * 1000)));
		}
		final Calendar calendar = Calendar.getInstance();
		timeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog.newInstance(
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePickerDialog view,
									int year, int monthOfYear, int dayOfMonth) {
								calendar.set(Calendar.YEAR, year);
								calendar.set(Calendar.MONTH, monthOfYear);
								calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

								TimePickerDialog
										.newInstance(
												new OnTimeSetListener() {

													@Override
													public void onTimeSet(
															RadialPickerLayout view,
															int hourOfDay,
															int minute) {
														calendar.set(
																Calendar.HOUR_OF_DAY,
																hourOfDay);
														calendar.set(
																Calendar.MINUTE,
																minute);
														eventdetail
																.setTime(calendar
																		.getTimeInMillis() / 1000);
														timeView.setText(sdf
																.format(new Date(
																		eventdetail
																				.getTime() * 1000)));
													}
												},
												calendar.get(Calendar.HOUR_OF_DAY),
												calendar.get(Calendar.MINUTE),
												true)
										.show(getSupportActivity());

							}
						}, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show(
						getSupportActivity());

			}
		});

		TextView positionView = (TextView) view.findViewById(R.id.position);
		if (eventdetail.getPosition() == null) {
			positionView.setText("");
		} else {
			positionView.setText(eventdetail.getPosition());
		}

		final EditText contentView = (EditText) view.findViewById(R.id.content);
		if (eventdetail.getContent() == null) {
			contentView.setText("");
		} else {
			contentView.setText(eventdetail.getContent());
		}

		positionView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				eventdetail.setName(nameView.getText().toString().trim());
				eventdetail.setContent(contentView.getText().toString().trim());

				Bundle bundle = null;
				if (eventdetail.getLatitude() != null
						&& eventdetail.getLongitude() != null) {
					bundle = new Bundle();
					bundle.putDouble("latitude", eventdetail.getLatitude());
					bundle.putDouble("longitude", eventdetail.getLongitude());
					bundle.putString("position", eventdetail.getPosition());
				}
				((MainActivity) getSupportActivity()).setFrament(
						FragmentName.SetPositionFragment, bundle);

			}
		});

		Button createView = (Button) view.findViewById(R.id.create);
		createView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				eventdetail.setName(nameView.getText().toString().trim());
				eventdetail.setContent(contentView.getText().toString().trim());
				if(eventdetail.getName()==null||eventdetail.getName().equals("")){
					CommUtil.makeToast(getSupportActivity(), "请输入标题");
					return;
				}
				
				if(eventdetail.getTime()==null||eventdetail.getTime().equals("")){
					CommUtil.makeToast(getSupportActivity(), "请输入集合时间");
					return;
				}
				
				if(eventdetail.getPosition()==null||eventdetail.getPosition().equals("")){
					CommUtil.makeToast(getSupportActivity(), "请输入集合地点");
					return;
				}
				
				if(eventdetail.getContent()==null||eventdetail.getContent().equals("")){
					CommUtil.makeToast(getSupportActivity(), "请输入内容");
					return;
				}

				CommUtil.makeRequest(getSupportActivity(),
						CommDef.CREATE_EVENT, eventdetail,
						new Listener<JsonNode>() {
							@Override
							public void onResponse(JsonNode response) {
								int retCode = response.findValue("retCode")
										.asInt();
								if (retCode == 0) {
									CommUtil.makeToast(getSupportActivity(),
											"创建成功");
									eventdetail = null;
									nameView.setText("");
									contentView.setText("");

									int id = response.findValue("id").asInt();
									Bundle bundle = new Bundle();
									bundle.putSerializable("from_fragment",
											FragmentName.EventFragment);
									bundle.putInt("event_id", id);
									((MainActivity) getSupportActivity())
											.setFrament(
													FragmentName.EventdetailFragment,
													bundle);

								} else if (retCode == -1) {
									CommUtil.makeToast(getSupportActivity(),
											"网络连接失败");
								} else if (retCode == 1) {
									CommUtil.makeToast(getSupportActivity(),
											"用户不存在");
								} else if (retCode == 2) {
									CommUtil.makeToast(getSupportActivity(),
											"服务器异常");
								}
							}
						});

			}
		});
	}

	@Override
	public void onBackPressed() {
		((MainActivity) getSupportActivity()).setFrament(
				FragmentName.EventFragment, null);
	}

}
