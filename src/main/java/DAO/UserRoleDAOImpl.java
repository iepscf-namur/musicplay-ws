package DAO;

import DAO.BEANS.Role;
import DAO.BEANS.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDAOImpl implements IUserRoleDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String INSERT = "INSERT INTO `user-role` (fkIdUser, fkIdRole) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM `user-role` WHERE fkIdUser=? AND fkIdRole=?";
    private static final String FIND_ROLES_BY_USER_ID =
            "SELECT id, name FROM `user-role` " +
                    "INNER JOIN role ON id = fkIdRole " +
            "WHERE fkIdUser=?";
    private static final String FIND_USERS_BY_ROLE_ID = "SELECT fkIdUser FROM `user-role` WHERE fkIdRole=?";

    UserRoleDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int addUserRole(User user, Role role) {

        if(user == null || role == null) {
            return 0;
        }

        UserDAO userDAO = daoFactory.getUserDAO();
        IRoleDAO roleDAO = daoFactory.getRoleDAO();

        // if user or role does not exist
        if(userDAO.GetUser(user.getId()) == null || roleDAO.getRole(role.getId()) == null) {
            return 0;
        }

        // if user has already been assigned that role
        List<Role> roles = this.getUserRoles(user);
        for(Role userRole : roles) {
            if(userRole.getId() == role.getId()) {
                return 0;
            }
        }

        PreparedStatement preparedStatement = null;
        int insertStatus = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(INSERT);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, role.getId());

            insertStatus = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return insertStatus;
    }

    @Override
    public int removeUserRole(User user, Role role) {
        PreparedStatement preparedStatement = null;
        int deleteStatus = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(DELETE);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, role.getId());

            deleteStatus = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return deleteStatus;
    }

    @Override
    public List<Role> getUserRoles(User user) {
        List<Role> roles = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(FIND_ROLES_BY_USER_ID);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
                roles.add(role);
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public List<User> getRoleUsers(Role role) {
        List<User> users = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(FIND_USERS_BY_ROLE_ID);
            preparedStatement.setInt(1, role.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                UserDAO userDAO = daoFactory.getUserDAO();
                User user = userDAO.GetUser(resultSet.getInt(1));
                users.add(user);
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
