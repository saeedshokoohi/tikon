package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OrderBagServiceItemDtail;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderBagServiceItemDtail entity.
 */
@SuppressWarnings("unused")
public interface OrderBagServiceItemDtailRepository extends JpaRepository<OrderBagServiceItemDtail,Long> {

}
