package com.nozomi.util;

import java.security.MessageDigest;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JsonNode;

import android.content.Context;
import android.widget.Toast;

public class CommUtil {

	private static Toast toast = null;

	public static void makeToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public static void makeRequest(Context context, String url,
			Listener<JsonNode> listener) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		try {
			mQueue.add(new JacksonRequest(context, url, listener));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void makeRequest(Context context, String url,
			Object requestBody, Listener<JsonNode> listener) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		try {
			mQueue.add(new JacksonRequest(context, url, requestBody, listener));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String md5(String input) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = input.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
