package util;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONReader {

    public static Object readJson(String filePath) throws IOException, ParseException {
        // Read JSON file
        FileReader reader;
        reader = new FileReader(filePath);
        // Transform the Text  string in the File into Java Object
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
