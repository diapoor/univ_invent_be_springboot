package univinvent.services;

import java.util.List;

public interface IService<T> {
    public List<T> getAll();
    public T getOne(int id);
    public T add(T object);
    public T update(T object);

    public void delete(int id);
}
