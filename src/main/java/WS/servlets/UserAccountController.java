package WS.servlets;

import DAO.BEANS.User;
import DAO.UserRoleDAOImpl;
import WS.errors.JsonErrorBuilder;
import WS.services.RoleServiceImpl;
import WS.services.UserAccountServiceImpl;
import WS.utils.ServletUtils;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAccountController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set Content-Type and CharacterEncoding in Header
        ServletUtils.setResponseSettings(response);

        JsonObject jsonResponse = null;
        // If request URL is /account/login or /account/login/
        if(request.getPathInfo() != null &&
                (request.getPathInfo().equals("/login") || request.getPathInfo().equals("/login/"))) {
            try {
                JsonObject jsonObj = ServletUtils.readBody(request);
                JsonObject user = UserAccountServiceImpl.getUserAccountImpl().verifyCredentials(jsonObj);
                if (user != null) {
                    // FIXME rename or extends JsonBuilder so we do not use "error" here
                    jsonResponse = JsonErrorBuilder.getJsonObject(200, "user credentials valid");
                    jsonResponse.add("user", user);
                } else {
                    jsonResponse = JsonErrorBuilder.getJsonObject(401, "user credentials invalid");
                }
            } catch (Exception e) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500, "An error occurred while reading the data provided");
                //e.printStackTrace();
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    404,
                    request.getServletPath() +
                            (request.getPathInfo() == null ? "" :  request.getPathInfo()) +
                            " is not a supported POST url");
        }

        response.setStatus(jsonResponse.get("code").getAsInt());
        response.getWriter().write(jsonResponse.toString());
    }
}
