package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQL {

    private static String url = "jdbc:mysql://localhost:3306/musicplaydbtest"+ "?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
    private static String username = "root";
    private static String password = "1234";
    private static Connection connect;

    public static Connection getInstance(){
        if (connect == null){
            try{
                connect = DriverManager.getConnection(url,username,password);

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }

}
