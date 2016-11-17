package com.eyeson.tikon.repository.extended;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by saeed on 9/30/2016.
 */
public interface BaseExtendedRepository<T> {
     EntityManager getEm();

}
