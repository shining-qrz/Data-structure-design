package dao;

import java.util.List;

public interface FileDao<T> {
    void save(List<T> items);
    List<T> load();
    void append(T item);
    boolean update(T item);
    boolean delete(String id);
}
