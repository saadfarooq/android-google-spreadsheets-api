package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

public class BatchOperation {

  public static final BatchOperation INSERT = BatchOperation.of("insert");
  public static final BatchOperation QUERY = BatchOperation.of("query");
  public static final BatchOperation UPDATE = BatchOperation.of("update");
  public static final BatchOperation DELETE = BatchOperation.of("delete");

  @Key("@type")
  public String type;

  public BatchOperation() {
  }

  public static BatchOperation of(String type) {
    BatchOperation result = new BatchOperation();
    result.type = type;
    return result;
  }
}
