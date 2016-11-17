package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OrderBagServiceItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderBagServiceItem entity.
 */
@SuppressWarnings("unused")
public interface OrderBagServiceItemRepository extends JpaRepository<OrderBagServiceItem,Long> {

}
