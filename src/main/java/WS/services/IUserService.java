package WS.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IUserService {

    public JsonObject getUserJson(int id);
    public JsonObject getUserJson(String login);
    public JsonArray getUsersJson();
    public JsonArray getUsersJson(JsonObject params);
    public JsonObject updateUserJson(JsonObject jsonObject);
    public JsonObject deleteUserJson(int id);
    public JsonObject addUserJson(JsonObject user, String baseUrl);

}
