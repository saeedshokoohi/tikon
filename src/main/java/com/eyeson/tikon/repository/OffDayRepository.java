package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.OffDay;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the OffDay entity.
 */
@SuppressWarnings("unused")
public interface OffDayRepository extends JpaRepository<OffDay,Long> {

    List<OffDay> findByOffDate(LocalDate offDate);
}
