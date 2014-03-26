package com.nozomi.playtogether.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.LinearLayout;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nozomi.playtogether.R;
import com.nozomi.playtogether.activity.MainActivity;
import com.nozomi.playtogether.activity.MainActivity.FragmentName;
import com.nozomi.view.BackFragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class ShowPositionFragment extends BackFragment {

	private MapView mMapView = null;
	private MapController mMapController = null;
	private double latitude = 0;
	private double longitude = 0;

	@Override
	public void setBundle(Bundle bundle) {
		if (bundle != null) {
			latitude = bundle.getDouble("latitude");
			longitude = bundle.getDouble("longitude");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.show_position_fragment, container,
				false);

		initView(view);

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

		GeoPoint p = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		mMapController.setCenter(p);

		ItemizedOverlay<OverlayItem> overlay = new ItemizedOverlay<OverlayItem>(
				getResources().getDrawable(R.drawable.show_position_marker), mMapView);
		OverlayItem overlayItem = new OverlayItem(p, "", "");
		overlayItem.setMarker(getResources().getDrawable(R.drawable.show_position_marker));
		overlay.addItem(overlayItem);
		mMapView.getOverlays().add(overlay);
		mMapView.refresh();
	}

	@Override
	public void onBackPressed() {

		((MainActivity) getSupportActivity()).setFrament(
				FragmentName.EventdetailFragment, null);

	}

}
