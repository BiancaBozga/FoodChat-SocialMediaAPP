package com.example.demo2.business;







import com.example.demo2.domain.entities.Entity;

import java.util.Optional;

public interface Service<ID, E extends Entity<ID>> {

    Optional<E> add(E entity);

    Optional<E>  delete(ID id);

    Optional<E>  getEntityById(ID id);

    Iterable<E> getAll();
}
