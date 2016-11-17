package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ServiceTimeSessionInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceTimeSessionInfo entity.
 */
@SuppressWarnings("unused")
public interface ServiceTimeSessionInfoRepository extends JpaRepository<ServiceTimeSessionInfo,Long> {

    List<ServiceTimeSessionInfo> findByServiceItemIdAndScheduleInfoId(Long serviceItemId, Long scheduleInfoId);
}
