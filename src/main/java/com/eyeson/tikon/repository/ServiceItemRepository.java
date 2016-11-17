package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceItem entity.
 */
@SuppressWarnings("unused")
public interface ServiceItemRepository extends JpaRepository<ServiceItem,Long> {

    @Query("select distinct serviceItem from ServiceItem serviceItem left join fetch serviceItem.options left join fetch serviceItem.serviceTimes left join fetch serviceItem.servants left join fetch serviceItem.tags left join fetch serviceItem.servicePriceInfo ")
    List<ServiceItem> findAllWithEagerRelationships();

    @Query("select serviceItem from ServiceItem serviceItem left join fetch serviceItem.options left join fetch serviceItem.serviceTimes left join fetch serviceItem.servants left join fetch serviceItem.tags left join fetch serviceItem.servicePriceInfo  where serviceItem.id =:id")
    ServiceItem findOneWithEagerRelationships(@Param("id") Long id);

}
