package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ThemeSettingInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ThemeSettingInfo and its DTO ThemeSettingInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThemeSettingInfoMapper {

    @Mapping(source = "logoImage.id", target = "logoImageId")
    ThemeSettingInfoDTO themeSettingInfoToThemeSettingInfoDTO(ThemeSettingInfo themeSettingInfo);

    List<ThemeSettingInfoDTO> themeSettingInfosToThemeSettingInfoDTOs(List<ThemeSettingInfo> themeSettingInfos);

    @Mapping(source = "logoImageId", target = "logoImage")
    ThemeSettingInfo themeSettingInfoDTOToThemeSettingInfo(ThemeSettingInfoDTO themeSettingInfoDTO);

    List<ThemeSettingInfo> themeSettingInfoDTOsToThemeSettingInfos(List<ThemeSettingInfoDTO> themeSettingInfoDTOs);

    default ImageData imageDataFromId(Long id) {
        if (id == null) {
            return null;
        }
        ImageData imageData = new ImageData();
        imageData.setId(id);
        return imageData;
    }
}
