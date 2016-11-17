package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.NotificationSettingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity NotificationSetting and its DTO NotificationSettingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationSettingMapper {

    NotificationSettingDTO notificationSettingToNotificationSettingDTO(NotificationSetting notificationSetting);

    List<NotificationSettingDTO> notificationSettingsToNotificationSettingDTOs(List<NotificationSetting> notificationSettings);

    NotificationSetting notificationSettingDTOToNotificationSetting(NotificationSettingDTO notificationSettingDTO);

    List<NotificationSetting> notificationSettingDTOsToNotificationSettings(List<NotificationSettingDTO> notificationSettingDTOs);
}
