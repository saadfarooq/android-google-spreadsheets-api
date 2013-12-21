package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.WorksheetEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class WorksheetEntryRequest extends SpreadsheetRequest<WorksheetEntry>{
    WorksheetEntryRequest(String url, Response.Listener<WorksheetEntry> listener,
                          Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<WorksheetEntry> parseNetworkResponse(NetworkResponse response) {
        try {
            WorksheetEntry entry = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , WorksheetEntry.class);
            return Response.success(entry, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
}
