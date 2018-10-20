package WS.servlets;

import WS.errors.JsonErrorBuilder;

import WS.services.RoleServiceImpl;
import WS.utils.ServletUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set Content-Type and CharacterEncoding in Header
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;

        // If URL is different from /roles or /roles/
        if(request.getPathInfo() !=  null && !request.getPathInfo().equals("/")) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported POST url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        // Else read the body content and send it to RoleService for creation
        try {
            JsonObject role = ServletUtils.readBody(request);
            if(role == null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "body must contain a json string");
                response.setStatus(jsonResponse.get("code").getAsInt());
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            // Used to provide the complete path to the resource created if successful
            String baseUrl =
                    request.getScheme() + "://" +
                            request.getServerName() + ":" +
                            request.getServerPort() +
                            request.getServletPath() + "/";
            jsonResponse = RoleServiceImpl.getRoleServiceImpl().addRoleJson(role, baseUrl);

        } catch(Exception e) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    500,
                    "An error occurred while processing the data provided (POST RoleController)");
        }

        response.setStatus(jsonResponse.get("code").getAsInt());
        response.getWriter().write(jsonResponse.toString());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set Content-Type and CharacterEncoding in Header
        ServletUtils.setResponseSettings(response);

        // If request URL is /roles or /roles/
        if(request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            JsonArray jsonResponse = RoleServiceImpl.getRoleServiceImpl().getRolesJson();
            response.setStatus(200);
            response.getWriter().write(jsonResponse.toString());
            return;
        // If request URL is /roles{id} starting from 1 to ...
        } else if(request.getPathInfo().substring(1).matches("[1-9][0-9]*")) {
            JsonObject jsonResponse =  null;
            // Remove "/" at the beginning of the string
            String id = request.getPathInfo().substring(1);
            try {
                int roleId = Integer.parseInt(id);
                jsonResponse = RoleServiceImpl.getRoleServiceImpl().getRoleJson(roleId);

                if(jsonResponse == null) {
                    jsonResponse = JsonErrorBuilder.getJsonObject(404, "role not found");
                    response.setStatus(jsonResponse.get("code").getAsInt());
                    response.getWriter().write(jsonResponse.toString());
                } else {
                    response.setStatus(200);
                    response.getWriter().write(jsonResponse.toString());
                }
            } catch(NumberFormatException e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "id must be an integer");
                response.setStatus(jsonResponse.get("code").getAsInt());
                response.getWriter().write(jsonResponse.toString());
            }

            // else the request URL is not supported
        } else {
            JsonObject jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() + request.getPathInfo() + " is not a supported GET url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            response.getWriter().write(jsonResponse.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set Content-Type and CharacterEncoding in Header
        ServletUtils.setResponseSettings(response);

        // If request URL is not /roles{id} starting from 1 to ...
        if (request.getPathInfo() == null || !request.getPathInfo().substring(1).matches("[1-9][0-9]*")) {
            JsonObject jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() +
                            (request.getPathInfo() == null ? "" : request.getPathInfo()) +
                            " is not a supported PUT url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        JsonObject jsonResponse = null;
        // Remove "/" at the beginning of the string
        String id = request.getPathInfo().substring(1);
        try {
            int roleId = Integer.parseInt(id);
            JsonObject role = ServletUtils.readBody(request);
            if(role == null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(400, "body must contain a json string");
                response.setStatus(jsonResponse.get("code").getAsInt());
                response.getWriter().write(jsonResponse.toString());
                return;
            }
            role.addProperty("id", roleId);
            jsonResponse = RoleServiceImpl.getRoleServiceImpl().updateRoleJson(role);
        } catch (NumberFormatException e) {
            jsonResponse = JsonErrorBuilder.getJsonObject(400, "id must be an integer");
        } catch(Exception e2) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    500, "An error occurred while processing the data provided (PUT RoleController)");
        }
        response.setStatus(jsonResponse.get("code").getAsInt());
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set Content-Type and CharacterEncoding in Header
        ServletUtils.setResponseSettings(response);

        // If request URL is not /roles{id} starting from 1 to ...
        if (request.getPathInfo() == null || !request.getPathInfo().substring(1).matches("[1-9][0-9]*")) {
            JsonObject jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() +
                            (request.getPathInfo() == null ? "" : request.getPathInfo()) +
                            " is not a supported DELETE url");
            response.setStatus(jsonResponse.get("code").getAsInt());
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        JsonObject jsonResponse = null;
        // Remove "/" at the beginning of the string
        String id = request.getPathInfo().substring(1);
        try {
            int roleId = Integer.parseInt(id);
            jsonResponse = RoleServiceImpl.getRoleServiceImpl().deleteRoleJson(roleId);
        } catch (NumberFormatException e) {
            jsonResponse = JsonErrorBuilder.getJsonObject(400, "id must be an integer");
        }

        response.setStatus(jsonResponse.get("code").getAsInt());
        response.getWriter().write(jsonResponse.toString());
    }
}
