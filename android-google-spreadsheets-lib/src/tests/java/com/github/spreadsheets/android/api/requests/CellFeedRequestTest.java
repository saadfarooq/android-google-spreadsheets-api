package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.CellFeed;
import com.github.spreadsheets.android.api.testutils.AssetsFileReader;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

@SmallTest
public class CellFeedRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;
    Map<String, String> headers = new HashMap<String, String>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new CellFeedRequest(null, null, null);
        String xml = new AssetsFileReader().assetFileContents("cell_feed.xml");
        headers.put(com.google.common.net.HttpHeaders.CONTENT_TYPE,
                "application/atom+xml; charset=UTF-8");
        mMockResponse = new NetworkResponse(200, xml.getBytes(), headers, false);
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
        CellFeed feed = (CellFeed) feedResponse.result;
        assertThat(feed.getEntries().size()).isEqualTo(30);

        SparseArray<String> columns = new SparseArray<String>(3);
        columns.append(1, "A");
        columns.append(2, "B");
        columns.append(3, "C");
        for (int i = 1; i <= 10 ; i++) {
            for (int j = 1; j <= 3 ; j++) {
                assertThat(feed.getEntries().get(3 * (i - 1) + j - 1).title)
                        .isEqualTo(columns.get(j) + Integer.toString(i));
            }
        }
    }
}
