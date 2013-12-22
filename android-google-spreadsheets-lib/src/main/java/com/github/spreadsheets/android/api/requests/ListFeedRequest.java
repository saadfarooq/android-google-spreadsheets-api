package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.ListFeed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ListFeedRequest extends SpreadsheetRequest<ListFeed> {

    public ListFeedRequest(String url, Listener<ListFeed> listener,
                                ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<ListFeed> parseNetworkResponse(NetworkResponse response) {
        try {
            ListFeed feed = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , ListFeed.class);
            return Response.success(feed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
}
