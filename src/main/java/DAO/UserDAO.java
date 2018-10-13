package DAO;

import DAO.BEANS.User;

import java.util.List;

public interface UserDAO {
    void AddUser(User user);
    void UpdateUser(User user);
    void DeleteUser(int id);
    User GetUser(int id);
    List<User> GetUsers();
}
