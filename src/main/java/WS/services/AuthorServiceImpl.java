package WS.services;

import DAO.BEANS.Author;
import DAO.DAOFactory;
import DAO.IAuthorDAO;
import DAO.IPartitionDAO;
import WS.errors.JsonErrorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class AuthorServiceImpl implements IAuthorService {

    private static AuthorServiceImpl _instance = null;

    private AuthorServiceImpl() {}

    public static AuthorServiceImpl getAuthorServiceImpl() {
        if(AuthorServiceImpl._instance == null) {
            AuthorServiceImpl._instance = new AuthorServiceImpl();
        }

        return AuthorServiceImpl._instance;
    }

    @Override
    public JsonObject getAuthorJson(int id) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        JsonObject jsonObj = null;
        Author author = authorDAO.getAuthor(id);

        if(author != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", author.getId());
            jsonObj.addProperty("name", author.getName());
        }

        return jsonObj;
    }

    @Override
    public JsonArray getAuthorsJson() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        JsonArray jsonArray = new JsonArray();

        List<Author> authors = authorDAO.getAuthors();
        for(Author author : authors) {
            JsonObject jsonObj = getAuthorJson(author.getId());
            jsonArray.add(jsonObj);
        }

        return jsonArray;
    }

    @Override
    public JsonObject updateAuthorJson(JsonObject jsonObject) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        JsonObject jsonResponse = null;

        if(isValidJson(jsonObject)) {
            Author currentAuthor = authorDAO.getAuthor(jsonObject.get("id").getAsInt());
            if(currentAuthor == null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "Author " + jsonObject.get("id").getAsString() + " does not exist");
                return jsonResponse;
            }

            if(authorDAO.getAuthor(jsonObject.get("name").getAsString()) != null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "The name specified is already in use. Author has not been updated");
                return jsonResponse;
            }

            currentAuthor.setName(jsonObject.get("name").getAsString());
            int status = authorDAO.updateAuthor(currentAuthor);
            if(status == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500,
                        "Author " + jsonObject.get("id").getAsString() + " has not been updated");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        200,
                        "Author " + jsonObject.get("id").getAsString() + " has been updated successfully");
            }
        }

        return jsonResponse;
    }

    @Override
    public JsonObject deleteAuthorJson(int id) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        JsonObject jsonResponse = null;

        Author author = authorDAO.getAuthor(id);
        if(author == null) {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Author not found");
            return jsonResponse;
        }

        // FIXME Add check for not deleting author if present in partition when partition is done
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();
        if(partitionDAO.GetPartitions(author).size() > 0) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    404, "Author cannot be deleted because referenced in partition(s)");
            return jsonResponse;
        }

        int status = authorDAO.removeAuthor(id);
        if(status == 0) {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    500,
                    "Author " + id + " has not been deleted");
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    200,
                    "Author " + id + " has been deleted successfully");
        }

        return jsonResponse;
    }

    @Override
    public JsonObject addAuthorJson(JsonObject author, String baseUrl) {
        JsonObject jsonResponse = null;
        if(author != null && author.has("name")) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            IAuthorDAO authorDAO = daoFactory.getAuthorDAO();

            if(authorDAO.getAuthor(author.get("name").getAsString()) != null) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        400,
                        "The name specified is already in use. Author has not been created");
                return jsonResponse;
            }

            Author newAuthor = new Author();
            newAuthor.setName(author.get("name").getAsString());
            int lastInsertId = authorDAO.addAuthor(newAuthor);
            if(lastInsertId == 0) {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        500,
                        "Author " + newAuthor.getName() + " has not been created");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(
                        201,
                        baseUrl + lastInsertId);
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(
                    400,
                    "Required field(s) missing. Author has not been created");
        }

        return jsonResponse;
    }

    public boolean isValidJson(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("id") &&
                jsonObj.has("name");
    }
}
