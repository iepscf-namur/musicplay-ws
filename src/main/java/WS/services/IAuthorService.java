package WS.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IAuthorService {

    public JsonObject getAuthorJson(int id);
    public JsonArray getAuthorsJson();
    public JsonObject updateAuthorJson(JsonObject jsonObject);
    public JsonObject deleteAuthorJson(int id);
    public JsonObject addAuthorJson(JsonObject author, String baseUrl);
}
