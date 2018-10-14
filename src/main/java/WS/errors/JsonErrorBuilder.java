package WS.errors;

import com.google.gson.JsonObject;

public class JsonErrorBuilder {

    public static String getJsonString(int code, String message) {
        JsonObject jsonObj = JsonErrorBuilder.getJsonObject(code, message);
        return jsonObj.toString();
    }

    public static JsonObject getJsonObject(int code, String message) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", code);
        jsonObj.addProperty("message", message);

        return jsonObj;
    }
}
