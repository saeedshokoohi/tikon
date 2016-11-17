package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CancelingInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CancelingInfo entity.
 */
@SuppressWarnings("unused")
public interface CancelingInfoRepository extends JpaRepository<CancelingInfo,Long> {

}
