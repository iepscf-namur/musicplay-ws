package WS.services;

import DAO.BEANS.User;
import DAO.DAOFactory;
import DAO.IUserDAO;
import com.google.gson.JsonObject;

public class UserAccountServiceImpl implements IUserAccountService {

    private static UserAccountServiceImpl _instance = null;

    private UserAccountServiceImpl() {}

    public static UserAccountServiceImpl getUserAccountImpl() {
        if(UserAccountServiceImpl._instance == null) {
            UserAccountServiceImpl._instance = new UserAccountServiceImpl();
        }

        return UserAccountServiceImpl._instance;
    }

    @Override
    public JsonObject verifyCredentials(JsonObject credentials) {
        User user = null;
        JsonObject userJson = null;
        if (credentials != null && credentials.has("login") && credentials.has("password")) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            IUserDAO userDAO = daoFactory.getUserDAO();

            System.out.println("Login: " + credentials.get("login").getAsString() +
                    ", pass: " + credentials.get("password").getAsString() );
            user = userDAO.AuthUser(credentials.get("login").getAsString(), credentials.get("password").getAsString());

        }

        // TODO Implement Salted Pass

        if(user != null) {
            userJson = UserServiceImpl.getUserServiceImpl().getUserJson(credentials.get("login").getAsString());
            userJson.remove("password");
            userJson.remove("salt");
        }

        return userJson;
    }
}
