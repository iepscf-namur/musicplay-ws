package DAO;

import DAO.BEANS.Author;

import java.util.List;

public interface IAuthorDAO {

    public int addAuthor(Author author);
    public Author getAuthor(int id);
    public Author getAuthor(String name);
    public List<Author> getAuthors();
    public int updateAuthor(Author author);
    public int removeAuthor(int id);
}
