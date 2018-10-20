package DAO;

import DAO.BEANS.Role;
import DAO.BEANS.User;

import java.util.List;

public interface IUserRoleDAO {

    public int addUserRole(User user, Role role);
    public int removeUserRole(User user, Role role);
    public List<Role> getUserRoles(User user);
    public List<User> getRoleUsers(Role role);
}
