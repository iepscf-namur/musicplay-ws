package DAO;

import DAO.BEANS.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM user WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM user WHERE id=?";
    private static final String FIND_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    private static final String FIND_ALL = "SELECT * FROM user ORDER BY id";
    //private static final String INSERT = "INSERT INTO user (IdUser, UserLogin, lastName, UserPassword, UserSalt) VALUES (NULL, ?, ?, ?)";
    private static final String INSERT = "INSERT INTO user (id, login, password, salt) VALUES (NULL, ?, ?, ?)";
    private static final String UPDATE = "UPDATE user SET login=?, password=?, salt=? WHERE id=?";


    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }


    @Override
    public int AddUser(User user){
        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(true);
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getSalt());

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
    public int UpdateUser(User user) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getSalt());
            preparedStatement.setInt(4, user.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int DeleteUser(int id) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatemnt = connexion.prepareStatement(DELETE);

            preparedStatemnt.setInt(1, id);
            nbRowsAffected = preparedStatemnt.executeUpdate();
            preparedStatemnt.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public User GetUser(int id){
        User user = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setString(1, Integer.toString(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.setId(Integer.parseInt(resultSet.getString("id")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }

    @Override
    public User GetUser(String login) {
        User user = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.setId(Integer.parseInt(resultSet.getString("id")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }

    @Override
    public List<User> GetUsers() {
        List<User> users = new ArrayList<>();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                User user = new User();
                user.setId(Integer.parseInt(resultSet.getString("id")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));

                users.add(user);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
