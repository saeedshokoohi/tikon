package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CapacityException;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CapacityException entity.
 */
@SuppressWarnings("unused")
public interface CapacityExceptionRepository extends JpaRepository<CapacityException,Long> {

}
