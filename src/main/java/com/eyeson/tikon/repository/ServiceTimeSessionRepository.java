package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceTimeSession;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceTimeSession entity.
 */
@SuppressWarnings("unused")
public interface ServiceTimeSessionRepository extends JpaRepository<ServiceTimeSession,Long> {

}
