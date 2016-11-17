package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.RelatedServiceItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RelatedServiceItem entity.
 */
@SuppressWarnings("unused")
public interface RelatedServiceItemRepository extends JpaRepository<RelatedServiceItem,Long> {

}
