package WS.services;

import DAO.BEANS.Role;
import DAO.DAOFactory;
import DAO.IRoleDAO;
import DAO.IUserRoleDAO;
import WS.errors.JsonErrorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class RoleServiceImpl implements IRoleService {

    private static RoleServiceImpl _instance = null;

    private RoleServiceImpl() {}

    public static RoleServiceImpl getRoleServiceImpl() {
        if(RoleServiceImpl._instance == null) {
            RoleServiceImpl._instance = new RoleServiceImpl();
        }

        return RoleServiceImpl._instance;
    }

    @Override
    public JsonObject getRoleJson(int id) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();
        JsonObject jsonObj = null;
        Role role = roleDAO.getRole(id);

        if(role != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", role.getId());
            jsonObj.addProperty("name", role.getName());
        }

        return jsonObj;
    }

    @Override
    public JsonArray getRolesJson() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();
        JsonArray jsonArray = new JsonArray();

        List<Role> roles = roleDAO.getRoles();
        for(Role role : roles) {
            JsonObject jsonObj = getRoleJson(role.getId());
            jsonArray.add(jsonObj);
        }

        return jsonArray;
    }

    @Override
    public JsonObject updateRoleJson(JsonObject role) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();
        JsonObject jsonResponse = null;

        if(isValidJson(role)) {
            Role currentRole = roleDAO.getRole(role.get("id").getAsInt());
            if(currentRole == null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "Role " + role.get("id").getAsString() + " does not exist");
                return jsonResponse;
            }

            if(roleDAO.getRole(role.get("name").getAsString()) != null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "The name specified is already in use. Role has not been updated");
                return jsonResponse;
            }

            currentRole.setName(role.get("name").getAsString());
            int status = roleDAO.updateRole(currentRole);
            if(status == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500,
                        "Role " + role.get("id").getAsString() + " has not been updated");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        200,
                        "Role " + role.get("id").getAsString() + " has been updated successfully");
            }
        }

        return jsonResponse;

    }

    @Override
    public JsonObject deleteRoleJson(int id) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();
        JsonObject jsonResponse = null;

        Role role = roleDAO.getRole(id);
        if(role == null) {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Role not found");
            return jsonResponse;
        }

        IUserRoleDAO userRoleDAO = daoFactory.getUserRoleDAO();
        if(userRoleDAO.getRoleUsers(role).size() > 0) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    400,
                    "Role " + id + " cannot be deleted because it is assigned to users");
            return jsonResponse;
        }

        int status = roleDAO.removeRole(id);
        if(status == 0) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    500,
                    "Role " + id + " has not been deleted");
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    200,
                    "Role " + id + " has been deleted successfully");
        }

        return jsonResponse;

    }

    @Override
    public JsonObject addRoleJson(JsonObject role, String baseUrl) {
        JsonObject jsonResponse = null;
        if(role != null && role.has("name")) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            IRoleDAO roleDAO = daoFactory.getRoleDAO();

            if(roleDAO.getRole(role.get("name").getAsString()) != null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "The name specified is already in use. Role has not been created");
                return jsonResponse;
            }

            Role newRole = new Role();
            newRole.setName(role.get("name").getAsString());
            int lastInsertId = roleDAO.addRole(newRole);
            if(lastInsertId == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500,
                        "Role " + newRole.getName() + " has not been created");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        201,
                        baseUrl + lastInsertId);
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    400,
                    "Required field(s) missing. Role has not been created");
        }

        return jsonResponse;
    }

    public boolean isValidJson(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("id") &&
                jsonObj.has("name");
    }
}
