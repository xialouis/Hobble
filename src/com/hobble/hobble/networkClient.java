package com.hobble.hobble;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class networkClient {
	// Global http objects
	HttpHost serverHost;
	HttpClient client;
	HttpPost createUserPost;
	HttpPost loginUserPost;
	String sessionId;

	// Initalize http urls in constructor
	public networkClient() {
		serverHost = new HttpHost(
				"http://ec2-50-17-146-172.compute-1.amazonaws.com");
		client = new DefaultHttpClient();
		createUserPost = new HttpPost("/rest/user/create");
		loginUserPost = new HttpPost("/rest/user/login");
	}

	// Method to create a new user on server, gets a user token back
	public void create(int deviceId) throws IOException {
		// Create response and result objects
		HttpResponse response;
		HttpEntity result;

		// Set up POST paramaters, use deviceId
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("device_id", Integer
				.toString(deviceId)));
		postParams.add(new BasicNameValuePair("key", "b0bp5y"));
		try {
			// Make request
			createUserPost.setEntity(new UrlEncodedFormEntity(postParams));
			response = client.execute(serverHost, createUserPost);

			// Get response
			result = response.getEntity();

			// Check response for errors
			if (result != null) {
				// Parse resulting JSON object
				String resultString = EntityUtils.toString(result);
				JSONObject resultJson = new JSONObject(resultString);
				String success = resultJson.getString("success");
				// Check if successful
				if (success.equals("true")) {
					sessionId = resultJson.getString("session_id");
				} else {
					Log.e("HTTP",
							"Server responded with error "
									+ resultJson.getString("error_code") + " "
									+ resultJson.getString("message"));
				}
			} else {
				Log.e("HTTP", "Response null ");
			}
		} catch (Exception e) {
			Log.e("HTTP", "Error in http connection " + e.toString());
		}
	}

	// Method to login, gets a user token back
	public void login(int deviceId) throws IOException {
		// TODO: almost exact same as create but duplicate code so each method
		// can be called seperately
	}
}