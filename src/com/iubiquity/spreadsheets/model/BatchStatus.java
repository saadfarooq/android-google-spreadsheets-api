package com.iubiquity.spreadsheets.model;

import com.google.api.client.util.Key;

public class BatchStatus {

  @Key("@code")
  public int code;

  @Key("text()")
  public String content;

  @Key("@content-type")
  public String contentType;

  @Key("@reason")
  public String reason;

  public BatchStatus() {
  }
}
