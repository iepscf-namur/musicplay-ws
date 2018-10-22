package WS.services;

import DAO.*;
import DAO.BEANS.*;
import WS.errors.JsonErrorBuilder;
import WS.utils.DateTimeUtils;
import WS.utils.EscapeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.*;

public class PartitionServiceImpl implements IPartitionService {

    private final boolean DEFAULT_USER_VALIDATION = false;
    private final boolean DEFAULT_MODERATOR_VALIDATION = false;
    private final String DEFAULT_IMAGE = "defaultPartition.png";

    private static PartitionServiceImpl _instance = null;

    private PartitionServiceImpl() {}

    public static PartitionServiceImpl getPartitionServiceImpl() {
        if(PartitionServiceImpl._instance == null) {
            PartitionServiceImpl._instance = new PartitionServiceImpl();
        }

        return PartitionServiceImpl._instance;
    }

    @Override
    public JsonObject getPartitionJson(int id) {

        DAOFactory daoFactory = DAOFactory.getInstance();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();
        JsonObject jsonObj = null;

        Partition partition = partitionDAO.GetPartition(id);
        if(partition != null) {
            jsonObj = new JsonObject();
            jsonObj.addProperty("id", partition.getId());
            jsonObj.addProperty("title", partition.getTitle());
            jsonObj.addProperty("urlImage", partition.getUrlImage());
            jsonObj.addProperty("creator", partition.getCreator().getLogin());
            jsonObj.addProperty("author", partition.getAuthor().getName());
            jsonObj.addProperty("userValidation", partition.isUserValidation());
            jsonObj.addProperty("moderatorValidation", partition.isModeratorValidation());
            jsonObj.addProperty("creationDate", partition.getCreationDate().toString());
            jsonObj.addProperty("modificationDate", partition.getModificationDate().toString());

            List<Strophe> strophes = partition.getStrophes();
            JsonArray jsonStrophes = new JsonArray();
            for(Strophe strophe : strophes) {
                JsonObject jsonStrophe = new JsonObject();
                jsonStrophe.addProperty("position", strophe.getPosition());
                List<Ligne> lignes = strophe.getLignes();
                JsonArray jsonLignes = new JsonArray();
                for(Ligne ligne : lignes) {
                    JsonObject jsonLigne = new JsonObject();
                    jsonLigne.addProperty("id", ligne.getId());
                    jsonLigne.addProperty("accord", ligne.getAccord());
                    jsonLigne.addProperty("text", ligne.getText());
                    jsonLigne.addProperty("position", ligne.getPosition());
                    jsonLignes.add(jsonLigne);
                }
                jsonStrophe.add("lignes", jsonLignes);
                jsonStrophes.add(jsonStrophe);
            }

            jsonObj.add("strophes", jsonStrophes);
        }

        return jsonObj;
    }

    @Override
    public JsonArray getPartitionsJson() {

        DAOFactory daoFactory = DAOFactory.getInstance();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();
        List<Partition> partitions = partitionDAO.GetPartitions();

        JsonArray partitionsJsonArray = new JsonArray();

        for(Partition partition : partitions) {

            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("id", partition.getId());
            jsonObj.addProperty("title", partition.getTitle());
            jsonObj.addProperty("urlImage", partition.getUrlImage());
            jsonObj.addProperty("creator", partition.getCreator().getLogin());
            jsonObj.addProperty("author", partition.getAuthor().getName());
            jsonObj.addProperty("userValidation", partition.isUserValidation());
            jsonObj.addProperty("moderatorValidation", partition.isModeratorValidation());
            jsonObj.addProperty("creationDate", partition.getCreationDate().toString());
            jsonObj.addProperty("modificationDate", partition.getModificationDate().toString());

            List<Strophe> strophes = partition.getStrophes();
            JsonArray jsonStrophes = new JsonArray();
            for(Strophe strophe : strophes) {
                JsonObject jsonStrophe = new JsonObject();
                jsonStrophe.addProperty("position", strophe.getPosition());
                List<Ligne> lignes = strophe.getLignes();
                JsonArray jsonLignes = new JsonArray();
                for(Ligne ligne : lignes) {
                    JsonObject jsonLigne = new JsonObject();
                    jsonLigne.addProperty("id", ligne.getId());
                    jsonLigne.addProperty("accord", ligne.getAccord());
                    jsonLigne.addProperty("text", ligne.getText());
                    jsonLigne.addProperty("position", ligne.getPosition());
                    jsonLignes.add(jsonLigne);
                }
                jsonStrophe.add("lignes", jsonLignes);
                jsonStrophes.add(jsonStrophe);
            }

            jsonObj.add("strophes", jsonStrophes);

            partitionsJsonArray.add(jsonObj);
        }

        return partitionsJsonArray;
    }

    @Override
    public JsonArray getPartitionsByAuthorJson(JsonObject author) {
        return null;
    }

    @Override
    public JsonArray getPartitionsByCreatorJson(JsonObject creator) {
        return null;
    }

    @Override
    public JsonObject updatePartitionJson(JsonObject jsonObject) {

        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();

        Partition partition = partitionDAO.GetPartition(jsonObject.get("id").getAsInt());
        if(jsonObject != null && partition != null) {
            // TODO Send message if field provided does not exist ?
            if(jsonObject.has("title")) {
                partition.setTitle(EscapeUtils.html2text(jsonObject.get("title").getAsString()));
            }

            if(jsonObject.has("urlImage")) {
                //partition.setUrlImage(EscapeUtils.html2text(jsonObject.get("urlImage").getAsString()));
                partition.setUrlImage(jsonObject.get("urlImage").getAsString());
            }

            if(jsonObject.has("userValidation")) {
                partition.setUserValidation(Boolean.parseBoolean(jsonObject.get("userValidation").getAsString()));
            }

            if(jsonObject.has("moderatorValidation")) {
                partition.setModeratorValidation(Boolean.parseBoolean(jsonObject.get("moderatorValidation").getAsString()));
            }

            if(jsonObject.has("strophes")) {

                if(!jsonObject.get("strophes").isJsonArray()) {
                    return JsonErrorBuilder.getJsonObject(400, "strophes must be an array");
                }

                JsonArray strophesJson = jsonObject.getAsJsonArray("strophes");
                IStropheDAO stropheDAO = daoFactory.getStropheDAO();
                ILigneDAO ligneDAO = daoFactory.getLigneDAO();
                JsonArray lignesJson = null;

                for(JsonElement stropheJson : strophesJson) {
                    if(((JsonObject)stropheJson).has("position")) {
                        try {
                            Integer.parseInt(((JsonObject)stropheJson).get("position").getAsString());
                        } catch(NumberFormatException e) {
                            return JsonErrorBuilder.getJsonObject(400, "Strophe position must be an integer");
                        }

                        int position = ((JsonObject)stropheJson).get("position").getAsInt();
                        Strophe strophe = stropheDAO.getStropheAt(position, partition);

                        if(strophe == null) {
                            return JsonErrorBuilder.getJsonObject(404, "Strophe at position " + position + " not found");
                        }

                        if(!((JsonObject)stropheJson).has("lignes")) {
                            return JsonErrorBuilder.getJsonObject(400, "strophes must contain an array of ligne(s)");
                        }

                        if(!((JsonObject) stropheJson).get("lignes").isJsonArray()) {
                            return JsonErrorBuilder.getJsonObject(400, "lignes must be an array");
                        }

                        lignesJson = ((JsonObject) stropheJson).getAsJsonArray("lignes");
                        for(JsonElement ligneJson : lignesJson) {
                            if(!((JsonObject)ligneJson).has("position")) {
                                return JsonErrorBuilder.getJsonObject(
                                        400, "lignes must have a position");
                            }
                            try {
                                Integer.parseInt(((JsonObject)ligneJson).get("position").getAsString());
                            } catch(NumberFormatException e) {
                                return JsonErrorBuilder.getJsonObject(
                                        400, "Ligne position must be an integer");
                            }
                            int linePosition = ((JsonObject) ligneJson).get("position").getAsInt();
                            Ligne ligne = ligneDAO.getLigneAt(linePosition, strophe);
                            if(ligne == null) {
                                return JsonErrorBuilder.getJsonObject(
                                        404, "Ligne position " + linePosition + " not found");
                            }
                        }

                    } else {
                        return JsonErrorBuilder.getJsonObject(400, "Strophe must have a position");
                    }
                }

                // All checks done, we make the modifications
                for(JsonElement stropheJson : strophesJson) {
                    Strophe strophe = stropheDAO.getStropheAt(((JsonObject)stropheJson).get("position").getAsInt(), partition);
                    lignesJson = stropheJson.getAsJsonObject().getAsJsonArray("lignes");

                    for(JsonElement ligneJson : lignesJson) {

                        Ligne ligne = ligneDAO.getLigneAt(ligneJson.getAsJsonObject().get("position").getAsInt(), strophe);

                        if(ligneJson.getAsJsonObject().has("accord")) {
                            ligne.setAccord(EscapeUtils.html2text(ligneJson.getAsJsonObject().get("accord").getAsString()));
                        }
                        if(ligneJson.getAsJsonObject().has("text")) {
                            ligne.setText(EscapeUtils.html2text(ligneJson.getAsJsonObject().get("text").getAsString()));
                        }
                        ligneDAO.updateLigne(ligne);
                    }
                }

                partition.setModificationDate(DateTimeUtils.getSqlCurrentDate());
                partitionDAO.UpdatePartition(partition);

                jsonResponse = JsonErrorBuilder.getJsonObject(
                        200, "Partition successfully updated");

            } else { // no "strophes" member in json
                partition.setModificationDate(DateTimeUtils.getSqlCurrentDate());
                partitionDAO.UpdatePartition(partition);

                jsonResponse = JsonErrorBuilder.getJsonObject(
                        200, "Partition title successfully updated");
            }

        } else { //jsonobject or partition is null
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Partition not found");
        }

        return jsonResponse;
    }

    @Override
    public JsonObject deletePartitionJson(int id) {

        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();
        IStropheDAO stropheDAO = daoFactory.getStropheDAO();
        ILigneDAO ligneDAO = daoFactory.getLigneDAO();

        Partition partition = partitionDAO.GetPartition(id);
        if(partition != null) {
            List<Strophe> strophes = stropheDAO.getStrophes(partition);
            for(Strophe strophe : strophes) {
                //List<Ligne> lignes = ligneDAO.getLignes(strophe);
                int deletionStatus = ligneDAO.removeLignes(strophe);
                //FIXME check deletion status
                /*
                if(deletionStatus == 0) {
                    jsonResponse = JsonErrorBuilder.getJsonObject(
                            500,
                            "User not deleted because role " + role.getName() + " could not be removed");
                    return jsonResponse;
                }
                */
            }
            //FIXME check deletion status
            stropheDAO.removeStrophes(partition);
            int nbRowsAffected = partitionDAO.DeletePartition(partition.getId());
            if(nbRowsAffected > 0) {
                // FIXME rename or extends JsonBuilder so we do not use "error" here
                jsonResponse = JsonErrorBuilder.getJsonObject(200, "Partition deleted");
            } else {
                jsonResponse = JsonErrorBuilder.getJsonObject(500, "Partition not deleted");
            }
        } else {
            jsonResponse = JsonErrorBuilder.getJsonObject(404, "Partition not found");
        }
        return jsonResponse;
    }

    @Override
    public JsonObject addPartitionJson(JsonObject partition, String baseUrl) {
        if(!isValidPartition(partition)) {
            return JsonErrorBuilder.getJsonObject(
                    400,
                    "required field missing or incorrectly formatted, please check the requirements");
        }

        JsonObject jsonResponse = null;
        DAOFactory daoFactory = DAOFactory.getInstance();
        IUserDAO userDAO = daoFactory.getUserDAO();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        IStropheDAO stropheDAO = daoFactory.getStropheDAO();
        ILigneDAO ligneDAO = daoFactory.getLigneDAO();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();

        Author author = authorDAO.getAuthor(partition.get("author").getAsInt());
        if(author == null) {
            return JsonErrorBuilder.getJsonObject(
                    400,
                    "The author id specified does not exist. Partition not created");
        }

        User user = userDAO.GetUser(partition.get("creator").getAsInt());
        if(user == null) {
            return JsonErrorBuilder.getJsonObject(
                    400,
                    "The creator id specified does not exist. Partition not created");
        }

        Partition newPartition = new Partition();
        newPartition.setTitle(partition.get("title").getAsString());
        newPartition.setUrlImage(DEFAULT_IMAGE);
        newPartition.setUserValidation(DEFAULT_USER_VALIDATION);
        newPartition.setModeratorValidation(DEFAULT_MODERATOR_VALIDATION);
        newPartition.setAuthor(author);
        newPartition.setCreator(user);
        newPartition.setCreationDate(DateTimeUtils.getSqlCurrentDate());
        newPartition.setModificationDate(newPartition.getCreationDate());

        int lastPartitionInsertId = partitionDAO.AddPartition(newPartition);
        if(lastPartitionInsertId == 0) {
            return JsonErrorBuilder.getJsonObject(
                    500,
                    "Partition could not be created");
        }

        newPartition.setId(lastPartitionInsertId);
        JsonArray strophes = partition.get("strophes").getAsJsonArray();
        int lastStropheInsertId = 0;
        int strophePosition = 1;
        for(JsonElement strophe : strophes) {
            Strophe newStrophe = new Strophe();
            newStrophe.setPosition(strophePosition++);
            lastStropheInsertId = stropheDAO.addStrophe(newStrophe, newPartition);
            if(lastStropheInsertId == 0) {
                if(rollbackAddPartition(newPartition)) {
                    return JsonErrorBuilder.getJsonObject(
                            500,
                            "Strophe positioned at " + newStrophe.getPosition() + " could not be created. Aborted.");
                }

                return JsonErrorBuilder.getJsonObject(
                        500,
                        "Strophe positioned at " + newStrophe.getPosition() + " could not be created. " +
                                "Could not be aborted, DB might be in an inconsistent state.");
            }

            newStrophe.setId(lastStropheInsertId);
            JsonArray lignes = ((JsonObject) strophe).get("lignes").getAsJsonArray();
            int lastLigneInsertId = 0;
            int lignePosition = 1;
            for(JsonElement ligne : lignes) {
                Ligne newLigne = new Ligne();
                newLigne.setAccord(((JsonObject)ligne).get("accord").getAsString());
                newLigne.setText(((JsonObject)ligne).get("text").getAsString());
                newLigne.setPosition(lignePosition++);
                lastLigneInsertId = ligneDAO.addLigne(newLigne, newStrophe);
                if(lastLigneInsertId == 0) {
                    if(rollbackAddPartition(newPartition)) {
                        return JsonErrorBuilder.getJsonObject(
                                500,
                                "Ligne positioned at " + newLigne.getPosition() + " could not be created. Aborted.");
                    }

                    return JsonErrorBuilder.getJsonObject(
                            500,
                            "Ligne positioned at " + newLigne.getPosition() + " could not be created. " +
                                    "Could not be aborted, DB might be in an inconsistent state.");
                }
            }
        }

        // FIXME rename or extends JsonBuilder so we do not use "error" here
        jsonResponse = JsonErrorBuilder.getJsonObject(201, baseUrl + lastPartitionInsertId);

        return jsonResponse;
    }

    private boolean rollbackAddPartition(Partition partition) {
        DAOFactory daoFactory = DAOFactory.getInstance();
        IStropheDAO stropheDAO = daoFactory.getStropheDAO();
        ILigneDAO ligneDAO = daoFactory.getLigneDAO();
        IPartitionDAO partitionDAO = daoFactory.getPartitionDAO();
        boolean deletionStatus = true;

        for(Strophe stropheToRemove : stropheDAO.getStrophes(partition)) {
            deletionStatus = deletionStatus && ligneDAO.removeLignes(stropheToRemove) > 0;
        }
        if(deletionStatus) {
            deletionStatus = stropheDAO.removeStrophes(partition) > 0;
        }
        if(deletionStatus) {
            deletionStatus = partitionDAO.DeletePartition(partition.getId()) > 0;
        }

        return deletionStatus;
    }

    private boolean isValidPartition(JsonObject jsonObj) {

        boolean isValid =
                jsonObj != null &&
                jsonObj.has("title") &&
                jsonObj.has("creator") &&
                jsonObj.has("author") &&
                jsonObj.has("strophes") &&
                jsonObj.get("strophes").isJsonArray();
        if(!isValid) {
            return isValid;
        }

        JsonArray strophes = jsonObj.get("strophes").getAsJsonArray();
        for(JsonElement strophe : strophes) {
            isValid = isValid && isValidStrophe((JsonObject)strophe);
        }

        return isValid;
    }

    private boolean isValidStrophe(JsonObject jsonObj) {
        boolean isValid =
                jsonObj != null &&
                //jsonObj.has("position") &&
                jsonObj.has("lignes") &&
                jsonObj.get("lignes").isJsonArray();
        JsonArray lignes = jsonObj.get("lignes").getAsJsonArray();
        for(JsonElement ligne : lignes) {
            isValid = isValid && isValidLigne((JsonObject) ligne);
        }

        return isValid;
    }

    private boolean isValidLigne(JsonObject jsonObj) {
        return  jsonObj != null &&
                jsonObj.has("accord") &&
                jsonObj.has("text");// &&
                //jsonObj.has("position");
    }
}
