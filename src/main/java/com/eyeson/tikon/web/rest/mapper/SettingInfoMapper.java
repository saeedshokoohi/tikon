package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.SettingInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SettingInfo and its DTO SettingInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {FinancialSettingMapper.class,ThemeSettingInfoMapper.class,NotificationSettingMapper.class })
public interface SettingInfoMapper {

    @Mapping(source = "themeSettingInfo.id", target = "themeSettingInfoId")
    @Mapping(source = "notificationSetting.id", target = "notificationSettingId")
    @Mapping(source = "financialSetting.id", target = "financialSettingId")
    SettingInfoDTO settingInfoToSettingInfoDTO(SettingInfo settingInfo);

    List<SettingInfoDTO> settingInfosToSettingInfoDTOs(List<SettingInfo> settingInfos);

//    @Mapping(source = "themeSettingInfoId", target = "themeSettingInfo")
//    @Mapping(source = "notificationSettingId", target = "notificationSetting")
//    @Mapping(source = "financialSettingId", target = "financialSetting")
    SettingInfo settingInfoDTOToSettingInfo(SettingInfoDTO settingInfoDTO);

    List<SettingInfo> settingInfoDTOsToSettingInfos(List<SettingInfoDTO> settingInfoDTOs);

    default ThemeSettingInfo themeSettingInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ThemeSettingInfo themeSettingInfo = new ThemeSettingInfo();
        themeSettingInfo.setId(id);
        return themeSettingInfo;
    }

    default NotificationSetting notificationSettingFromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setId(id);
        return notificationSetting;
    }

    default FinancialSetting financialSettingFromId(Long id) {
        if (id == null) {
            return null;
        }
        FinancialSetting financialSetting = new FinancialSetting();
        financialSetting.setId(id);
        return financialSetting;
    }
}
