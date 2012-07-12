package com.google.sf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.iubiquity.spreadsheets.model.Feed;
import com.iubiquity.spreadsheets.model.ListEntry;
import com.iubiquity.spreadsheets.model.ListFeed;
import com.iubiquity.spreadsheets.model.SpreadsheetEntry;
import com.iubiquity.spreadsheets.model.SpreadsheetFeed;
import com.iubiquity.spreadsheets.model.WorksheetEntry;
import com.iubiquity.spreadsheets.model.WorksheetFeed;
import com.modbusgooey.spreadsheets.AndroidSpreadSheetClient;
import com.modbusgooey.spreadsheets.AsyncSpreadsheetCaller;

/**
 * Android Activity class that demonstrates the usage of the <i>Spreadsheet-api
 * </i>in an Android project
 * 
 * @author Saad Farooq
 */
public class GoogleSpreadsheetActivity extends Activity implements AsyncSpreadsheetCaller {

	  private static final String TAG = "GoogleSpreadSheetActivity";

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		    Intent intent = new Intent(this, OAuthActivity.class);
		    startActivityForResult(intent, 0);
	  }
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (resultCode == Activity.RESULT_OK) {
			  String authToken = data.getStringExtra("Token");
			  try {
				getSpreadSheets(authToken);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }

	private void getSpreadSheets(String authToken) throws IOException {
		AndroidSpreadSheetClient client = new AndroidSpreadSheetClient(authToken, this);
		client.createClient(this, "[company-id]-[app-name]-[app-version]");
		
		SpreadsheetFeed spreadSheetFeed = null;
		
		try {
			spreadSheetFeed = client.getSpreadsheetMetafeed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for (SpreadsheetEntry entry : spreadSheetFeed.getEntries()) {
//			
//		}
		
		
		SpreadsheetEntry entry = spreadSheetFeed.getEntries().get(0);
		
		Toast.makeText(this, entry.getWorksheetFeedLink(), Toast.LENGTH_SHORT).show();
		
		WorksheetFeed worksheetFeed = client.executeGetWorksheetFeed(entry.getWorksheetFeedLink());
		
		List<WorksheetEntry> worksheetentries = worksheetFeed.getEntries();
		
		WorksheetEntry worksheetEntry = worksheetentries.get(0);
		
//		String cellfeedlink = worksheetEntry.getCellFeedLink();
//		CellFeed cellFeed = client.executeGetCellFeed(cellfeedlink);
//		List<CellEntry> cellEntries = cellFeed.getEntries();
		
		String listfeedLink = worksheetEntry.getListFeedLink();
//		List list = client.execute(new SpreadsheetUrl(listfeedLink));
		ListFeed listFeed = client.executeGetListFeed(listfeedLink);
		ListEntry listEntry = listFeed.getEntries().get(0);
//		String title = listEntry.title;
//		String content = listEntry.content;
		Map<String, String> cells = listEntry.getColumns();
		
		
		
		
		
		
		
		
		
	}

	public void onSpreadsheetResult(int requestCode, Feed feed) {
		// TODO Auto-generated method stub
		
	}

	public void onExceptionResponse(Exception e) {
		// TODO Auto-generated method stub
		
	}

}