package DAO;

import DAO.BEANS.Partition;
import DAO.BEANS.Strophe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StropheDAOImpl implements IStropheDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM strophe WHERE id=?";
    private static final String DELETE_ALL = "DELETE FROM strophe WHERE fkIdPartition=?";
    private static final String FIND_BY_ID = "SELECT * FROM strophe WHERE id=?";
    private static final String FIND_BY_POSITION = "SELECT * FROM strophe WHERE fkIdPartition=? AND position=?";
    private static final String FIND_ALL = "SELECT * FROM strophe WHERE fkIdPartition=? ORDER BY position ASC";
    private static final String INSERT =
            "INSERT INTO strophe (id, position, fkIdPartition) VALUES (NULL, ?, ?)";
    private static final String UPDATE = "UPDATE strophe SET position=? WHERE id=?";


    StropheDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int addStrophe(Strophe strophe, Partition partition) {
        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(true);
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, strophe.getPosition());
            preparedStatement.setInt(2, partition.getId());

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
    public int removeStrophe(int id) {
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
    public int removeStrophes(Partition partition) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE_ALL);

            preparedStatement.setInt(1, partition.getId());
            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int updateStrophe(Strophe strophe) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setInt(1, strophe.getPosition());
            preparedStatement.setInt(2, strophe.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public Strophe getStrophe(int id) {
        Strophe strophe = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                strophe = new Strophe();
                strophe.setId(resultSet.getInt("id"));
                strophe.setPosition(resultSet.getInt("position"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return strophe ;
    }

    @Override
    public Strophe getStropheAt(int position, Partition partition) {
        Strophe strophe = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_POSITION);
            preparedStatement.setInt(1, partition.getId());
            preparedStatement.setInt(2, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                strophe = new Strophe();
                strophe.setId(resultSet.getInt("id"));
                strophe.setPosition(resultSet.getInt("position"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return strophe ;
    }

    @Override
    public List<Strophe> getStrophes(Partition partition) {
        List<Strophe> strophes = new ArrayList<>();
        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_ALL);
            preparedStatement.setInt(1, partition.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Strophe strophe = new Strophe();
                strophe.setId(resultSet.getInt("id"));
                strophe.setPosition(resultSet.getInt("position"));
                strophes.add(strophe);
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strophes;
    }
}
