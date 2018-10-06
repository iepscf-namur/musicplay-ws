package beans;

import java.util.Date;

public class Partition {
    private int id;
    private String title;
    private String urlImage;
    private int userValidation;
    private int moderatorValidation;
    private int creator;
    private int author;
    private Date creationDate;
    private Date modificationDate;

    public Partition(int id, String title, String urlImage, int userValidation, int moderatorValidation, int creator, int author, Date creationDate, Date modificationDate) {
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

    public int getUserValidation() {
        return userValidation;
    }

    public void setUserValidation(int userValidation) {
        this.userValidation = userValidation;
    }

    public int getModeratorValidation() {
        return moderatorValidation;
    }

    public void setModeratorValidation(int moderatorValidation) {
        this.moderatorValidation = moderatorValidation;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
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
}
