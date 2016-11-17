package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceCapacityInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceCapacityInfo entity.
 */
@SuppressWarnings("unused")
public interface ServiceCapacityInfoRepository extends JpaRepository<ServiceCapacityInfo,Long> {

}
