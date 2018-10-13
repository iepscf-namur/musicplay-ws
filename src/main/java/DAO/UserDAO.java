package DAO;

import DAO.BEANS.User;

import java.util.List;

public interface UserDAO {
    void AddUser(User user);
    User GetUser(int id);
    List<User> GetUsers();
}
