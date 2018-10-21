package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private static DAOFactory _instance = null;
    private String url;
    private String username;
    private String password;

    private DAOFactory(String url, String username, String password){
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
        if(DAOFactory._instance == null) {
            DAOFactory._instance = new DAOFactory("jdbc:mysql://localhost:3306/musicplaydb", "root", "");
        }
        return DAOFactory._instance;
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,password);
    }

    public IUserDAO getUserDAO(){
        return new UserDAOImpl(this);
    }
    public IRoleDAO getRoleDAO(){ return new RoleDAOImpl(this); }
    public IUserRoleDAO getUserRoleDAO(){
        return new UserRoleDAOImpl(this);
    }
    public IAuthorDAO getAuthorDAO(){ return new AuthorDAOImpl(this); }
    public IPartitionDAO getPartitionDAO(){ return new PartitionDAOImpl(this); }
    public ILigneDAO getLigneDAO(){ return new LigneDAOImpl(this); }
    public IStropheDAO getStropheDAO(){ return new StropheDAOImpl(this); }
}
