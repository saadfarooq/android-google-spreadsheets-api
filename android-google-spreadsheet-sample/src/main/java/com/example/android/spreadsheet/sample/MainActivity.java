package com.example.android.spreadsheet.sample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.spreadsheets.android.api.oauth.SpreadsheetOAuth;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

import static com.google.android.gms.common.GooglePlayServicesUtil.*;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnGetAccess = (Button) findViewById(R.id.btn_oauth);
        btnGetAccess.setOnClickListener(this);

    }


    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(
                GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_oauth:
                getToken();
                break;
        }
    }

    private void getToken() {

        switch (isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SUCCESS:
                Log.d("T", "Success");
                break;

            default:
                Log.d("T", "Something else");
                break;
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(MainActivity.this, "unimpeccable@gmail.com",
                            "oauth2:https://spreadsheets.google.com/feeds");
                } catch (GooglePlayServicesAvailabilityException e) {
                    // GooglePlayServices.apk is either old, disabled, or not present.
                    e.printStackTrace();
                } catch (UserRecoverableAuthException userRecoverableException) {
                    // Unable to authenticate, but the user can fix this.
                    // Forward the user to the appropriate activity.
                    MainActivity.this.startActivityForResult(userRecoverableException.getIntent(), 50);
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(final String token) {
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
            }
        }.execute();

    }
}
