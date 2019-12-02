package dao;

import java.util.Set;

public interface Dao<T> {
  T getById(int id);
  Set<T> getAll();
  Boolean insert(T obj);
  Boolean update(T obj);
  Boolean delete(T obj);
}
