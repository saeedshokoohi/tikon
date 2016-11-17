package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OrderBagItemOption;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderBagItemOption entity.
 */
@SuppressWarnings("unused")
public interface OrderBagItemOptionRepository extends JpaRepository<OrderBagItemOption,Long> {

}
