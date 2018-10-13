package DAO;

import DAO.BEANS.User;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DAOFactory daoFactory;
    private Connection connexion;

    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }


    @Override
    public void AddUser(User user){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try{
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("INSERT INTO user(UserId, UserLogin, UserPassword, UserSalt) VALUES (NULL,?,?,?);");
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
    public User GetUser(int id){
        PreparedStatement preparedStatemnt = null;
        User user = new User();

        try{
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE IdUser = ?");

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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

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
