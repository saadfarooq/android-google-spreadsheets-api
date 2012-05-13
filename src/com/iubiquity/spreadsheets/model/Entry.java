package com.iubiquity.spreadsheets.model;

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

//  public void executeDelete() throws IOException {
//    HttpRequest request = Util.TRANSPORT.buildDeleteRequest();
//    request.setUrl(getEditLink());
//    RedirectHandler.execute(request).ignore();
//  }

//  Entry executeInsert(SpreadsheetUrl url) throws IOException {
//    HttpRequest request = Util.TRANSPORT.buildPostRequest();
//    request.url = url;
//    AtomContent content = new AtomContent();
//    content.namespaceDictionary = Util.DICTIONARY;
//    content.entry = this;
//    request.content = content;
//    return RedirectHandler.execute(request).parseAs(getClass());
//  }

  public String getEditLink() {
    return Link.find(links, "edit");
  }
}
