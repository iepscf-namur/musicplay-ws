package DAO;

import DAO.BEANS.User;

import java.util.List;

public interface IUserDAO {
    int AddUser(User user);
    boolean UpdateUser(User user);
    boolean DeleteUser(int id);
    User AuthUser(String login, String password);
    List<User> GetUsers();
    User GetUser(int id);
    User GetUser(String login);
}