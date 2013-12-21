package com.example.android.spreadsheet.sample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.spreadsheets.android.api.model.SpreadsheetFeed;
import com.github.spreadsheets.android.api.oauth.SpreadsheetOAuth;
import com.github.spreadsheets.android.api.oauth.SpreadsheetOAuthActivity;
import com.github.spreadsheets.android.api.requests.SpreadsheetFeedRequest;
import com.google.android.gms.auth.GoogleAuthUtil;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_OAUTH_ACTIVITY = 103;
    private AccountManager mAccountManager;
    private Spinner mAccountTypesSpinner;
    private String[] mNamesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String saad;
//        SpreadsheetOAuth.setAuthToken("saad");
//        try {
//            saad = SpreadsheetOAuth.getAuthToken();
//        } catch (AuthFailureError authFailureError) {
//            authFailureError.printStackTrace();
//        }

        final Button btnGetAccess = (Button) findViewById(R.id.btn_oauth);
        btnGetAccess.setOnClickListener(this);

        mNamesArray = getAccountNames();
        mAccountTypesSpinner = initializeSpinner(
                R.id.accounts_tester_account_types_spinner, mNamesArray);
    }

    private Response.Listener<SpreadsheetFeed> getSuccessListener() {
        return new Response.Listener<SpreadsheetFeed>() {

            @Override
            public void onResponse(SpreadsheetFeed response) {
                Toast.makeText(MainActivity.this, "Request successful", Toast.LENGTH_LONG).show();
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.getMessage());
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_LONG).show();
            }
        };
    }

    private Spinner initializeSpinner(int id, String[] values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, values);
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setAdapter(adapter);
        return spinner;
    }

    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_oauth:
                int accountIndex = mAccountTypesSpinner.getSelectedItemPosition();
                if (accountIndex < 0) {
                    // this happens when the sample is run in an emulator which has no google account
                    // added yet.
                    Toast.makeText(this,
                            "No account available. Please add an account to the phone first.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                getToken(mNamesArray[accountIndex]);
                break;
        }
    }

    private void getToken(final String email) {
        final Intent intent = new Intent(this, SpreadsheetOAuthActivity.class);
        intent.putExtra(SpreadsheetOAuthActivity.EXTRA_EMAIL, email);
        intent.putExtra(SpreadsheetOAuthActivity.EXTRA_SCOPE, SpreadsheetOAuth.READ_WRITE_SCOPE);
        startActivityForResult(intent, REQUEST_CODE_OAUTH_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OAUTH_ACTIVITY) {
            switch (resultCode) {
                case RESULT_OK:
                    useToken(data.getStringExtra(SpreadsheetOAuthActivity.EXTRA_TOKEN));
                    break;

                case RESULT_CANCELED:
                    String msg = data.getStringExtra(SpreadsheetOAuthActivity.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    break;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void useToken(String token) {
        SpreadsheetOAuth.setAuthToken(token);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        SpreadsheetFeedRequest mRequest = new SpreadsheetFeedRequest(getSuccessListener(), getErrorListener());
        mRequestQueue.add(mRequest);
    }
}
