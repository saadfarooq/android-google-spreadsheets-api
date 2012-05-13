package com.iubiquity.spreadsheets.model;

import com.google.api.client.util.Key;

import java.util.List;

public class Link {

  @Key("@href")
  public String href;

  @Key("@rel")
  public String rel;

  public static String find(List<Link> links, String rel) {
    if (links != null) {
      for (Link link : links) {
        if (rel.equals(link.rel)) {
          return link.href;
        }
      }
    }
    return null;
  }
}
