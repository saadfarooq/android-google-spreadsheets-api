package com.iubiquity.spreadsheets.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.api.client.util.Key;
import com.google.api.client.xml.GenericXml;

public class ListEntry extends GenericXml {
	
	public static final String TAG = "ListEntry";

//	@Key("gs:cell")
//	public Cell cell;
	
	@Key
	public String title;
	
	@Key
	public String id;

	@Key
	public String content;

	@Key("@gd:etag")
	public String etag;

	@Key("batch:id")
	public String batchId;

	@Key("batch:operation")
	public BatchOperation batchOperation;

	@Key("batch:status")
	public BatchStatus batchStatus;

	public void batchUpdate(String value) {
		this.batchId = this.id;
		this.batchOperation = BatchOperation.UPDATE;
	}
	
	/**
	 * Returns the values of the columns as a Map using the Headers row values as keys
	 * @return map of column values of the rows
	 */
	public Map<String, String> getColumns() {
		
		Map<String, String> cells = new HashMap<String, String>();
		
		for (Entry<String, Object> entry : entrySet() ) {
			String key = entry.getKey();
			if (key.startsWith("gsx:")) {
//				Log.i(TAG, "Key: "+ key.substring(4));
//				if (entry.getValue() != null ) {
					cells.put(key.substring(4), (String) ((Map)((List)entry.getValue()).get(0)).get("text()"));
//				} else {
//					cells.put(key.substring(4), "");
//				}
				
//				((List)e.getValue()).get(0)).get("text()").toString(); 
//				    cells.put(key.substring(4), value); 
			}
		}
		
		return cells;
	} 

	/*public static ListEntry makeInstance(String value, int row, int col) {
		ListEntry ce = new ListEntry();
		ce.cell = new Cell();
		ce.cell.value = value;
		ce.cell.row = row;
		ce.cell.col = col;
		return ce;
	}*/
}
