package WS.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class ServletUtils {

    public static void setResponseSettings(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    public static JsonObject readBody(HttpServletRequest request) throws Exception {

        StringBuilder buffer = new StringBuilder(640);
        String line;
        JsonObject jsonInput = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if(buffer.length() != 0) {
                JsonParser jsonParser = new JsonParser();
                jsonInput = (JsonObject)jsonParser.parse(buffer.toString());
            }
        } catch( Exception e) {
            throw e;
        }

        return jsonInput;
    }
}
