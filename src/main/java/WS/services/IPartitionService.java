package WS.services;

import java.util.Map;

public interface IPartitionService {

    // GET /partitions?start=0&size=50, returns JSON array
    public String getPartitionsJson() throws Exception;
    public String getPartitionsXml() throws Exception;
    // GET /partitions?start={int}&size={int}
    public String getPartitionsJson(Map<String, String> params) throws Exception;
    public String getPartitionsXml(Map<String, String> params) throws Exception;
}
