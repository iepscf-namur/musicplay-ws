package WS.services;

import DAO.BEANS.User;
import DAO.DAOFactory;
import DAO.UserDAO;
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
    public boolean isCredentialsValid(JsonObject credentials) {
        User user = null;
        if (credentials != null && credentials.has("login") && credentials.has("password")) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            UserDAO userDAO = daoFactory.getUserDAO();
            user = userDAO.GetUser(credentials.get("login").getAsString());
        }

        // TODO Implement Salted Pass
        return user != null && user.getPassword().equals(credentials.get("password").getAsString());
    }
}
