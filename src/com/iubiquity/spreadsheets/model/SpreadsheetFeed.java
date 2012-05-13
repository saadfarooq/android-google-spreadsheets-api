package com.iubiquity.spreadsheets.model;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.List;
 
public class SpreadsheetFeed extends Feed {

	@Key("entry")
	public List<SpreadsheetEntry> spreadsheets = new ArrayList<SpreadsheetEntry>();

	/**
	 * Returns a list of SpreadsheetEntries from the feed
	 * @return list of spread sheet entries in the feed
	 */
	public List<SpreadsheetEntry> getEntries() {
		return spreadsheets;
	}

	public SpreadsheetEntry getSpreadsheetEntry(final String title) {
		if (title != null) {
			final String s = title.trim();
			for (SpreadsheetEntry e : spreadsheets) {
				if (s.equals(e.title)) {
					return e;
				}
			}
		}
		return null;
	}

}
