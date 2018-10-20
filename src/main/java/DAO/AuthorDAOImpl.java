package DAO;

import DAO.BEANS.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAOImpl implements IAuthorDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM author WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM author WHERE id=?";
    private static final String FIND_BY_NAME = "SELECT * FROM author WHERE name=?";
    private static final String FIND_ALL = "SELECT * FROM author ORDER BY id";
    private static final String INSERT = "INSERT INTO author (id, name) VALUES (NULL, ?)";
    private static final String UPDATE = "UPDATE author SET name=? WHERE id=?";


    AuthorDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int addAuthor(Author author) {
        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getName());

            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if(generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
            }
            generatedKey.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return lastInsertID;
    }

    @Override
    public Author getAuthor(int id) {
        Author author = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return author ;
    }

    @Override
    public Author getAuthor(String name) {
        Author author = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return author ;
    }

    @Override
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                authors.add(author);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public int updateAuthor(Author author) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int removeAuthor(int id) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);
            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }
}
