package com.modbusgooey.spreadsheets;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.iubiquity.spreadsheets.client.SpreadsheetClient;

public class AndroidSpreadSheetClient extends SpreadsheetClient {
	private static final String TAG = "AndroidSpreadSheetClient";
	private String authToken;
	
	public AndroidSpreadSheetClient(String authToken) {
		this.authToken = authToken;
	}
	
	
	public void createClient(final Context context) {

		final HttpTransport transport = AndroidHttp.newCompatibleTransport();

		super.requestFactory = 
				transport.createRequestFactory(new HttpRequestInitializer() {

					public void initialize(final HttpRequest request) {
						final GoogleHeaders headers = new GoogleHeaders();
						headers.setApplicationName("Modbus-Gooey/1.0");
						headers.gdataVersion = "2";
						request.setHeaders(headers);
						initializeParser(request);
						request.setInterceptor(new HttpExecuteInterceptor() {

							public void intercept(final HttpRequest request)
									throws IOException {
								final GoogleHeaders headers = (GoogleHeaders) request.getHeaders();
								Log.d(TAG, "setting authToken in Header: "
										+ authToken);
								headers.setGoogleLogin(authToken);
								new MethodOverride().intercept(request);
							}
						});

						request.setUnsuccessfulResponseHandler(new HttpUnsuccessfulResponseHandler() {
							public boolean handleResponse(
									final HttpRequest request,
									final HttpResponse response,
									final boolean retrySupported) {

								if (response.getStatusCode() == 401) {
									Log.d(TAG, "invalidating token "
											+ authToken);
									AccountManager.get(context).invalidateAuthToken("com.google", authToken);
//									accountManager.invalidateAuthToken("com.google", authToken);
//									settings.edit()
//											.remove(MainActivity.PREF_AUTH_TOKEN)
//											.commit();
//									final Intent intent = new Intent(context,
//											MainActivity.class);
//									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//									MainActivity.this.startActivity(intent);
//									throw new AuthenticationException("Unauthorized: status code) 
								}
								return false;
							}
						});
					}
				});
	}

}
