package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.Company;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select distinct company from Company company left join fetch company.tags")
    List<Company> findAllWithEagerRelationships();

    @Query("select company from Company company left join fetch company.tags where company.id =:id")
    Company findOneWithEagerRelationships(@Param("id") Long id);

}
