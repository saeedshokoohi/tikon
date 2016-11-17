package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceCategory entity.
 */
@SuppressWarnings("unused")
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory,Long> {

    @Query("select distinct serviceCategory from ServiceCategory serviceCategory left join fetch serviceCategory.servants")
    List<ServiceCategory> findAllWithEagerRelationships();

    @Query("select serviceCategory from ServiceCategory serviceCategory left join fetch serviceCategory.servants where serviceCategory.id =:id")
    ServiceCategory findOneWithEagerRelationships(@Param("id") Long id);

}
