package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.WeeklyWorkDay;

import com.eyeson.tikon.domain.enumeration.WeekDay;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WeeklyWorkDay entity.
 */
@SuppressWarnings("unused")
public interface WeeklyWorkDayRepository extends JpaRepository<WeeklyWorkDay,Long> {

    List<WeeklyWorkDay> findByWeekday(WeekDay weekday);
}
