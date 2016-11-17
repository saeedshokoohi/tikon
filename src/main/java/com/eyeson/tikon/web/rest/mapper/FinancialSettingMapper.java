package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.FinancialSettingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity FinancialSetting and its DTO FinancialSettingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FinancialSettingMapper {

    FinancialSettingDTO financialSettingToFinancialSettingDTO(FinancialSetting financialSetting);

    List<FinancialSettingDTO> financialSettingsToFinancialSettingDTOs(List<FinancialSetting> financialSettings);

    FinancialSetting financialSettingDTOToFinancialSetting(FinancialSettingDTO financialSettingDTO);

    List<FinancialSetting> financialSettingDTOsToFinancialSettings(List<FinancialSettingDTO> financialSettingDTOs);
}
