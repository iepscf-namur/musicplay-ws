package WS.services;

import com.google.gson.JsonObject;

public interface IUserAccountService {

    public JsonObject verifyCredentials(JsonObject credentials);
}
