package WS.servlets;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", 200);
        jsonObj.addProperty("message", "Test OK");
        jsonObj.addProperty("PathInfo", request.getPathInfo());
        jsonObj.addProperty("Servletpath", request.getServletPath());
        jsonObj.addProperty("ServletContext", request.getServletContext().getContextPath());
        jsonObj.addProperty("QueryString", request.getQueryString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().write(jsonObj.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", 200);
        jsonObj.addProperty("message", "Test OK");
        jsonObj.addProperty("PathInfo", request.getPathInfo());
        jsonObj.addProperty("Servletpath", request.getServletPath());
        jsonObj.addProperty("ServletContext", request.getServletContext().getContextPath());
        jsonObj.addProperty("QueryString", request.getQueryString());
        jsonObj.addProperty("FullQueryString",
                request.getScheme() + "/" +
                        request.getServerName() + ":" +
                        request.getServerPort() +
                        request.getServletPath() +
                        request.getPathInfo()
                );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().write(jsonObj.toString());
    }


}
