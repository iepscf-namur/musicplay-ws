package WS.services;

import DAO.DAOFactory;
import DAO.UserDAO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.util.Map;

public class PartitionServiceImpl implements IPartitionService {

    private final int MAX_REQUEST_SZ = 50;

    /**
     * GET /partitions?start=0&size=MAX_REQUEST_SZ, returns JSON array
     * QueryParams accepted in params
     *      start, int
     *      size, int
     *      order, map<field, 'ASC' or 'DESC'>
     *
     * @return a Json array string
     * @throws Exception
     */
    public String getPartitionsJson() throws Exception {


        return null;
    }

    /**
     *
     * @return an XML string
     * @throws Exception
     */
    public String getPartitionsXml() throws Exception {
        throw new NotImplementedException();
    }

    /**
     *
     * @param params
     * @return a Json array string
     * @throws Exception
     */
    public String getPartitionsJson(Map<String, String> params) throws Exception {
        return null;
    }

    /**
     * GET /partitions?start={int}&size={int}
     * @param params
     * @return an XML string
     * @throws Exception
     */
    public String getPartitionsXml(Map<String, String> params) throws Exception {
        throw new NotImplementedException();
    }

}
