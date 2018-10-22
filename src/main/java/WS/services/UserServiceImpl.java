package WS.services;

import DAO.*;
import DAO.BEANS.Role;
import DAO.BEANS.User;
import WS.errors.JsonErrorBuilder;
import WS.utils.EscapeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private final String DEFAULT_ROLE = "member";

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
        IUserDAO userDAO = daoFactory.getUserDAO();
        IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
        JsonObject jsonObj = null;

        User user = userDAO.GetUser(id);
        if(user != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", user.getId());
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("salt", user.getSalt());
            List<Role> roles = userRoleDAO.getUserRoles(user);
            JsonArray jsonRoles = new JsonArray();
            for(Role role : roles) {
                jsonRoles.add(role.getName());
            }
            jsonObj.add("roles", jsonRoles);
        }

        return jsonObj;
    }

    @Override
    public JsonObject getUserJson(String login) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IUserDAO userDAO = daoFactory.getUserDAO();
        IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
        JsonObject jsonObj = null;

        User user = userDAO.GetUser(login);
        if(user != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", user.getId());
            jsonObj.addProperty("login", user.getLogin());
            jsonObj.addProperty("password", user.getPassword());
            jsonObj.addProperty("salt", user.getSalt());
            List<Role> roles = userRoleDAO.getUserRoles(user);
            JsonArray jsonRoles = new JsonArray();
            for(Role role : roles) {
                jsonRoles.add(role.getName());
            }
            jsonObj.add("roles", jsonRoles);
        }

        return jsonObj;
    }

    @Override
    public JsonArray getUsersJson() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IUserDAO userDAO = daoFactory.getUserDAO();
        List<User> users = userDAO.GetUsers();

        JsonArray usersJsonArray = new JsonArray();

        // FIXME Avoid doing a request for each user !!!
        for(User user : users) {
            usersJsonArray.add(getUserJson(user.getId()));
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
        IUserDAO userDAO = daoFactory.getUserDAO();

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
            if(jsonObject.has("roles")) {
                IRoleDAO roleDAO = daoFactory.getRoleDAO();
                IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
                List<Role> currentRoles = userRoleDAO.getUserRoles(user);
                JsonArray newRoles = jsonObject.getAsJsonArray("roles");

                // Add missing roles present in json
                for(JsonElement newRole : newRoles) {
                    boolean isRoleAbsent = true;
                    for(Role currentRole: currentRoles) {
                        if(currentRole.getName().equals(newRole.getAsString())) {
                            isRoleAbsent = false;
                            break;
                        }
                    }
                    if(isRoleAbsent) {
                        Role roleToAdd = roleDAO.getRole(newRole.getAsString());
                        if(roleToAdd == null) {
                            jsonResponse = JsonErrorBuilder.getJsonObject(
                                    400,
                                    "User not updated because role " + newRole.getAsString() + " does not exist");
                            return jsonResponse;
                        }
                        int updateStatus = userRoleDAO.addUserRole(user, roleToAdd);
                        if(updateStatus == 0) {
                            jsonResponse = JsonErrorBuilder.getJsonObject(
                                    500, "Role " + roleToAdd.getName() + " could not be added");
                            return jsonResponse;
                        }
                    }
                }

                // Remove roles missing in json
                for(Role currentRole : currentRoles) {
                    boolean isRolePresent = false;
                    for(JsonElement newRole : newRoles) {
                        if(currentRole.getName().equals(newRole.getAsString())) {
                            isRolePresent = true;
                            break;
                        }
                    }
                    if(!isRolePresent) {
                        int updateStatus = userRoleDAO.removeUserRole(user, currentRole);
                        if(updateStatus == 0) {
                            jsonResponse = JsonErrorBuilder.getJsonObject(
                                    500, "Role " + currentRole.getName() + " could not be removed");
                            return jsonResponse;
                        }
                    }
                }
            }

            //int updatedStatus = userDAO.UpdateUser(user);
            if(userDAO.UpdateUser(user)) {
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
                IUserDAO userDAO = daoFactory.getUserDAO();
                IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();

                User user = userDAO.GetUser(id);
                if(user != null) {
                    List<Role> userRoles = userRoleDAO.getUserRoles(user);
                    for(Role role : userRoles) {
                        int deletionStatus = userRoleDAO.removeUserRole(user, role);
                if(deletionStatus == 0) {
                    jsonResponse = JsonErrorBuilder.getJsonObject(
                            500,
                            "User not deleted because role " + role.getName() + " could not be removed");
                    return jsonResponse;
                }
            }
            //int nbRowsAffected = userDAO.DeleteUser(user.getId());
            if(userDAO.DeleteUser(user.getId())) {
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
            return JsonErrorBuilder.getJsonObject(
                    400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        JsonObject jsonResponse = null;
        int lastInsertId = 0;
        int roleUserInsertStatus = 0;
        DAOFactory daoFactory = DAOFactory.getInstance();
        IUserDAO userDAO = daoFactory.getUserDAO();

        // Do not allow user creation if login already exists
        if(userDAO.GetUser(userJsonObj.get("login").getAsString()) != null) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    400,
                    "User not created because login " + userJsonObj.get("login").getAsString() + " is already used");
            return jsonResponse;
        }

        IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();

        User user = new User();
        user.setLogin(EscapeUtils.html2text(userJsonObj.get("login").getAsString()));
        user.setPassword(userJsonObj.get("password").getAsString());
        //user.setSalt(userJsonObj.get("salt").getAsString());

        // If roles is not set in the json object, we assign the default role to the user
        if(!userJsonObj.has("roles")) {
            Role role = roleDAO.getRole(DEFAULT_ROLE);
            if(role == null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400, "User not created because role " + DEFAULT_ROLE + " does not exist");
                return jsonResponse;
            }

            // We insert the user and check if it has been correctly inserted into the db
            lastInsertId = userDAO.AddUser(user);
            if(lastInsertId == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(500, "User not created");
                return jsonResponse;
            }

            // We get the db generated id assigned to the new user to link the user with its role
            user.setId(lastInsertId);
            roleUserInsertStatus = userRoleDAO.addUserRole(user, role);
            // If the link cannot be created between the user and the role, we suppress the user created
            if(roleUserInsertStatus == 0) {
                userDAO.DeleteUser(lastInsertId);
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "User not created because role could not be assigned");
                return jsonResponse;
            }
        // If roles is set in the json object
        } else {
            JsonArray roles = userJsonObj.getAsJsonArray("roles");
            Role role = null;
            List<Role> userRolesToAdd = new ArrayList<>();

            // We check if each role provided in the json object exists in the db
            for(JsonElement roleName : roles) {
                role = roleDAO.getRole(roleName.getAsString());
                if(role == null) {
                    jsonResponse = JsonErrorBuilder.getJsonObject(
                            400,
                            "User not created because role " + roleName.getAsString() + " does not exist");
                    return jsonResponse;
                }

                // We check that the same role is not assigned to the user more than once
                for(Role userRoleToAdd : userRolesToAdd) {
                    if(userRoleToAdd.getName().equals(roleName.getAsString())) {
                        jsonResponse = JsonErrorBuilder.getJsonObject(
                                400,
                                "User not created because role " + roleName.getAsString() +
                                        " can only be assigned once");
                        return jsonResponse;
                    }
                }

                // If checks are OK, we add the role to a list that is going to be used next
                userRolesToAdd.add(role);
            }

            // We insert the user into the DB and verify the insertion
            lastInsertId = userDAO.AddUser(user);
            if(lastInsertId == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(500, "User not created");
                return jsonResponse;
            }

            // We set the db auto-increment generated key to the user so we can link it with its role(s)
            user.setId(lastInsertId);
            for(Role userRoleToAdd: userRolesToAdd) {
                roleUserInsertStatus = userRoleDAO.addUserRole(user, userRoleToAdd);
                // If a role cannot be assigned, we removed all the previously roles added and delete the user
                if(roleUserInsertStatus == 0) {

                    for(Role userRoleToRemove : userRoleDAO.getUserRoles(user)) {
                        userRoleDAO.removeUserRole(user, userRoleToRemove);
                    }
                    userDAO.DeleteUser(lastInsertId);
                    jsonResponse = JsonErrorBuilder.getJsonObject(
                            500,
                            "User not created because role " + userRoleToAdd + " could not be assigned");
                    return jsonResponse;
                }
            }
        }

        // FIXME rename or extends JsonBuilder so we do not use "error" here
        jsonResponse = JsonErrorBuilder.getJsonObject(201, baseUrl + lastInsertId);

        return jsonResponse;
    }

    private boolean isValid(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("login") &&
                jsonObj.has("password") &&
                //jsonObj.has("salt") &&
                (jsonObj.has("roles") ? jsonObj.get("roles").isJsonArray() : true);
    }
}
