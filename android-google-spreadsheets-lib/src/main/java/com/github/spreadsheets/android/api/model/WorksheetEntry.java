package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

public class WorksheetEntry extends Entry {

  public static final String CELL_FEED = "http://schemas.google.com/spreadsheets/2006#cellsfeed";
  public static final String LIST_FEED = "http://schemas.google.com/spreadsheets/2006#listfeed";

  @Key
  public Content content;

  @Key("app:edited")
  public String edited;

  @Override
  public WorksheetEntry clone() {
    return (WorksheetEntry) super.clone();
  }

  public String getListFeedLink() {
      return Link.find(links, LIST_FEED);
  }

  public String getCellFeedLink() {
    return Link.find(links, CELL_FEED);
  }

}
