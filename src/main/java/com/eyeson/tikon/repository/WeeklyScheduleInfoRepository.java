package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.WeeklyScheduleInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the WeeklyScheduleInfo entity.
 */
@SuppressWarnings("unused")
public interface WeeklyScheduleInfoRepository extends JpaRepository<WeeklyScheduleInfo,Long> {

    @Query("select distinct weeklyScheduleInfo from WeeklyScheduleInfo weeklyScheduleInfo left join fetch weeklyScheduleInfo.datePeriods left join fetch weeklyScheduleInfo.dailyDurations left join fetch weeklyScheduleInfo.weekdays")
    List<WeeklyScheduleInfo> findAllWithEagerRelationships();

    @Query("select weeklyScheduleInfo from WeeklyScheduleInfo weeklyScheduleInfo left join fetch weeklyScheduleInfo.datePeriods left join fetch weeklyScheduleInfo.dailyDurations left join fetch weeklyScheduleInfo.weekdays where weeklyScheduleInfo.id =:id")
    WeeklyScheduleInfo findOneWithEagerRelationships(@Param("id") Long id);

}
