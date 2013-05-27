package com.iubiquity.spreadsheets.model;

import com.google.api.client.util.Key;

public class WorksheetEntry extends Entry {

  public static final String CELL_FEED = "http://schemas.google.com/spreadsheets/2006#cellsfeed";

  @Key
  public Content content;

  @Key("app:edited")
  public String edited;

	@Key("gs:rowCount")
	public int rowCount;

	@Key("gs:colCount")
	public int colCount;

	@Override
	public WorksheetEntry clone() {
		return (WorksheetEntry) super.clone();
	}

	public String getListFeedLink() {
		return content.src;
	}

	public String getCellFeedLink() {
		return Link.find(links, CELL_FEED);
	}

}
