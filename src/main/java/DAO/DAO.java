package DAO;


import java.sql.Connection;
import java.util.List;
import DAO.BEANS.*;

public abstract class DAO<T> {


    public Connection connect = ConnectionSQL.getInstance();

    public abstract T select(int id);
    public abstract List<T> selectAll();
    public abstract int insert(T obj); // cles generée renvoyée id insérée
    public abstract int update(T obj); // nb Ligne impactée renvoyée
    public abstract int delete(T obj); // nb Ligne impactée renvoyée


}
