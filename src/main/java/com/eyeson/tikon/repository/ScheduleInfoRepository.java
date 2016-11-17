package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ScheduleInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduleInfo entity.
 */
@SuppressWarnings("unused")
public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfo,Long> {

}
