package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CompanyManager;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyManager entity.
 */
@SuppressWarnings("unused")
public interface CompanyManagerRepository extends JpaRepository<CompanyManager,Long> {

}
