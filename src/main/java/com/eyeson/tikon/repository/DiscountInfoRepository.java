package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.DiscountInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiscountInfo entity.
 */
@SuppressWarnings("unused")
public interface DiscountInfoRepository extends JpaRepository<DiscountInfo,Long> {

}
