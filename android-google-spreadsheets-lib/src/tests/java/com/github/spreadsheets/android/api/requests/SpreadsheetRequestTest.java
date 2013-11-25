package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.Feed;

@SmallTest
public class SpreadsheetRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new SpreadsheetRequest<Feed>(Request.Method.GET, null, null);
        String responseText = "<entry gd:etag=\"\"FDHGCHnx12\"\">\n" +
                "  <id>https://spreadsheets.google.com/feeds/spreadsheets/tNXTXMh83yMWLVJfEgOWTvQ</id>\n" +
                "  <updated>2011-05-27T15:08:37.102Z</updated>\n" +
                "  <category scheme=\"http://schemas.google.com/spreadsheets/2006\" term=\"http://schemas.google.com/spreadsheets/2006#spreadsheet\"/>\n" +
                "  <title>My Spreadsheet</title>\n" +
                "  <content type=\"application/atom+xml;type=feed\" src=\"https://spreadsheets.google.com/feeds/worksheets/tNXTXMh83yMWLVJfEgOWTvQ/private/full\"/>\n" +
                "  <link rel=\"http://schemas.google.com/spreadsheets/2006#tablesfeed\" type=\"application/atom+xml\" href=\"https://spreadsheets.google.com/feeds/tNXTXMh83yMWLVJfEgOWTvQ/tables\"/>\n" +
                "  <link rel=\"alternate\" type=\"text/html\" href=\"https://spreadsheets.google.com/ccc?key=0Ak8c_1IVge120199weAHSjDHf1XTFZKZkVnT1dUdlE\"/>\n" +
                "  <link rel=\"self\" type=\"application/atom+xml\" href=\"https://spreadsheets.google.com/feeds/spreadsheets/private/full/tNXTXMh83yMWLVJfEgOWTvQ\"/>\n" +
                "  <author>\n" +
                "    <name>joe</name>\n" +
                "    <email>joe@yourdomain.com</email>\n" +
                "  </author>\n" +
                "</entry>";

        mMockResponse = new NetworkResponse(200, responseText.getBytes(), null, false);
    }

    public void testParsingSuccess() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
    }

    public void testParsingFailure() {
        Response response = mRequest
                    .parseNetworkResponse(
                            new NetworkResponse(200, "error".getBytes(), null, false));
        assertFalse(response.isSuccess());
    }

    public void testParseNetworkResponse() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
//        SpreadsheetFeed feed = (SpreadsheetFeed) feedResponse.result;
//        assertNull(feed);
    }

}
