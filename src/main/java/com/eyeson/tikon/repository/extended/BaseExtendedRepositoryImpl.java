package com.eyeson.tikon.repository.extended;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by saeed on 9/30/2016.
 */
public class BaseExtendedRepositoryImpl<T> implements BaseExtendedRepository<T> {

    @PersistenceContext
    EntityManager em;
    @Override
    public EntityManager getEm() {
        return em;
    }
}
