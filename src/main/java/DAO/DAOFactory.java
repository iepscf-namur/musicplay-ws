package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private String url;
    private String username;
    private String password;

    DAOFactory(String url, String username, String password){
        this.url = url + "?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
        this.username = username;
        this.password = password;
    }

    public static DAOFactory getInstance(){
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){

        }

        DAOFactory instance = new DAOFactory("jdbc:mysql://localhost:3306/musicplaydb", "root", "");
        return instance;
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,password);
    }

    public UserDAO getUserDAO(){
        return new UserDAOImpl(this);
    }
}
