package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.LocationInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LocationInfo entity.
 */
@SuppressWarnings("unused")
public interface LocationInfoRepository extends JpaRepository<LocationInfo,Long> {

}
