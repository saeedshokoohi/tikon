package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.PriceInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PriceInfo entity.
 */
@SuppressWarnings("unused")
public interface PriceInfoRepository extends JpaRepository<PriceInfo,Long> {

    @Query("select distinct priceInfo from PriceInfo priceInfo left join fetch priceInfo.servants")
    List<PriceInfo> findAllWithEagerRelationships();

    @Query("select priceInfo from PriceInfo priceInfo left join fetch priceInfo.servants where priceInfo.id =:id")
    PriceInfo findOneWithEagerRelationships(@Param("id") Long id);

}
