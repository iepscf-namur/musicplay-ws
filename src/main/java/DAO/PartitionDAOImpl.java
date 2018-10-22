package DAO;

import DAO.BEANS.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartitionDAOImpl implements IPartitionDAO {

    private DAOFactory daoFactory;
    private Connection connexion;

    private static final String DELETE = "DELETE FROM partitonmusicplay WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM partitonmusicplay WHERE id=?";
    private static final String FIND_BY_CREATOR = "SELECT * FROM partitonmusicplay WHERE creatorFkIdUser=?";
    private static final String FIND_BY_AUTHOR = "SELECT * FROM partitonmusicplay WHERE authorFkIdAuthor=?";
    private static final String FIND_ALL_MODERATOR_STATUS =
            "SELECT * FROM partitonmusicplay WHERE moderatorValidation=? ORDER BY id";
    private static final String FIND_ALL = "SELECT * FROM partitonmusicplay ORDER BY id";
    private static final String INSERT =
            "INSERT INTO partitonmusicplay (id, title, urlImage, userValidation, moderatorValidation, " +
                    "creatorFkIdUser, authorFkIdAuthor, creationDate, modificationDate) " +
            "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE partitonmusicplay SET title=?, urlImage=?, userValidation=?, " +
                "moderatorValidation=?, modificationDate=? " +
            "WHERE id=?";


    PartitionDAOImpl(DAOFactory daoFactory){
        this.daoFactory = daoFactory;
    }

    @Override
    public int AddPartition(Partition partition) {
        PreparedStatement preparedStatement = null;
        int lastInsertID = 0;

        try{
            connexion = daoFactory.getConnection();
            //connexion.setAutoCommit(true);
            preparedStatement = connexion.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, partition.getTitle());
            preparedStatement.setString(2, partition.getUrlImage());
            preparedStatement.setBoolean(3, partition.isUserValidation());
            preparedStatement.setBoolean(4, partition.isModeratorValidation());
            preparedStatement.setInt(5, partition.getCreator().getId());
            preparedStatement.setInt(6, partition.getAuthor().getId());
            preparedStatement.setDate(7, partition.getCreationDate());
            preparedStatement.setDate(8, partition.getModificationDate());

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
    public int UpdatePartition(Partition partition) {

        int nbRowsAffected = 0;

        try {
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE);
            preparedStatement.setString(1, partition.getTitle());
            preparedStatement.setString(2, partition.getUrlImage());
            preparedStatement.setBoolean(3, partition.isUserValidation());
            preparedStatement.setBoolean(4, partition.isModeratorValidation());
            preparedStatement.setDate(5, partition.getModificationDate());
            preparedStatement.setInt(6, partition.getId());

            nbRowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbRowsAffected;
    }

    @Override
    public int DeletePartition(int id) {
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
    public Partition GetPartition(int id) {

        Partition partition = null;
        IUserDAO userDAO = daoFactory.getUserDAO();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                partition = new Partition();
                partition.setId(resultSet.getInt("id"));
                partition.setTitle(resultSet.getString("title"));
                partition.setUrlImage(resultSet.getString("urlImage"));
                User creator = userDAO.GetUser(resultSet.getInt("creatorFkIdUser"));
                partition.setCreator(creator);
                Author author = authorDAO.getAuthor(resultSet.getInt("authorFkIdAuthor"));
                partition.setAuthor(author);
                partition.setUserValidation(resultSet.getBoolean("userValidation"));
                partition.setModeratorValidation(resultSet.getBoolean("moderatorValidation"));

                IStropheDAO stropheDAO = daoFactory.getStropheDAO();
                partition.setStrophes(stropheDAO.getStrophes(partition));

                ILigneDAO ligneDAO = daoFactory.getLigneDAO();
                for(Strophe strophe : partition.getStrophes()) {
                    strophe.setLignes(ligneDAO.getLignes(strophe));
                }

                partition.setCreationDate(resultSet.getDate("creationDate"));
                partition.setModificationDate(resultSet.getDate("modificationDate"));
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return partition ;
    }

    @Override
    public List<Partition> GetPartitions() {
        List<Partition> partitions = new ArrayList<>();
        IUserDAO userDAO = daoFactory.getUserDAO();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();
        try {
            connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while(resultSet.next()){
                Partition partition = new Partition();
                partition.setId(resultSet.getInt("id"));
                partition.setTitle(resultSet.getString("title"));
                partition.setUrlImage(resultSet.getString("urlImage"));
                User creator = userDAO.GetUser(resultSet.getInt("creatorFkIdUser"));
                partition.setCreator(creator);
                Author author = authorDAO.getAuthor(resultSet.getInt("authorFkIdAuthor"));
                partition.setAuthor(author);
                partition.setUserValidation(resultSet.getBoolean("userValidation"));
                partition.setModeratorValidation(resultSet.getBoolean("moderatorValidation"));

                IStropheDAO stropheDAO = daoFactory.getStropheDAO();
                partition.setStrophes(stropheDAO.getStrophes(partition));

                ILigneDAO ligneDAO = daoFactory.getLigneDAO();
                for(Strophe strophe : partition.getStrophes()) {
                    strophe.setLignes(ligneDAO.getLignes(strophe));
                }

                partition.setCreationDate(resultSet.getDate("creationDate"));
                partition.setModificationDate(resultSet.getDate("modificationDate"));
                partitions.add(partition);
            }
            resultSet.close();
            statement.close();
            connexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partitions;
    }

    @Override
    public List<Partition> GetPartitions(Author author) {
        List<Partition> partitions = new ArrayList<>();
        IUserDAO userDAO = daoFactory.getUserDAO();
        IAuthorDAO authorDAO = daoFactory.getAuthorDAO();

        try{
            connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(FIND_BY_AUTHOR);
            preparedStatement.setInt(1, author.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Partition partition = new Partition();
                partition.setId(resultSet.getInt("id"));
                partition.setTitle(resultSet.getString("title"));
                partition.setUrlImage(resultSet.getString("urlImage"));
                User creator = userDAO.GetUser(resultSet.getInt("creatorFkIdUser"));
                partition.setCreator(creator);
                author = authorDAO.getAuthor(resultSet.getInt("authorFkIdAuthor"));
                partition.setAuthor(author);
                partition.setUserValidation(resultSet.getBoolean("userValidation"));
                partition.setModeratorValidation(resultSet.getBoolean("moderatorValidation"));

                IStropheDAO stropheDAO = daoFactory.getStropheDAO();
                partition.setStrophes(stropheDAO.getStrophes(partition));

                ILigneDAO ligneDAO = daoFactory.getLigneDAO();
                for(Strophe strophe : partition.getStrophes()) {
                    strophe.setLignes(ligneDAO.getLignes(strophe));
                }

                partition.setCreationDate(resultSet.getDate("creationDate"));
                partition.setModificationDate(resultSet.getDate("modificationDate"));
                partitions.add(partition);
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return partitions;
    }

    @Override
    public List<Partition> GetPartitions(User creator) {
        return null;
    }

    @Override
    public List<Partition> GetPartitions(boolean moderatorStatus) {
        return null;
    }
}
