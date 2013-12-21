package com.github.spreadsheets.android.api.requests;


import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.CellEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CellEntryRequest extends SpreadsheetRequest<CellEntry>{
    CellEntryRequest(String url, Response.Listener<CellEntry> listener,
                          Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<CellEntry> parseNetworkResponse(NetworkResponse response) {
        try {
            CellEntry entry = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , CellEntry.class);
            return Response.success(entry, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }
}
