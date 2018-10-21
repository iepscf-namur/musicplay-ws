package DAO;

import DAO.BEANS.*;


public class DAOFactory {

    public static DAO<User> getUserDao(){
        return new UserDAO();
    }
}