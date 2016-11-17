package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.SettingInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SettingInfo entity.
 */
@SuppressWarnings("unused")
public interface SettingInfoRepository extends JpaRepository<SettingInfo,Long> {

}
