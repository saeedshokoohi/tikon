package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ExtraSettingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ExtraSetting and its DTO ExtraSettingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExtraSettingMapper {

    @Mapping(source = "settingInfo.id", target = "settingInfoId")
    ExtraSettingDTO extraSettingToExtraSettingDTO(ExtraSetting extraSetting);

    List<ExtraSettingDTO> extraSettingsToExtraSettingDTOs(List<ExtraSetting> extraSettings);

    @Mapping(source = "settingInfoId", target = "settingInfo")
    ExtraSetting extraSettingDTOToExtraSetting(ExtraSettingDTO extraSettingDTO);

    List<ExtraSetting> extraSettingDTOsToExtraSettings(List<ExtraSettingDTO> extraSettingDTOs);

    default SettingInfo settingInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setId(id);
        return settingInfo;
    }
}
