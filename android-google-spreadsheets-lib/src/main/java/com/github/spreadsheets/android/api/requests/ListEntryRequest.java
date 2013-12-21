package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.ListEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ListEntryRequest extends SpreadsheetRequest<ListEntry>{
    ListEntryRequest(String url, Response.Listener<ListEntry> listener,
                          Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<ListEntry> parseNetworkResponse(NetworkResponse response) {
        try {
            ListEntry entry = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , ListEntry.class);
            return Response.success(entry, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
}
