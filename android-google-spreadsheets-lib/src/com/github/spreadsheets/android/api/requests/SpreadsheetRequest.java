package com.github.spreadsheets.android.api.requests;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.api.client.xml.XmlNamespaceDictionary;

public abstract class SpreadsheetRequest<T> extends Request<T> {
    public static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary()
            .set("", "http://www.w3.org/2005/Atom")
            .set("gd", "http://schemas.google.com/g/2005")
            .set("gs", "http://schemas.google.com/spreadsheets/2006")
            .set("gsx", "http://schemas.google.com/spreadsheets/2006/extended")
            .set("openSearch", "http://a9.com/-/spec/opensearch/1.1/")
            .set("xml", "http://www.w3.org/XML/1998/namespace")
            .set("app", "http://www.w3.org/2007/app");

    public static final String CODE_412 = "412 Precondition Failed";

    SpreadsheetRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {

    }
}
