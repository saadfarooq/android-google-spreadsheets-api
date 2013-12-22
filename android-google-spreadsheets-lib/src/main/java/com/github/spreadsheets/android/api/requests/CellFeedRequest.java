package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.CellFeed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CellFeedRequest extends SpreadsheetRequest<CellFeed> {

    public CellFeedRequest(String url, Listener<CellFeed> listener,
                                ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<CellFeed> parseNetworkResponse(NetworkResponse response) {
        try {
            CellFeed feed = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , CellFeed.class);
            return Response.success(feed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
}
