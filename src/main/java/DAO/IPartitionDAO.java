package DAO;

import DAO.BEANS.*;

import java.util.List;
import java.util.Map;

public interface IPartitionDAO {
    void AddPartition(Partition partition, User creator, Author author, Map<Strophe, List<Ligne>> strophesLignes);
    void UpdatePartition(Partition partition);
    void DeletePartition(int id);
    Partition GetPartition(int id);
    List<Partition> GetPartitions();
}
