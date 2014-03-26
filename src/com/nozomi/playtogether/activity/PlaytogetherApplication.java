package com.nozomi.playtogether.activity;

import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Application;

import android.content.Context;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class PlaytogetherApplication extends Application {
	private BMapManager mBMapManager = null;
	private static final String strKey = "R0aqxWCGAKsr5YGk3zOpP7i5";

	@Override
	public void onCreate() {
		super.onCreate();
		ThemeManager
				.setDefaultTheme(org.holoeverywhere.R.style.Holo_Theme_Light_NoActionBar);
		initEngineManager(this);
	}

	private void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			// Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG)
			// .show();
		}
	}

	private class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				// CommUtil.makeToast(PlaytogetherApplication.this, "您的网络出错啦！");
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				// CommUtil.makeToast(PlaytogetherApplication.this,
				// "输入正确的检索条件！");
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				// 授权Key错误：
				// CommUtil.makeToast(PlaytogetherApplication.this,
				// "请在 DemoApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "
				// + iError);
			} else {
				// CommUtil.makeToast(PlaytogetherApplication.this, "key认证成功");
			}
		}
	}
}
