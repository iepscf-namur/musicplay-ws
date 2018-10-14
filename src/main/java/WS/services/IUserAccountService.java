package WS.services;

import com.google.gson.JsonObject;

public interface IUserAccountService {

    public boolean isCredentialsValid(JsonObject credentials);
}
