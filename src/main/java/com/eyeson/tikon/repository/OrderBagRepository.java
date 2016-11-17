package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OrderBag;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderBag entity.
 */
@SuppressWarnings("unused")
public interface OrderBagRepository extends JpaRepository<OrderBag,Long> {

}
