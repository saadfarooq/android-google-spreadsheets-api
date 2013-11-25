package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.github.spreadsheets.android.api.model.Feed;
import com.github.spreadsheets.android.api.model.SpreadsheetFeed;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.XmlObjectParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SpreadsheetRequest<T> extends Request<Feed> {
    public static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary()
            .set("", "http://www.w3.org/2005/Atom")
            .set("gd", "http://schemas.google.com/g/2005")
            .set("gs", "http://schemas.google.com/spreadsheets/2006")
            .set("gsx", "http://schemas.google.com/spreadsheets/2006/extended")
            .set("openSearch", "http://a9.com/-/spec/opensearch/1.1/")
            .set("xml", "http://www.w3.org/XML/1998/namespace")
            .set("app", "http://www.w3.org/2007/app");

    public static final String CODE_412 = "412 Precondition Failed";
    XmlObjectParser parser = new XmlObjectParser(DICTIONARY);

    SpreadsheetRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response<Feed> parseNetworkResponse(NetworkResponse response) {
        try {
            Feed feed = parser
                    .parseAndClose(new InputStreamReader(new ByteArrayInputStream(response.data))
                            , SpreadsheetFeed.class);

            return Response.success(feed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Feed response) {

    }
}
