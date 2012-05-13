package com.iubiquity.spreadsheets.model;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WorksheetData {

	public final CellFeed cellFeed;

	public final List<CellEntry> toInsert = Lists.newArrayList();

	public final SortedMap<Integer, SortedMap<Integer, CellEntry>> rows = new TreeMap<Integer, SortedMap<Integer, CellEntry>>();

	public WorksheetData(final CellFeed cellFeed) {
		this.cellFeed = cellFeed;
		populate(cellFeed.getEntries());
	}

	private void populate(List<CellEntry> cells) {
		for (CellEntry ce : cells) {
			Cell c = ce.cell;
			int row = c.row;
			SortedMap<Integer, CellEntry> rowValues = getRow(row);
			rowValues.put(c.col, ce);
		}
	}

	private CellEntry getCellEntry(int row, int col) {
		Map<Integer, CellEntry> rowValues = rows.get(row);
		if (rowValues == null) {
			return null;
		}
		return rowValues.get(col);
	}

	public String getContent(int row, int col) {
		CellEntry ce = getCellEntry(row, col);
		if (ce != null) {
			return ce.content;
		}
		return null;
	}

	public void setValue(String value, int row, int col) {
		CellEntry ce = getCellEntry(row, col);
		if (ce != null) {
			ce.content = value;
			ce.batchUpdate(value);
		} else {
			ce = CellEntry.makeInstance(value, row, col);
			ce.content = value;
			SortedMap<Integer, CellEntry> rowValues = getRow(row);
			rowValues.put(col, ce);
			toInsert.add(ce);
		}
	}

	private SortedMap<Integer, CellEntry> getRow(int row) {
		SortedMap<Integer, CellEntry> rowValues = rows.get(row);
		if (rowValues == null) {
			rowValues = new TreeMap<Integer, CellEntry>();
			rows.put(row, rowValues);
		}
		return rowValues;
	}
}
