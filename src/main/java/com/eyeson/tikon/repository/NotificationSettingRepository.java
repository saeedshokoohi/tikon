package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.NotificationSetting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationSetting entity.
 */
@SuppressWarnings("unused")
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting,Long> {

}
