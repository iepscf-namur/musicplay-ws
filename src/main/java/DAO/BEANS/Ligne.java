package DAO.BEANS;

public class Ligne {
    private int id;
    private String accord;
    private String text;
    private int position;
    //private int fkIdStrophe;

    public Ligne() {}

    /*
    public Ligne(int id, String accord, String text, int fkIdStrophe) {
        this.id = id;
        this.accord = accord;
        this.text = text;
        this.fkIdStrophe = fkIdStrophe;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccord() {
        return accord;
    }

    public void setAccord(String accord) {
        this.accord = accord;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /*
    public int getFkIdStrophe() {
        return fkIdStrophe;
    }

    public void setFkIdStrophe(int fkIdStrophe) {
        this.fkIdStrophe = fkIdStrophe;
    }
    */


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
