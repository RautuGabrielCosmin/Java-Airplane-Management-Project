package repository;

import java.util.List;

public interface InterfaceRepository<ID, E> {
    E add(ID elem);
    E findById(ID id);
    List<E> findAll();
    E save(E entity);
    E update(E entity);
    E delete(ID id);
}
