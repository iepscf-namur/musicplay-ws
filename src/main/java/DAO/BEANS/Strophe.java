package DAO.BEANS;

public class Strophe {
    private int id;
    private int fkIdPartition;
    private int order;

    public Strophe(int id, int fkIdPartition, int order) {
        this.id = id;
        this.fkIdPartition = fkIdPartition;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFkIdPartition() {
        return fkIdPartition;
    }

    public void setFkIdPartition(int fkIdPartition) {
        this.fkIdPartition = fkIdPartition;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
