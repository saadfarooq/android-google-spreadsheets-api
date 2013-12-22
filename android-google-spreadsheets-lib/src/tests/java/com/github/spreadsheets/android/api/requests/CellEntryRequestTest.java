package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.CellEntry;
import com.github.spreadsheets.android.api.testutils.AssetsFileReader;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

@SmallTest
public class CellEntryRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;
    Map<String, String> headers = new HashMap<String, String>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new CellEntryRequest(null, null, null);
        String xml = new AssetsFileReader().assetFileContents("cell_entry.xml");
        headers.put(com.google.common.net.HttpHeaders.CONTENT_TYPE,
                "application/atom+xml; charset=UTF-8");
        mMockResponse = new NetworkResponse(200, xml.getBytes(), headers, false);
    }

    public void testCellEntryParsingSuccess() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
    }

    public void testCellEntryParsingFailure() {
        Response response = mRequest
                .parseNetworkResponse(
                        new NetworkResponse(200,"error".getBytes(), headers, false));
        assertFalse(response.isSuccess());
    }

    public void testCellEntryParseNetworkResponse() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        CellEntry entry = (CellEntry) feedResponse.result;
        assertThat(entry.title).isNotNull();
        assertThat(entry.title).isEqualTo("A1");
        assertThat(entry.cell.value).isEqualTo("A1");
    }
}
