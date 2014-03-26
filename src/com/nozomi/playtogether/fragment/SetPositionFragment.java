package com.nozomi.playtogether.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nozomi.playtogether.R;
import com.nozomi.playtogether.activity.MainActivity;
import com.nozomi.playtogether.activity.MainActivity.FragmentName;
import com.nozomi.view.BackFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class SetPositionFragment extends BackFragment {

	private LocationClient mLocClient = null;

	private MapView mMapView = null;
	private MapController mMapController = null;
	private double latitude = 0;
	private double longitude = 0;
	private String position = null;

	@Override
	public void setBundle(Bundle bundle) {
		if (bundle != null) {
			latitude = bundle.getDouble("latitude");
			longitude = bundle.getDouble("longitude");
			position = bundle.getString("position");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.set_position_fragment, container, false);

		initView(view);

		if (latitude == 0 && longitude == 0) {
			mLocClient = new LocationClient(getSupportActivity());
			mLocClient.registerLocationListener(new MyLocationListenner());
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);
			option.setCoorType("bd09ll");
			mLocClient.setLocOption(option);
			mLocClient.start();
		} else {
			GeoPoint p = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			mMapController.setCenter(p);
		}

		return view;
	}

	private void initView(View view) {

		LinearLayout footBarView = (LinearLayout) getSupportActivity()
				.findViewById(R.id.foot_bar);
		footBarView.setVisibility(View.INVISIBLE);

		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);

	}

	@Override
	public void onBackPressed() {
		// GeoPoint p =mMapView.getMapCenter();
		// Bundle bundle = new Bundle();
		// bundle.putDouble("latitude", 1.0*p.getLatitudeE6()/1E6);
		// bundle.putDouble("longitude", 1.0*p.getLongitudeE6()/1E6);
		// ((MainActivity) getSupportActivity()).setFrament(
		// FragmentName.EventFragment, bundle);
		final EditText edittext = new EditText(getSupportActivity());
		if (position != null) {
			edittext.setText(position);
		}
		new AlertDialog.Builder(getSupportActivity()).setTitle("请输入地点名称")
				.setView(edittext)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GeoPoint p = mMapView.getMapCenter();
						Bundle bundle = new Bundle();
						bundle.putDouble("latitude",
								1.0 * p.getLatitudeE6() / 1E6);
						bundle.putDouble("longitude",
								1.0 * p.getLongitudeE6() / 1E6);
						bundle.putString("position", edittext.getText()
								.toString().trim());
						((MainActivity) getSupportActivity()).setFrament(
								FragmentName.CreateEventFragment, bundle);

					}
				}).show();

	}

	private class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			mLocClient.stop();

			GeoPoint p = new GeoPoint((int) (latitude * 1E6),
					(int) (longitude * 1E6));
			mMapController.setCenter(p);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}

		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mLocClient != null) {
			mLocClient.stop();
		}
	}

}
