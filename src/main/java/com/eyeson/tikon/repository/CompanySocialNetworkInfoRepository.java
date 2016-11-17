package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CompanySocialNetworkInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanySocialNetworkInfo entity.
 */
@SuppressWarnings("unused")
public interface CompanySocialNetworkInfoRepository extends JpaRepository<CompanySocialNetworkInfo,Long> {

}
