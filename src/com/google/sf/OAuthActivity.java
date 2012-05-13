package com.google.sf;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OAuthActivity extends Activity {
	private static final int DIALOG_ACCOUNTS = 0;
	
	private static final String AUTH_TOKEN_TYPE = "wise";
	
	AccountManager accountManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountManager = AccountManager.get(this);
		showDialog(DIALOG_ACCOUNTS);
	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
	  switch (id) {
	    case DIALOG_ACCOUNTS:
	      AlertDialog.Builder builder = new AlertDialog.Builder(this);
	      builder.setTitle("Select a Google account");	      
	      final Account[] accounts = accountManager.getAccountsByType("com.google");
	      final int size = accounts.length;
	      String[] names = new String[size];
	      for (int i = 0; i < size; i++) {
	        names[i] = accounts[i].name;
	      }
	      builder.setItems(names, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	          // Stuff to do when the account is selected by the user
	          gotAccount(accounts[which]);
	        }
	      });
	      return builder.create();
	  }
	  return null;
	}


	protected void gotAccount(Account account) {		
		accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, this, new AccountManagerCallback<Bundle>() {

			public void run(AccountManagerFuture<Bundle> future) {
				try {
					String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
					useGoogleAPI(token);
					
				} catch (OperationCanceledException e) {
					// TODO Handle exceptions
					e.printStackTrace();
				} catch (AuthenticatorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}, null);
	}


	protected void useGoogleAPI(String token) {
		/*HttpTransport transport = AndroidHttp.newCompatibleTransport();
		AccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(accessToken);
		Tasks service = new Tasks(transport, accessProtectedResource, new JacksonFactory());
		service.accessKey = INSERT_YOUR_API_KEY;
		service.setApplicationName("Google-TasksSample/1.0");*/
		Toast.makeText(this, "Token: "+token, Toast.LENGTH_SHORT).show();
		Intent callingIntent = getIntent();
		callingIntent.putExtra("Token", token);
		setResult(RESULT_OK, callingIntent);
		finish();
	}
	
}
