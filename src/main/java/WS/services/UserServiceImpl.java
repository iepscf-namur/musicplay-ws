package WS.services;

import DAO.BEANS.User;
import DAO.DAOFactory;
import DAO.UserDAO;
import WS.errors.JsonErrorBuilder;
import WS.utils.EscapeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private static UserServiceImpl _instance = null;

    private UserServiceImpl() {}

    public static UserServiceImpl getUserServiceImpl() {
        if(UserServiceImpl._instance == null) {
            UserServiceImpl._instance = new UserServiceImpl();
        }

        return UserServiceImpl._instance;
    }

    @Override
    public JsonObject getUserJson(int id) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        JsonObject jsonObj = null;

        User user = userDAO.GetUser(id);
        if(user != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", user.getId());
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("salt", user.getSalt());
        }

        return jsonObj;
    }

    @Override
    public JsonArray getUsersJson() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        List<User> users = userDAO.GetUsers();

        JsonArray usersJsonArray = new JsonArray();

        for(User user : users) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("id", user.getId());
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("salt", user.getSalt());
            usersJsonArray.add(jsonObj);
        }

        return usersJsonArray;
    }

    @Override
    public JsonArray getUsersJson(JsonObject params) {
        return null;
    }

    @Override
    public JsonObject updateUserJson(JsonObject jsonObject) {

        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.GetUser(jsonObject.get("id").getAsInt());
        if(jsonObject != null && user != null) {
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("login")) {
                user.setLogin(EscapeUtils.html2text(jsonObject.get("login").getAsString()));
            }
            if(jsonObject.has("password")) {
                user.setPassword(jsonObject.get("password").getAsString());
            }
            if(jsonObject.has("salt")) {
                user.setSalt(jsonObject.get("salt").getAsString());
            }

            int updatedStatus = userDAO.UpdateUser(user);
            if(updatedStatus != 0) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "User " + user.getId() + " updated");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "User found but not updated");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "User not found");
        }

        return jsonResponse;
    }

    @Override
    public JsonObject deleteUserJson(int id) {
        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = userDAO.GetUser(id);
        if(user != null) {
            int nbRowsAffected = userDAO.DeleteUser(user.getId());
            if(nbRowsAffected > 0) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "User deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "User not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject addUserJson(JsonObject userJsonObj, String baseUrl) {
        if(!isValid(userJsonObj)) {
            return JsonErrorBuilder.getJsonObject(400, "required field missing");
        }

        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = new User();
        user.setLogin(EscapeUtils.html2text(userJsonObj.get("login").getAsString()));
        user.setPassword(userJsonObj.get("password").getAsString());
        user.setSalt(userJsonObj.get("salt").getAsString());
        // TODO DAO return status code
        int lastInsertID = userDAO.AddUser(user);
        if(lastInsertID != 0) {
            // FIXME rename or extends JsonBuilder so we do not use "error" here
            jsonResponse = JsonErrorBuilder.getJsonObject(201, baseUrl + lastInsertID);
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(400, "User not created");
        }

        return jsonResponse;
    }


    private boolean isValid(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("login") &&
                jsonObj.has("password") &&
                jsonObj.has("salt");
    }
}
