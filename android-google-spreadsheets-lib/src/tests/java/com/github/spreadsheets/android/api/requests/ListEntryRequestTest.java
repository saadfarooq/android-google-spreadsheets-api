package com.github.spreadsheets.android.api.requests;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.github.spreadsheets.android.api.model.ListEntry;
import com.github.spreadsheets.android.api.testutils.AssetsFileReader;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

@SmallTest
public class ListEntryRequestTest extends AndroidTestCase {
    private SpreadsheetRequest mRequest;
    private NetworkResponse mMockResponse;
    Map<String, String> headers = new HashMap<String, String>();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRequest = new ListEntryRequest(null, null, null);
        String xml = new AssetsFileReader().assetFileContents("list_entry.xml");
        headers.put(com.google.common.net.HttpHeaders.CONTENT_TYPE,
                "application/atom+xml; charset=UTF-8");
        mMockResponse = new NetworkResponse(200, xml.getBytes(), headers, false);
    }

    public void testListEntryParsingSuccess() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        assertTrue(feedResponse.isSuccess());
    }

    public void testListEntryParsingFailure() {
        Response response = mRequest
                .parseNetworkResponse(
                        new NetworkResponse(200,"error".getBytes(), headers, false));
        assertFalse(response.isSuccess());
    }

    public void testListEntryParseNetworkResponse() {
        Response feedResponse = mRequest.parseNetworkResponse(mMockResponse);
        ListEntry entry = (ListEntry) feedResponse.result;
//        assertThat(entry.getEditLink())
//                .isEqualTo("https://spreadsheets.google.com/feeds/list/tVYukALjr4jLjzBVH9xDGdQ/od6/private/full/cokwr/6ipgkep1hc");
//        assertThat(entry.getColumns()).isNotNull();
//        assertThat(entry.title).isNotNull();
//        assertThat(entry.title).isEqualTo("custom_word_list");
//        assertThat(entry.author.name).isEqualTo("sa");
//        assertThat(entry.author.email).isEqualTo("sa@dfarooq.com");
        for (Map.Entry<String, Object> e : entry.getValues()) {
            Log.d("Saad", e.getKey() + ", " + e.getValue());
        }
        assertThat(entry.getValues()).isNotNull();
    }
}
