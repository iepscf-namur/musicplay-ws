package DAO;

import DAO.BEANS.User;

import java.util.List;

public interface UserDAO {
    int AddUser(User user);
    int UpdateUser(User user);
    int DeleteUser(int id);
    User GetUser(int id);
    User GetUser(String login);
    List<User> GetUsers();
}
