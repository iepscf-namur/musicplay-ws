package DAO;

import DAO.BEANS.*;

import java.util.List;
import java.util.Map;

public interface IPartitionDAO {
    public int AddPartition(Partition partition);
    public int UpdatePartition(Partition partition);
    public int DeletePartition(int id);
    public Partition GetPartition(int id);
    public List<Partition> GetPartitions();
    public List<Partition> GetPartitions(Author author);
    public List<Partition> GetPartitions(User creator);
    public List<Partition> GetPartitions(boolean moderatorStatus);
}
