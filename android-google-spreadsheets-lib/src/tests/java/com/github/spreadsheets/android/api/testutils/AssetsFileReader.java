package com.github.spreadsheets.android.api.testutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsFileReader {
    public String assetFileContents(final String fileName) throws IOException {
        StringBuilder text = new StringBuilder();
        InputStream fileStream = this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        String line;
        while((line = reader.readLine()) != null) {
            text.append(line);
        }
        return text.toString();
    }
}
