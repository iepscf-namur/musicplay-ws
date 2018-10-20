package DAO;

import DAO.BEANS.Ligne;
import DAO.BEANS.Strophe;

import java.util.List;

public interface ILigneDAO {

    public int addLigne(Ligne ligne, Strophe strophe);
    public int removeLigne(int id);
    public int removeLignes(Strophe strophe);
    public int updateLigne(Ligne ligne);
    public Ligne getLigne(int id);
    public Ligne getLigneAt(int position, Strophe strophe);
    public List<Ligne> getLignes(Strophe strophe);
}
