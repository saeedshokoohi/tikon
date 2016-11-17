package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ScheduleBaseDiscount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduleBaseDiscount entity.
 */
@SuppressWarnings("unused")
public interface ScheduleBaseDiscountRepository extends JpaRepository<ScheduleBaseDiscount,Long> {

}
