package com.github.spreadsheets.android.api.oauth;

import com.android.volley.AuthFailureError;

public class SpreadsheetOAuth {
    public static final String READ_SCOPE = "oauth2:https://spreadsheets.google.com/feeds";
    public static final String READ_WRITE_SCOPE =
                            "oauth2:https://spreadsheets.google.com/feeds https://docs.google.com/feeds";

    private static String oauthToken;

    public static String getAuthToken() throws AuthFailureError {
        if (oauthToken != null) {
            return oauthToken;
        } else {
            throw new AuthFailureError("No authentication token available");
        }
    }

    public static void setAuthToken(final String token) {
        oauthToken = token;
    }
}
