package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.SpreadsheetFeed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpreadsheetFeedRequest extends SpreadsheetRequest<SpreadsheetFeed> {

    SpreadsheetFeedRequest(Listener listener, ErrorListener errorListener) {
        super(Method.GET,
                "https://spreadsheets.google.com/feeds/spreadsheets/private/full",
                listener,
                errorListener);
    }

    @Override
    protected Response<SpreadsheetFeed> parseNetworkResponse(NetworkResponse response) {
        try {
            SpreadsheetFeed feed = parser
                        .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                                , SpreadsheetFeed.class);
            return Response.success(feed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() {

        return new byte[0];
    }
}
