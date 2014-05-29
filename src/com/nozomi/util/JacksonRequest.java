/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nozomi.util;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL,
 * allowing for an optional {@link JSONObject} to be passed in as part of the
 * request body.
 */
public class JacksonRequest extends JsonRequest<JsonNode> {
	private static ObjectMapper om = new ObjectMapper();

	/**
	 * Creates a new request.
	 * 
	 * @param method
	 *            the HTTP method to use
	 * @param url
	 *            URL to fetch the JSON from
	 * @param jsonRequest
	 *            A {@link JSONObject} to post with the request. Null is allowed
	 *            and indicates no parameters will be posted along with request.
	 * @param listener
	 *            Listener to receive the JSON response
	 * @param errorListener
	 *            Error listener, or null to ignore errors.
	 * @throws Exception
	 */
	public JacksonRequest(int method, String url, Object requestBody,
			final Listener<JsonNode> listener, ErrorListener errorListener)
			throws Exception {
		super(method, url, requestBody == null ? null : om
				.writeValueAsString(requestBody), listener,
				errorListener == null ? new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						try {
							JsonNode jn = om.readTree("{\"RetCode\":-1}");
							listener.onResponse(jn);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				} : errorListener);

	}

	/**
	 * Constructor which defaults to <code>GET</code>
	 * 
	 * @throws Exception
	 * 
	 * @see #JsonObjectRequest(int, String, JSONObject, Listener, ErrorListener)
	 */
	public JacksonRequest(String url, Listener<JsonNode> listener,
			ErrorListener errorListener) throws Exception {
		this(Method.GET, url, null, listener, errorListener);

	}

	/**
	 * Constructor which defaults to <code>GET</code>
	 * 
	 * @throws Exception
	 * 
	 * @see #JsonObjectRequest(int, String, JSONObject, Listener, ErrorListener)
	 */
	public JacksonRequest(String url, Listener<JsonNode> listener)
			throws Exception {
		this(Method.GET, url, null, listener, null);
	}

	/**
	 * Constructor which defaults to <code>POST</code>
	 * 
	 * @throws Exception
	 * 
	 * @see #JsonObjectRequest(int, String, JSONObject, Listener, ErrorListener)
	 */
	public JacksonRequest(String url, Object requestBody,
			Listener<JsonNode> listener, ErrorListener errorListener)
			throws Exception {
		this(Method.POST, url, requestBody, listener, errorListener);
	}

	/**
	 * Constructor which defaults to <code>POST</code>
	 * 
	 * @throws Exception
	 * 
	 * @see #JsonObjectRequest(int, String, JSONObject, Listener, ErrorListener)
	 */
	public JacksonRequest(String url, Object requestBody,
			Listener<JsonNode> listener) throws Exception {
		this(Method.POST, url, requestBody, listener, null);
	}

	@Override
	protected Response<JsonNode> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			JsonNode jn = om.readTree(jsonString);
			return Response.success(jn,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

}
