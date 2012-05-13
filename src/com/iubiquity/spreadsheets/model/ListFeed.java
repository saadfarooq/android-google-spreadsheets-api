package com.iubiquity.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.util.Key;

public class ListFeed extends Feed {

	@Key("title")
	public String title;
	
	@Key("author")
	public Author author;

	@Key("entry")
	public List<ListEntry> lists = new ArrayList<ListEntry>();

	public List<ListEntry> getEntries() {
		return lists;
	}

	public String getBatchError() {
		for (ListEntry ce : lists) {
			BatchStatus batchStatus = ce.batchStatus;
			if (batchStatus != null
					&& !HttpStatusCodes.isSuccess(batchStatus.code)) {
//				&& !HttpResponse.isSuccessStatusCode(batchStatus.code)) {
				return batchStatus.reason;
			}
		}
		return null;
	}

	public ListEntry getListEntry(final String title) {
		if (title != null) {
			final String s = title.trim();
			for (ListEntry e : lists) {
				if (s.equals(e.title)) {
					return e;
				}
			}
		}
		return null;
	}
}
