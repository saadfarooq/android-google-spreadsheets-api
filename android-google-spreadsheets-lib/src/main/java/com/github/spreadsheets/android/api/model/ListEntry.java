package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;
import com.google.api.client.xml.GenericXml;

import java.util.Map;
import java.util.Set;

public class ListEntry extends GenericXml {
    @Key("gsx")
    public Map<String,String> cols;

    public Set<Entry<String, Object>> getValues() {
//        Map<String, String> values = new HashMap<String,String>();
        return entrySet();
    }
}
