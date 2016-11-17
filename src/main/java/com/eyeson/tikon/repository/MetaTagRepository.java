package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.MetaTag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MetaTag entity.
 */
@SuppressWarnings("unused")
public interface MetaTagRepository extends JpaRepository<MetaTag,Long> {

}
