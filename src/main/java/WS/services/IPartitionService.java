package WS.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public interface IPartitionService {
    /*
    // GET /partitions/{id}
    public JsonObject getPartitionJson(int id);

    // GET /partitions calls /partitions?start=0&size=50 and returns JSON array
    public JsonArray getPartitionsJson();

    // GET /partitions?start={int}&size={int}
    //public JsonArray getPartitionsJson(Map<String, Object> params);

    // GET /partitions/author/{id} calls /partitions/author/{id}?start=0&size=50
    //public JsonArray getPartitionsAuthor(int authorId);

    // GET /partitions/author/{id}?start={int}&size={int}
    //public JsonArray getPartitionsAuthor(int authorId, Map<String, Object> params);
    */

    public JsonObject getPartitionJson(int id);
    public JsonArray getPartitionsJson();
    public JsonArray getPartitionsByAuthorJson(JsonObject author);
    public JsonArray getPartitionsByCreatorJson(JsonObject creator);
    public JsonObject updatePartitionJson(JsonObject partition);
    public JsonObject deletePartitionJson(int id);
    public JsonObject addPartitionJson(JsonObject partition, String baseUrl);

}
