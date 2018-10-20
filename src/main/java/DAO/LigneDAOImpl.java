package DAO;

import DAO.BEANS.Ligne;
import DAO.BEANS.Strophe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneDAOImpl implements ILigneDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM ligne WHERE id=?";
    private static final String DELETE_ALL = "DELETE FROM ligne WHERE fkIdStrophe=?";
    private static final String FIND_BY_ID = "SELECT * FROM ligne WHERE id=?";
    private static final String FIND_BY_POSITION = "SELECT * FROM ligne WHERE fkIdStrophe=? AND position=?";
    private static final String FIND_ALL = "SELECT * FROM ligne WHERE fkIdStrophe=? ORDER BY position ASC";
    private static final String INSERT =
            "INSERT INTO ligne (id, accord, text, position, fkIdStrophe) VALUES (NULL, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE ligne SET accord=?, text=?, position=? WHERE id=?";


    LigneDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int addLigne(Ligne ligne, Strophe strophe) {
        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(true);
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ligne.getAccord());
            preparedStatement.setString(2, ligne.getText());
            preparedStatement.setInt(3, ligne.getPosition());
            preparedStatement.setInt(4, strophe.getId());

            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if(generatedKey.next()) {
                lastInsertID = generatedKey.getInt(1);
            }
            preparedStatement.close();
            generatedKey.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return lastInsertID;
    }

    @Override
    public int removeLigne(int id) {
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
    public int removeLignes(Strophe strophe) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(DELETE_ALL);

            preparedStatement.setInt(1, strophe.getId());
            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int updateLigne(Ligne ligne) {
        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, ligne.getAccord());
            preparedStatement.setString(2, ligne.getText());
            preparedStatement.setInt(3, ligne.getPosition());
            preparedStatement.setInt(4, ligne.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public Ligne getLigne(int id) {
        Ligne ligne = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                ligne = new Ligne();
                ligne.setId(resultSet.getInt("id"));
                ligne.setAccord(resultSet.getString("accord"));
                ligne.setText(resultSet.getString("text"));
                ligne.setPosition(resultSet.getInt("position"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return ligne ;
    }

    @Override
    public Ligne getLigneAt(int position, Strophe strophe) {
        Ligne ligne = null;
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_POSITION);
            preparedStatement.setInt(1, strophe.getId());
            preparedStatement.setInt(2, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                ligne = new Ligne();
                ligne.setId(resultSet.getInt("id"));
                ligne.setAccord(resultSet.getString("accord"));
                ligne.setText(resultSet.getString("text"));
                ligne.setPosition(resultSet.getInt("position"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return ligne ;
    }

    @Override
    public List<Ligne> getLignes(Strophe strophe) {
        List<Ligne> lignes = new ArrayList<>();
        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_ALL);
            preparedStatement.setInt(1, strophe.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Ligne ligne = new Ligne();
                ligne.setId(resultSet.getInt("id"));
                ligne.setAccord(resultSet.getString("accord"));
                ligne.setText(resultSet.getString("text"));
                ligne.setPosition(resultSet.getInt("position"));
                lignes.add(ligne);
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }
}
