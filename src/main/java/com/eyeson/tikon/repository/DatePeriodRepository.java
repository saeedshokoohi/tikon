package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.DatePeriod;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DatePeriod entity.
 */
@SuppressWarnings("unused")
public interface DatePeriodRepository extends JpaRepository<DatePeriod,Long> {

    @Query("select distinct datePeriod from DatePeriod datePeriod left join fetch datePeriod.offdays")
    List<DatePeriod> findAllWithEagerRelationships();

    @Query("select datePeriod from DatePeriod datePeriod left join fetch datePeriod.offdays where datePeriod.id =:id")
    DatePeriod findOneWithEagerRelationships(@Param("id") Long id);

}
