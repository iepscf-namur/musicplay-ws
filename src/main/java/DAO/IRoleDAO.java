package DAO;

import DAO.BEANS.Role;

import java.util.List;

public interface IRoleDAO {

    public int addRole(Role role);
    public int removeRole(int id);
    public int updateRole(Role role);
    public Role getRole(int id);
    public Role getRole(String name);
    public List<Role> getRoles();
}
