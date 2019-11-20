package Daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface Dao<T> {
    T getById(int id);

    Set<T> getAll();

    Boolean insert(T obj);

    Boolean update(T obj);

    Boolean delete(T obj);

    Set<T> unpackResultSet(ResultSet rs) throws SQLException;
}
