package com.iubiquity.spreadsheets.model;

import com.google.api.client.util.Key;

import java.util.List;

public abstract class Feed {

  @Key("link")
  public List<Link> links;

  @Key("@gd:etag")
  public String etag;
  
  public String getNextLink() {
    return Link.find(links, "next");
  }

  public String getBatchLink() {
    return Link.find(links, "http://schemas.google.com/g/2005#batch");
  }
}
