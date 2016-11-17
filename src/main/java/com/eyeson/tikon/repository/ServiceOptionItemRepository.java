package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceOptionItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceOptionItem entity.
 */
@SuppressWarnings("unused")
public interface ServiceOptionItemRepository extends JpaRepository<ServiceOptionItem,Long> {

}
