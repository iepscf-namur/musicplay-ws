package DAO;

import DAO.BEANS.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements IRoleDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM role WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM role WHERE id=?";
    private static final String FIND_BY_NAME = "SELECT * FROM role WHERE name=?";
    private static final String FIND_ALL = "SELECT * FROM role ORDER BY id";
    private static final String INSERT = "INSERT INTO role (id, name) VALUES (NULL, ?)";
    private static final String UPDATE = "UPDATE role SET name=? WHERE id=?";


    RoleDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int addRole(Role role) {

        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(true);
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, role.getName());

            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if(generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
            }
            generatedKey.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return lastInsertID;
    }

    @Override
    public int removeRole(int id) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE);

            preparedStatement.setInt(1, id);
            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int updateRole(Role role) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, role.getName());
            preparedStatement.setInt(2, role.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public Role getRole(int id) {
        Role role = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return role ;
    }

    @Override
    public Role getRole(String name) {
        Role role = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return role ;
    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
                roles.add(role);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
