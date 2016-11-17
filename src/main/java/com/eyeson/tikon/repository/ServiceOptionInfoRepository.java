package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceOptionInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceOptionInfo entity.
 */
@SuppressWarnings("unused")
public interface ServiceOptionInfoRepository extends JpaRepository<ServiceOptionInfo,Long> {

}
