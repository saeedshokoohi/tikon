package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.SelectorDataDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SelectorData and its DTO SelectorDataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SelectorDataMapper {

    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "parent.id", target = "parentId")
    SelectorDataDTO selectorDataToSelectorDataDTO(SelectorData selectorData);

    List<SelectorDataDTO> selectorDataToSelectorDataDTOs(List<SelectorData> selectorData);

    @Mapping(source = "typeId", target = "type")
    @Mapping(source = "parentId", target = "parent")
    SelectorData selectorDataDTOToSelectorData(SelectorDataDTO selectorDataDTO);

    List<SelectorData> selectorDataDTOsToSelectorData(List<SelectorDataDTO> selectorDataDTOs);

    default SelectorDataType selectorDataTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        SelectorDataType selectorDataType = new SelectorDataType();
        selectorDataType.setId(id);
        return selectorDataType;
    }

    default SelectorData selectorDataFromId(Long id) {
        if (id == null) {
            return null;
        }
        SelectorData selectorData = new SelectorData();
        selectorData.setId(id);
        return selectorData;
    }
}
