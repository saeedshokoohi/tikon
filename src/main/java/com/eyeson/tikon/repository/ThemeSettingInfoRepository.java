package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ThemeSettingInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ThemeSettingInfo entity.
 */
@SuppressWarnings("unused")
public interface ThemeSettingInfoRepository extends JpaRepository<ThemeSettingInfo,Long> {

}
