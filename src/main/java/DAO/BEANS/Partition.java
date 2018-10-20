package DAO.BEANS;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class Partition {
    private int id;
    private String title;
    private String urlImage;
    private boolean userValidation;
    private boolean moderatorValidation;
    private User creator;
    private Author author;
    private List<Strophe> strophes;
    private Date creationDate;
    private Date modificationDate;

    public Partition() {
        strophes = new ArrayList<>();
    }

    /*
    public Partition(int id, String title, String urlImage, boolean userValidation,
                     boolean moderatorValidation, User creator, Author author,
                     Date creationDate, Date modificationDate) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
        this.userValidation = userValidation;
        this.moderatorValidation = moderatorValidation;
        this.creator = creator;
        this.author = author;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean isUserValidation() {
        return userValidation;
    }

    public void setUserValidation(boolean userValidation) {
        this.userValidation = userValidation;
    }

    public boolean isModeratorValidation() {
        return moderatorValidation;
    }

    public void setModeratorValidation(boolean moderatorValidation) {
        this.moderatorValidation = moderatorValidation;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public List<Strophe> getStrophes() {
        return strophes;
    }

    public void setStrophes(List<Strophe> strophes) {
        this.strophes = strophes;
        /*
        System.out.println("setStrophes : " + (strophes.isEmpty() ? "EMPTY" : "NOT EMPTY"));
        for(Strophe strophe : strophes) {
            this.strophes.add(strophe);
        }
        */

    }
}
