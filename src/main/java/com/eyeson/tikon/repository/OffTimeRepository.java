package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OffTime;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the OffTime entity.
 */
@SuppressWarnings("unused")
public interface OffTimeRepository extends JpaRepository<OffTime,Long> {

    List<OffTime> findByFromTimeAndToTime(ZonedDateTime fromTime, ZonedDateTime toTime);

}
