package com.github.spreadsheets.android.api.model;

import com.google.api.client.util.Key;

public class List {
    @Key("gs:cell")
    public Cell cell;

    @Key
    public String content;

    @Key("@gd:etag")
    public String etag;

    @Key("batch:id")
    public String batchId;

    @Key("batch:operation")
    public BatchOperation batchOperation;

    @Key("batch:status")
    public BatchStatus batchStatus;

    public void batchUpdate(String value) {
        this.cell.value = value;
        this.batchId = this.id;
        this.batchOperation = BatchOperation.UPDATE;
    }

    public static CellEntry makeInstance(String value, int row, int col) {
        CellEntry ce = new CellEntry();
        ce.cell = new Cell();
        ce.cell.value = value;
        ce.cell.row = row;
        ce.cell.col = col;
        return ce;
    }
}
