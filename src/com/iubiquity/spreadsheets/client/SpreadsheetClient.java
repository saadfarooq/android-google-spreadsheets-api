package com.iubiquity.spreadsheets.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.http.xml.atom.AtomParser;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.common.collect.Lists;
import com.iubiquity.spreadsheets.model.CellEntry;
import com.iubiquity.spreadsheets.model.CellFeed;
import com.iubiquity.spreadsheets.model.Feed;
import com.iubiquity.spreadsheets.model.Link;
import com.iubiquity.spreadsheets.model.ListFeed;
import com.iubiquity.spreadsheets.model.SpreadsheetFeed;
import com.iubiquity.spreadsheets.model.SpreadsheetUrl;
import com.iubiquity.spreadsheets.model.WorksheetData;
import com.iubiquity.spreadsheets.model.WorksheetEntry;
import com.iubiquity.spreadsheets.model.WorksheetFeed;

abstract public class SpreadsheetClient {
	private static final String TAG = "SpreadsheetClient";

	public static final XmlNamespaceDictionary DICTIONARY = new XmlNamespaceDictionary()
			.set("", "http://www.w3.org/2005/Atom")
			.set("gd", "http://schemas.google.com/g/2005")
			.set("gs", "http://schemas.google.com/spreadsheets/2006")
			.set("gsx", "http://schemas.google.com/spreadsheets/2006/extended")
			.set("openSearch", "http://a9.com/-/spec/opensearch/1.1/")
			.set("xml", "http://www.w3.org/XML/1998/namespace")
			.set("app", "http://www.w3.org/2007/app");

	public static final String CODE_412 = "412 Precondition Failed";

	public HttpRequestFactory requestFactory;

	public void initializeParser(HttpRequest request) {
		AtomParser parser = new AtomParser(DICTIONARY);
		request.addParser(parser);
	}

	public void executeDelete(CellEntry cellEntry) throws IOException {
		HttpRequest request = requestFactory.buildDeleteRequest(new GenericUrl(
				cellEntry.getEditLink()));
		request.getHeaders().setIfMatch(cellEntry.etag);
		request.execute().ignore();
	}

	public CellEntry executeInsert(CellEntry entry, boolean matchTag)
			throws IOException {
		AtomContent content = AtomContent.forEntry(DICTIONARY, entry);
		HttpRequest request = requestFactory.buildPutRequest(
				new SpreadsheetUrl(entry.getEditLink()), content);

		if (matchTag) {
			// this will only insert if there has been no modification.
			request.getHeaders().setIfMatch(((CellEntry) entry).etag);
		} else {
			// this will only insert if there has been a modification
			request.getHeaders().setIfNoneMatch(((CellEntry) entry).etag);
		}
		return request.execute().parseAs(entry.getClass());
	}

	public CellEntry addCellEntry(CellEntry cellEntry,
			SpreadsheetUrl cellfeedUrl) throws IOException {
		AtomContent content = AtomContent.forEntry(DICTIONARY, cellEntry);
		HttpRequest request = requestFactory.buildPostRequest(cellfeedUrl,
				content);
		request.getHeaders().setIfNoneMatch(((CellEntry) cellEntry).etag);
		return request.execute().parseAs(cellEntry.getClass());
	}

	public CellFeed batchUpdate(final WorksheetData wd) throws IOException {
		String link = Link.find(wd.cellFeed.links, "self");
		SpreadsheetUrl url = new SpreadsheetUrl(link);
		for (CellEntry ce : wd.toInsert) {
			addCellEntry(ce, url);
		}
		return batchUpdate(wd.cellFeed);
	}

	public CellFeed batchUpdate(CellFeed feed) throws IOException {
		List<CellEntry> updatedCells = Lists.newArrayList();
		for (CellEntry ce : feed.cells) {
			if (ce.batchId != null) {
				updatedCells.add(ce);
			}
		}
		feed.cells = updatedCells;
		SpreadsheetUrl url = new SpreadsheetUrl(feed.getBatchLink());
//		AtomFeedContent content = new AtomFeedContent();
		AtomContent content = AtomContent.forFeed(DICTIONARY, feed);
//		content.namespaceDictionary = DICTIONARY;
//		content.feed = feed;
		HttpRequest request = requestFactory.buildPostRequest(url, content);
		request.getHeaders().setIfNoneMatch("whatever");
		return request.execute().parseAs(CellFeed.class);
	}

	public CellEntry addCell(WorksheetEntry we, String value, int row, int col)
			throws IOException {
		CellEntry ce = CellEntry.makeInstance(value, row, col);
		new CellEntry();
		String link = we.getCellFeedLink();
		return addCellEntry(ce, new SpreadsheetUrl(link));
	}

	public WorksheetEntry addWorksheet(WorksheetEntry worksheetEntry,
			SpreadsheetUrl worksheetfeedUrl) throws IOException {
		AtomContent content = AtomContent.forEntry(DICTIONARY, worksheetEntry);
		HttpRequest request = requestFactory.buildPostRequest(worksheetfeedUrl,
				content);
		// request.getHeaders().setIfNoneMatch(worksheetEntry.etag);
		return request.execute().parseAs(worksheetEntry.getClass());
	}

	public CellEntry executeInsert(CellEntry entry) throws IOException {
		return executeInsert(entry, true);
	}

	<F extends Feed> F executeGetFeed(SpreadsheetUrl url, final Class<F> feedClass)
			throws IOException {
		final HttpRequest request = requestFactory.buildGetRequest(url);
		return request.execute().parseAs(feedClass);
	}

	public HttpResponse execute(SpreadsheetUrl url) throws IOException {
		HttpRequest request = requestFactory.buildGetRequest(url);
		return request.execute();
	}

	public SpreadsheetFeed executeGetSpreadsheetFeed(SpreadsheetUrl url)
			throws IOException {
		return executeGetFeed(url, SpreadsheetFeed.class);
	}

	public CellFeed executeGetCellFeed(SpreadsheetUrl url) throws IOException {
		return executeGetFeed(url, CellFeed.class);
	}

	public CellFeed executeGetCellFeed(String url) throws IOException {
		return executeGetCellFeed(new SpreadsheetUrl(url));
	}
	
	
	public ListFeed executeGetListFeed(SpreadsheetUrl url) throws IOException {
		return executeGetFeed(url, ListFeed.class);
	}

	public ListFeed executeGetListFeed(String url) throws IOException {
		return executeGetListFeed(new SpreadsheetUrl(url));
	}

	public WorksheetFeed executeGetWorksheetFeed(SpreadsheetUrl url)
			throws IOException {
		return executeGetFeed(url, WorksheetFeed.class);
	}

	public WorksheetFeed executeGetWorksheetFeed(final String url)
			throws IOException {
		return executeGetWorksheetFeed(new SpreadsheetUrl(url));
	}

	public SpreadsheetFeed getSpreadsheetMetafeed() throws IOException {
		return executeGetSpreadsheetFeed(SpreadsheetUrl
				.forSpreadSheetMetafeed());
	}

	public WorksheetData getWorksheetData(final String cellFeedURL)
			throws IOException {
		return new WorksheetData(executeGetCellFeed(cellFeedURL));
	}

}
