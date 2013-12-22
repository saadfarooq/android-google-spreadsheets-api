package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.List;

public class ListFeed extends Feed {
    @Key("title")
    public String title;

    @Key("author")
    public Author author;

    @Key("entry")
    public List<ListEntry> lists = new ArrayList<ListEntry>();

    public List<ListEntry> getEntries() {
        return lists;
    }

//    public ListEntry getListEntry(final String title) {
//        if (title != null) {
//            final String s = title.trim();
//            for (ListEntry e : lists) {
//                if (s.equals(e.title)) {
//                    return e;
//                }
//            }
//        }
//        return null;
//    }
}
