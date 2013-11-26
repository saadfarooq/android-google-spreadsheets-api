package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.Feed;
import com.github.spreadsheets.android.api.model.SpreadsheetFeed;

import java.util.HashMap;
import java.util.Map;

@SmallTest
public class SpreadsheetRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;
    Map<String, String> headers = new HashMap<String, String>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new SpreadsheetRequest<Feed>(Request.Method.GET, null, null);

        String responseText = "<feed xmlns=\"http://www.w3.org/2005/Atom\"\n" +
                "    xmlns:openSearch=\"http://a9.com/-/spec/opensearch/1.1/\"\n" +
                "    xmlns:gs=\"http://schemas.google.com/spreadsheets/2006\"\n" +
                "    xmlns:gd=\"http://schemas.google.com/g/2005\"\n" +
                "    gd:etag=\"D0cERnk-eip7ImA9WBBXGEg.\">\n" +
                "  <id>https://spreadsheets.google.com/feeds/worksheets/key/private/full</id>\n" +
                "  <updated>2006-11-17T18:23:45.173Z</updated>\n" +
                "  <title type=\"text\">Groceries R Us</title>\n" +
                "  <link rel=\"alternate\" type=\"text/html\"\n" +
                "      href=\"https://spreadsheets.google.com/ccc?key=key\"/>\n" +
                "  <link rel=\"http://schemas.google.com/g/2005#feed\"\n" +
                "      type=\"application/atom+xml\"\n" +
                "      href=\"https://spreadsheets.google.com/feeds/worksheets/key/private/full\"/>\n" +
                "  <link rel=\"self\" type=\"application/atom+xml\"\n" +
                "      href=\"https://spreadsheets.google.com/feeds/worksheets/key/private/full\"/>\n" +
                "  <link rel=\"http://schemas.google.com/g/2005#post\" type=\"application/atom+xml\"\n" +
                "      href=\"https://spreadsheets.google.com/feeds/worksheets/key/private/full\"/>\n" +
                "  <author>\n" +
                "    <name>Fitzwilliam Darcy</name>\n" +
                "    <email>fitz@gmail.com</email>\n" +
                "  </author>\n" +
                "  <openSearch:totalResults>1</openSearch:totalResults>\n" +
                "  <openSearch:startIndex>1</openSearch:startIndex>\n" +
                "  <openSearch:itemsPerPage>1</openSearch:itemsPerPage>\n" +
                "  <entry gd:etag='\"YDwqeyI.\"'>\n" +
                "    <id>https://spreadsheets.google.com/feeds/worksheets/key/private/full/worksheetId</id>\n" +
                "    <updated>2006-11-17T18:23:45.173Z</updated>\n" +
                "    <title type=\"text\">Sheet1</title>\n" +
                "    <content type=\"text\">Sheet1</content>\n" +
                "    <link rel=\"http://schemas.google.com/spreadsheets/2006#listfeed\"\n" +
                "        type=\"application/atom+xml\"\n" +
                "        href=\"https://spreadsheets.google.com/feeds/list/key/worksheetId/private/full\"/>\n" +
                "    <link rel=\"http://schemas.google.com/spreadsheets/2006#cellsfeed\"\n" +
                "        type=\"application/atom+xml\"\n" +
                "        href=\"https://spreadsheets.google.com/feeds/cells/key/worksheetId/private/full\"/>\n" +
                "    <link rel=\"self\" type=\"application/atom+xml\"\n" +
                "        href=\"https://spreadsheets.google.com/feeds/worksheets/key/private/full/worksheetId\"/>\n" +
                "    <link rel=\"edit\" type=\"application/atom+xml\"\n" +
                "        href=\"https://spreadsheets.google.com/feeds/worksheets/key/private/full/worksheetId/version\"/>\n" +
                "    <gs:rowCount>100</gs:rowCount>\n" +
                "    <gs:colCount>20</gs:colCount>\n" +
                "  </entry>\n" +
                "</feed>";
        headers.put(com.google.common.net.HttpHeaders.CONTENT_TYPE, "application/atom+xml; charset=UTF-8");
        mMockResponse = new NetworkResponse(200, responseText.getBytes(), headers, false);
    }

    public void testParsingSuccess() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
    }

    public void testParsingFailure() {
        Response response = mRequest
                    .parseNetworkResponse(
                            new NetworkResponse(200,"error".getBytes(), headers, false));
        assertFalse(response.isSuccess());
    }

    public void testParseNetworkResponse() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        SpreadsheetFeed feed = (SpreadsheetFeed) feedResponse.result;
        assertEquals(feed.etag, "D0cERnk-eip7ImA9WBBXGEg.");
    }

}
