package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.WorksheetEntry;
import com.github.spreadsheets.android.api.testutils.AssetsFileReader;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

@SmallTest
public class WorksheetEntryRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;
    Map<String, String> headers = new HashMap<String, String>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new WorksheetEntryRequest(null, null, null);
        String xml = new AssetsFileReader().assetFileContents("spreadsheet_entry.xml");
        headers.put(com.google.common.net.HttpHeaders.CONTENT_TYPE,
                "application/atom+xml; charset=UTF-8");
        mMockResponse = new NetworkResponse(200, xml.getBytes(), headers, false);
    }

    public void testWorksheetEntryParsingSuccess() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
    }

    public void testWorksheetEntryParsingFailure() {
        Response response = mRequest
                .parseNetworkResponse(
                        new NetworkResponse(200, "error".getBytes(), headers, false));
        assertFalse(response.isSuccess());
    }

    public void testWorksheetEntryParseNetworkResponse() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        WorksheetEntry entry = (WorksheetEntry) feedResponse.result;
        assertThat(entry.title).isNotNull();
        assertThat(entry.title).isEqualTo("custom_word_list");
    }
}
