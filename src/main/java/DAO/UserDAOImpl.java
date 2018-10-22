package DAO;



import DAO.BEANS.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {
    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM user WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM user WHERE id=?";
    private static final String FIND_BY_LOGIN = "SELECT * FROM user WHERE login=?";
    private static final String FIND_ALL = "SELECT * FROM user ORDER BY id";
    private static final String AUTH_STOREDPROC= "CALL user_auth(?, ?, ?)";
    private static final String INSERT_STOREDPROC = "CALL user_create(?, ?, ?)";
    private static final String UPDATE = "UPDATE user SET login=?, password=?, salt=? WHERE id=?";


    public UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }


    @Override
    public int AddUser(User user){
        int lastInsertID = 0 ;
        try{
            connexion = daoFactory.getConnection();
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
            connexion = daoFactory.getConnection();
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
            connexion = daoFactory.getConnection();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User AuthUser(String login, String password){
        User user = null;
        try{
            connexion = daoFactory.getConnection();
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

    public Connection getConnection() {

        String dbURL = "jdbc:mysql://localhost/musicplay";
        String dbUser = "root";
        String dbPassword = "";

        try {
            if (connexion == null) {
                Class.forName("com.mysql.jdbc.Driver");
                connexion = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }

        return connexion;

    }

    public void closeConnection() {
        if (connexion != null) {
            try {
                connexion.close();
            } catch (SQLException e) {
                //e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public User GetUser(int id){
        User user = null;
        try{
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_ID);
            ((PreparedStatement) statement).setInt(1, id);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setId(Integer.parseInt(resultSet.getString("id")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }

    @Override
    public User GetUser(String login){
        User user = null;
        try{
            connexion = daoFactory.getConnection();
            Statement statement = connexion.prepareStatement(FIND_BY_LOGIN);
            ((PreparedStatement) statement).setString(1, login);
            ResultSet resultSet = ((PreparedStatement) statement).executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setId(Integer.parseInt(resultSet.getString("id")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getString("salt"));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user ;
    }
}

