package WS.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IRoleService {

    public JsonObject getRoleJson(int id);
    public JsonArray getRolesJson();
    public JsonObject updateRoleJson(JsonObject role);
    public JsonObject deleteRoleJson(int id);
    public JsonObject addRoleJson(JsonObject role, String baseUrl);
}
