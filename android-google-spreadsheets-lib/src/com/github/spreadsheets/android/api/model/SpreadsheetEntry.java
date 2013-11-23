package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

public class SpreadsheetEntry extends Entry {

  @Key
  public Content content;

  @Key("author")
  public Author author;

  public String getWorksheetFeedLink() {
    return content.src;
  }

  @Override
  public SpreadsheetEntry clone() {
    return (SpreadsheetEntry) super.clone();
  }

}
