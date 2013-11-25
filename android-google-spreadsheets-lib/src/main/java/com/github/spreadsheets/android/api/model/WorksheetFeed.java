package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.List;


public class WorksheetFeed extends Feed {

  @Key("title")
  public String title;

  @Key("author")
  public Author author;

  @Key("entry")
  public List<WorksheetEntry> worksheets = new ArrayList<WorksheetEntry>();


  public List<WorksheetEntry> getEntries() {
    return worksheets;
  }

  public WorksheetEntry getWorksheetEntry(String title) {
    if (title != null) {
      for (WorksheetEntry we : worksheets) {
        if (title.equalsIgnoreCase(we.title)) {
          return we;
        }
      }
    }
    return null;
  }
}
