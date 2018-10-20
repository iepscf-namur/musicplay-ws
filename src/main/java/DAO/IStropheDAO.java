package DAO;

import DAO.BEANS.Partition;
import DAO.BEANS.Strophe;

import java.util.List;

public interface IStropheDAO {

    public int addStrophe(Strophe strophe, Partition partition);
    public int removeStrophe(int id);
    public int removeStrophes(Partition partition);
    public int updateStrophe(Strophe strophe);
    public Strophe getStrophe(int id);
    public Strophe getStropheAt(int position, Partition partition);
    public List<Strophe> getStrophes(Partition partition);
}
