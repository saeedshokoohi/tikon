package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.TimePeriod;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TimePeriod entity.
 */
@SuppressWarnings("unused")
public interface TimePeriodRepository extends JpaRepository<TimePeriod,Long> {

    @Query("select distinct timePeriod from TimePeriod timePeriod left join fetch timePeriod.offtimes")
    List<TimePeriod> findAllWithEagerRelationships();

    @Query("select timePeriod from TimePeriod timePeriod left join fetch timePeriod.offtimes where timePeriod.id =:id")
    TimePeriod findOneWithEagerRelationships(@Param("id") Long id);

}
