package com.github.spreadsheets.android.api.oauth;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Activity to manage getting Google OAuth token
 * Needs to have EXTRA_EMAIL and EXTRA_SCOPE extras set
 */
public class SpreadsheetOAuthActivity extends Activity {
    public static final String EXTRA_EMAIL = "extra_email";
    public static final String EXTRA_SCOPE = "extra_scope";
    public static final String EXTRA_RESULT_MESSAGE = "extra_result_message";
    public static final String EXTRA_TOKEN = "extra_token";

    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;

    private String scope;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(EXTRA_EMAIL) && bundle.containsKey(EXTRA_SCOPE)) {
            scope = bundle.getString(EXTRA_SCOPE);
            email = bundle.getString(EXTRA_EMAIL);
            getAndUseAuthTokenInAsyncTask();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            setResult(RESULT_CANCELED);
        }
        if (resultCode == RESULT_OK) {
            getAndUseAuthTokenBlocking();
        }
        if (resultCode == RESULT_CANCELED) {
            setResult(RESULT_CANCELED);
        }
    }

    // Example of how to use the GoogleAuthUtil in a blocking, non-main thread context
    void getAndUseAuthTokenBlocking() {
        try {
            // Retrieve a token for the given account and scope. It will always return either
            // a non-empty String or throw an exception.
            final String token = GoogleAuthUtil.getToken(this, email, scope);
            final Intent intent = new Intent();
            intent.putExtra(EXTRA_TOKEN, token);
            setResult(RESULT_OK, intent);
            finish();
        } catch (GooglePlayServicesAvailabilityException playEx) {
            final Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT_MESSAGE, "GooglePlayServicesAvailable");
            setResult(RESULT_CANCELED, intent);
        } catch (UserRecoverableAuthException userAuthEx) {
            // Start the user recoverable action using the intent returned by
            // getIntent()
            startActivityForResult(userAuthEx.getIntent(), REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
            setResult(RESULT_OK);
            finish();
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            final Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT_MESSAGE, "IOException");
            setResult(RESULT_CANCELED, intent);
            finish();
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            final Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT_MESSAGE, "GoogleAuthException");
            setResult(RESULT_CANCELED, intent);
        }

    }

    void getAndUseAuthTokenInAsyncTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                getAndUseAuthTokenBlocking();
                return null;
            }
        }.execute();
    }
}
