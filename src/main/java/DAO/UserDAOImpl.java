package DAO;

import DAO.BEANS.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    private DAOFactory daoFactory;

    UserDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public void AddUser(User user){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try{
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("INSERT INTO User(Login, Password, Salt) VALUES (?,?,?);");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(1, user.getSalt());

            preparedStatement.executeUpdate();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
