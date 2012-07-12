package com.modbusgooey.spreadsheets;

import java.io.IOException;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.iubiquity.spreadsheets.client.SpreadsheetClient;
import com.iubiquity.spreadsheets.model.Feed;
import com.iubiquity.spreadsheets.model.SpreadsheetFeed;
import com.iubiquity.spreadsheets.model.SpreadsheetUrl;

public class AndroidSpreadSheetClient extends SpreadsheetClient {
	private static final String TAG = "AndroidSpreadSheetClient";
	private String authToken;
	private AsyncSpreadsheetCaller caller;
	private int requestCode = Integer.MAX_VALUE;
	private ProgressDialog dialog;

	// public AndroidSpreadSheetClient(String authToken) {
	// this.authToken = authToken;
	// }
	//
	/**
	 * Allows the creation of an Asynchronous spreadsheet client that calls back
	 * to the activity after background processing is completed. This
	 * implementation using the Android default progress dialog. To use a custom
	 * dialog, use one of the other constructors.
	 * 
	 * @param authToken
	 *            the OAUTH 2.0 authentication token for the Spreadsheet
	 *            authentication
	 * @param requestCode
	 *            the request code to use in call back
	 * @param caller
	 *            the interface which implements the callback method (the
	 *            calling activity must implement this inteface)
	 */
	public AndroidSpreadSheetClient(String authToken,
			AsyncSpreadsheetCaller caller) {
		this.caller = caller;
		this.authToken = authToken;
	}

	/**
	 * Creates the client and authenticates with the service using the given
	 * authentication token *
	 * 
	 * @param context
	 *            reference to the current Context
	 * @param applicationName
	 *            sets the "User-Agent" header for the given application name of
	 *            the form "[company-id]-[app-name]-[app-version]" into the
	 *            given HTTP headers.
	 */
	public void createClient(final Context context, final String applicationName) {

		final HttpTransport transport = AndroidHttp.newCompatibleTransport();

		super.requestFactory = transport
				.createRequestFactory(new HttpRequestInitializer() {

					public void initialize(final HttpRequest request) {
						final GoogleHeaders headers = new GoogleHeaders();
						// Sets the "User-Agent" header for the given
						// application name of the form
						// "[company-id]-[app-name]-[app-version]" into the
						// given HTTP headers.
						headers.setApplicationName(applicationName);
						headers.gdataVersion = "2";
						request.setHeaders(headers);
						initializeParser(request);
						request.setInterceptor(new HttpExecuteInterceptor() {

							public void intercept(final HttpRequest request)
									throws IOException {
								final GoogleHeaders headers = (GoogleHeaders) request
										.getHeaders();
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
									AccountManager.get(context)
											.invalidateAuthToken("com.google",
													authToken);
									// accountManager.invalidateAuthToken("com.google",
									// authToken);
									// settings.edit()
									// .remove(MainActivity.PREF_AUTH_TOKEN)
									// .commit();
									// final Intent intent = new Intent(context,
									// MainActivity.class);
									// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									// MainActivity.this.startActivity(intent);
									// throw new
									// AuthenticationException("Unauthorized:
									// status code)
								}
								return false;
							}
						});
					}
				});
	}

	private class AsyncSpreadsheetClient extends AsyncTask<Object, Void, Feed> {

		@Override
		protected void onPreExecute() {
			if (dialog == null) {
				dialog = new ProgressDialog((Context) caller);
				dialog.setMessage("Loading spreadsheet data...");
			}

			dialog.show();
		}

		@Override
		protected Feed doInBackground(Object... params) {
			HttpRequest request = (HttpRequest) params[0];
			Class<Feed> feedClass = (Class<Feed>) params[1];
			
			try {
				return request.execute().parseAs(feedClass);
			} catch (IOException e) {
				caller.onExceptionResponse(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Feed result) {
			dialog.dismiss();

			if (result != null) {
				caller.onSpreadsheetResult(requestCode, result);
			}
		}

	}

	public void executeAsyncGetFeed(final int requestCode, final String url,
			Class<? extends Feed> feedClass) {
		this.requestCode = requestCode;
		HttpRequest request = null;
		try {
			request = requestFactory.buildGetRequest(new SpreadsheetUrl(url));
		} catch (IOException e) {
			caller.onExceptionResponse(e);
		}

		new AsyncSpreadsheetClient().execute(request, feedClass);
	}

	public void getSpreadsheetMetafeed(final int requestCode) {
		this.requestCode = requestCode;

		HttpRequest request = null;
		try {
			request = requestFactory.buildGetRequest(SpreadsheetUrl
					.forSpreadSheetMetafeed());
		} catch (IOException e) {
			caller.onExceptionResponse(e);
		}

		new AsyncSpreadsheetClient().execute(request, SpreadsheetFeed.class);

	}
}
