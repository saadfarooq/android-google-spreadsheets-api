package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.Key;

import java.util.List;


public class Entry implements Cloneable {

  @Key
  public String id;

  @Key
  public String title;

  @Key
  public String updated;

  @Key("link")
  public List<Link> links;

  @Override
  protected Entry clone() {
    return Data.clone(this);
  }

  public String getEditLink() {
    return Link.find(links, "edit");
  }
}
