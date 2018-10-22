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
    private static final String FIND_ALL = "SELECT * FROM user ORDER BY id";
    private static final String AUTH_STOREDPROC= "CALL user_auth(?, ?, ?)";
    private static final String INSERT_STOREDPROC = "CALL user_create(?, ?, ?)";
    private static final String UPDATE = "UPDATE user SET login=?, password=?, salt=? WHERE id=?";


    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }


    @Override
    public int AddUser(User user){
        int lastInsertID = 0 ;
        try{
            CallableStatement callableStatement = connexion.prepareCall(INSERT_STOREDPROC);
            callableStatement.setString(1, user.getLogin());
            callableStatement.setString(2, user.getPassword());
            callableStatement.registerOutParameter(3, Types.INTEGER);
            callableStatement.execute();
            lastInsertID = callableStatement.getInt(3);
            callableStatement.close();
        }catch(SQLException e)
        {
            System.out.println("SQLException");
            e.printStackTrace();
        }

        return lastInsertID;
    }

    @Override
    public boolean UpdateUser(User user) {
        boolean response = false ;
        try {
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getSalt());
            preparedStatement.setInt(4, user.getId());

            if(preparedStatement.executeUpdate() > 0) response = true ;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response ;
    }

    @Override
    public boolean DeleteUser(int id) {
        boolean response = false ;
        try {
            PreparedStatement preparedStatemnt = connexion.prepareStatement(DELETE);

            preparedStatemnt.setInt(1, id);
            if(preparedStatemnt.executeUpdate() > 0) response = true ;
            preparedStatemnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<User> GetUsers() {
        List<User> users = new LinkedList<User>();
        try {
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

    @Override
    public User AuthUser(String login, String password){
        User user = new User();
        try{
            CallableStatement callableStatement = connexion.prepareCall(AUTH_STOREDPROC);
            callableStatement.setString(1, login);
            callableStatement.setString(2, password);
            callableStatement.registerOutParameter(3,Types.INTEGER);
            callableStatement.execute();
            int returnValue = callableStatement.getInt(3);
            if(returnValue > 0){
                user = GetUser(returnValue);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }

    // Helpers
    // Used in stored_proc to return user after login
    private User GetUser(int id){
        User user = null;
        try{
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
}
