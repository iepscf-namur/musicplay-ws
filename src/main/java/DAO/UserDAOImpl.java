package DAO;

import DAO.BEANS.User;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM user WHERE IdUser=?";
    private static final String FIND_BY_ID = "SELECT * FROM user WHERE IdUser=?";
    private static final String FIND_ALL = "SELECT * FROM user ORDER BY IdUser";
    private static final String INSERT = "INSERT INTO user (IdUser, UserLogin, lastName, UserPassword, UserSalt) VALUES (NULL, ?, ?, ?)";
    private static final String UPDATE = "UPDATE user SET UserLogin=?, UserPassword=?, UserSalt=? WHERE IdUser=?";


    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }


    @Override
    public void AddUser(User user){
        PreparedStatement preparedStatement = null;

        try{
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(INSERT);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(1, user.getSalt());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateUser(User user) {
        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getSalt());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteUser(int id) {
        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatemnt = connexion.prepareStatement(DELETE);

            preparedStatemnt.setInt(1, id);
            preparedStatemnt.executeUpdate();
            preparedStatemnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User GetUser(int id){
        User user = new User();
        try{
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_BY_ID);

            user.setId(Integer.parseInt(resultSet.getString("IdUser")));
            user.setLogin(resultSet.getString("UserLogin"));
            user.setPassword(resultSet.getString("UserPassword"));
            user.setSalt(resultSet.getString("UserSalt"));
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }

    @Override
    public List<User> GetUsers() {
        List<User> users = new LinkedList<User>();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                User user = new User();
                user.setId(Integer.parseInt(resultSet.getString("IdUser")));
                user.setLogin(resultSet.getString("UserLogin"));
                user.setPassword(resultSet.getString("UserPassword"));
                user.setSalt(resultSet.getString("UserSalt"));

                users.add(user);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
