package DAO;

import DAO.BEANS.*;


public class DAOFactory {
<<<<<<< HEAD
    private String url;
    private String username;
    private String password;

    DAOFactory(String url, String username, String password){
        this.url = url + "?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
        this.username = username;
        this.password = password;
=======

    public static DAO<User> getUserDao(){
        return new UserDAO();
>>>>>>> Musicplay-ws.DAO
    }



<<<<<<< HEAD
        DAOFactory instance = new DAOFactory("jdbc:mysql://localhost:3306/musicplaydb", "root", "1234");
        return instance;
    }
=======
>>>>>>> Musicplay-ws.DAO


}