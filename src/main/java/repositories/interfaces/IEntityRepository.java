package repositories.interfaces;

import java.util.List;

public interface IEntityRepository<T> {
    void add(T entity);

    void update(T entity);

    void remove(T entity);

    List<T> query(String sql);

    T queryOne(String sql);
}
