package DAO.BEANS;

import java.util.ArrayList;
import java.util.List;

public class Strophe {
    private int id;
    //private int fkIdPartition;
    private int position;
    private List<Ligne> lignes;

    public Strophe() {
        this.lignes = new ArrayList<>();
    }

    /*
    public Strophe(int id, int fkIdPartition, int position) {
        this.id = id;
        this.fkIdPartition = fkIdPartition;
        this.position = position;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public int getFkIdPartition() {
        return fkIdPartition;
    }

    public void setFkIdPartition(int fkIdPartition) {
        this.fkIdPartition = fkIdPartition;
    }
    */


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Ligne> getLignes() {
        return lignes;
    }

    public void setLignes(List<Ligne> lignes) {
        this.lignes = lignes;
    }
}
